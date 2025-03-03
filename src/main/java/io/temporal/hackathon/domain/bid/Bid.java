package io.temporal.hackathon.domain.bid;

public record Bid(
        String userId,
        Long amount,
        boolean isValid,
        long timestamp
) {
}

