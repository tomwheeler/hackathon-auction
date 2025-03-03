package io.temporal.hackathon;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import java.time.Duration;
import io.temporal.workflow.Promise;

public class AuctionImpl implements Auction {

	private final Logger logger = Workflow.getLogger(AuctionImpl.class);

	private boolean hasEnded;
	private int currentPrice;
	private long lastBidTimestamp;

    @Override
    public int startAuction(String name) {
		while (!hasEnded) {
			Promise<Void> timer = Workflow.newTimer(Duration.ofSeconds(30));
			Workflow.await(() -> timer.isCompleted() || this.hasEnded);
			if (System.currentTimeMillis() - lastBidTimestamp > 30_000) {
				logger.info("Ending auction, no bid received in last 30 seconds");
				hasEnded = true;
			}
		}
        return currentPrice;
    }

	@Override
	public void bid(int amount) {
		logger.info("Bid received");
		lastBidTimestamp = System.currentTimeMillis();

		// use validator instead?
		if (amount <= currentPrice) {
			logger.info("Bid rejected. Amount {} is too low", amount);
			return;
		}

		logger.info("Bid accepted. Price is now {}", amount);
		currentPrice = amount;
	}

	@Override
	public void end() {
		hasEnded = true;
	}
}
