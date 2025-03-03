package io.temporal.hackathon.codec;

import static io.temporal.common.converter.EncodingKeys.METADATA_ENCODING_KEY;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.protobuf.ByteString;
import io.temporal.api.common.v1.Payload;
import io.temporal.common.converter.DataConverterException;
import io.temporal.payload.codec.PayloadCodec;
import io.temporal.payload.codec.PayloadCodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.List;

public class SecurePayloadCodec implements PayloadCodec {

    private static final Logger log = LoggerFactory.getLogger(SecurePayloadCodec.class);

    private static final String CIPHER = "AES/GCM/NoPadding";
    private static final ByteString METADATA_ENCODING = ByteString.copyFrom("binary/encrypted", UTF_8);
    private static final int GCM_TAG_LENGTH_BIT = 128;
    private static final int GCM_NONCE_LENGTH_BYTE = 12;

    private static final String METADATA_ENCRYPTION_CIPHER_KEY = "encryption-cipher";
    private static final String METADATA_ENCRYPTION_KEY_ID_KEY = "encryption-key-id";
    private static final ByteString METADATA_ENCRYPTION_CIPHER = ByteString.copyFrom(CIPHER, UTF_8);

    private static final String AES = "AES";

    private final String currentKeyId;

    public SecurePayloadCodec() {
        this.currentKeyId = Long.toString(System.currentTimeMillis());
    }

    @Override
    @Nonnull
    public List<Payload> encode(List<Payload> payloads) {
        return payloads.stream()
                .map(this::encodePayload)
                .toList();
    }

    @Override
    @Nonnull
    public List<Payload> decode(List<Payload> payloads) {
        return payloads.stream()
                .map(this::decodePayload)
                .toList();
    }

    private Payload encodePayload(Payload payload) {
        if (payload == null) {
            throw new PayloadCodecException("Payload is null");
        }

        final EncodingKey currentKey = getCurrentKey();

        try {
            final byte[] encryptedData = encrypt(payload.toByteArray(), currentKey.key());

            return Payload.newBuilder()
                    .putMetadata(METADATA_ENCODING_KEY, METADATA_ENCODING)
                    .putMetadata(METADATA_ENCRYPTION_CIPHER_KEY, METADATA_ENCRYPTION_CIPHER)
                    .putMetadata(METADATA_ENCRYPTION_KEY_ID_KEY, ByteString.copyFromUtf8(currentKey.keyId()))
                    .setData(ByteString.copyFrom(encryptedData))
                    .build();
        } catch (Exception e) {
            log.error("Failed to encode payload={} with keyId={}", payload, currentKey, e);
            throw new DataConverterException("Encryption failed", e);
        }
    }

    private Payload decodePayload(Payload payload) {
        if (payload == null) {
            throw new PayloadCodecException("Payload is null");
        }
        if (METADATA_ENCODING.equals(
                payload.getMetadataOrDefault(METADATA_ENCODING_KEY, null))) {
            String keyId;
            try {
                keyId = payload.getMetadataOrThrow(METADATA_ENCRYPTION_KEY_ID_KEY).toString(UTF_8);
            } catch (Exception e) {
                log.error("Failed to decode payload={} with keyId", payload, e);
                throw new PayloadCodecException(e);
            }
            SecretKey key = getSecretKey(keyId);

            byte[] plainData;
            Payload decryptedPayload;

            try {
                plainData = decrypt(payload.getData().toByteArray(), key);
                decryptedPayload = Payload.parseFrom(plainData);
                return decryptedPayload;
            } catch (Exception e) {
                log.error("Failed to decode payload={} with keyId={}", payload, keyId, e);
                throw new PayloadCodecException("Decryption failed", e);
            }
        } else {
            return payload;
        }
    }

    private record EncodingKey(String keyId, SecretKey key) {}

    private SecretKey getSecretKey(String keyId) {
        // should be
        // keyRepository.getSecretKey(keyId);
        String key = keyId + "-secret-key";
        return new SecretKeySpec(key.getBytes(UTF_8), AES);
    }

    private EncodingKey getCurrentKey() {
        return new EncodingKey(currentKeyId, getSecretKey(currentKeyId));
    }

    private byte[] encrypt(byte[] plainData, SecretKey key) throws Exception {
        final byte[] nonce = getNonce(GCM_NONCE_LENGTH_BYTE);

        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH_BIT, nonce));

        byte[] encryptedData = cipher.doFinal(plainData);
        return ByteBuffer.allocate(nonce.length + encryptedData.length)
                .put(nonce)
                .put(encryptedData)
                .array();
    }

    private byte[] decrypt(byte[] encryptedDataWithNonce, SecretKey key) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(encryptedDataWithNonce);

        byte[] nonce = new byte[GCM_NONCE_LENGTH_BYTE];
        buffer.get(nonce);
        byte[] encryptedData = new byte[buffer.remaining()];
        buffer.get(encryptedData);

        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH_BIT, nonce));

        return cipher.doFinal(encryptedData);
    }

    private static byte[] getNonce(int size) {
        byte[] nonce = new byte[size];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }
}