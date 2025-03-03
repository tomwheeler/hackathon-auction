package io.temporal.hackathon.domain.temporal;

import io.temporal.hackathon.domain.auction.AuctionStats;
import io.temporal.workflow.CancellationScope;
import io.temporal.workflow.ContinueAsNewOptions;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import java.time.Duration;
import io.temporal.workflow.Promise;

public class AuctionWorkflowImpl implements AuctionWorkflow {

	private final Logger logger = Workflow.getLogger(AuctionWorkflowImpl.class);

	private static final Duration THIRTY_SECONDS = Duration.ofSeconds(30);
	private CancellationScope cancellationScope;
	private Promise<Void> timerPromise;
	private boolean hasEnded;
	private long currentPrice;
	private long lastBidTimestamp;

    @Override
    public long startAuction(String auctionId) {
		while (!hasEnded) {
			createAndStartTimer();
			if (System.currentTimeMillis() - lastBidTimestamp > 30_000) {
				logger.info("Ending auction, no bid received in last 30 seconds");
				hasEnded = true;
			}
		}
        return currentPrice;
    }

	private void createAndStartTimer() {
		cancellationScope = Workflow.newCancellationScope(
				() -> timerPromise = Workflow.newTimer(THIRTY_SECONDS)
		);
		cancellationScope.run();
		Workflow.await(() -> timerPromise.isCompleted() || this.hasEnded);
	}

	@Override
	public void bid(String userId, Long amount) {
		logger.info("Bid received");
		lastBidTimestamp = System.currentTimeMillis();

		// use validator instead?
		if (amount <= currentPrice || hasEnded) {
			logger.info("Bid rejected. Amount {} is too low", amount);
			return;
		}

		logger.info("Bid accepted. Price is now {}", amount);
		currentPrice = amount;
		cancellationScope.cancel();
		createAndStartTimer();
	}

	@Override
	public AuctionStats getStats(String auctionId) {
		return null;
	}

	@Override
	public void end() {
		hasEnded = true;
	}
}
