package io.temporal.hackathon.domain.temporal;

import io.temporal.hackathon.domain.auction.AuctionState;
import io.temporal.hackathon.domain.auction.AuctionStats;
import io.temporal.hackathon.domain.bid.Bid;
import io.temporal.hackathon.domain.timer.Timer;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.LinkedList;

public class AuctionWorkflowImpl implements AuctionWorkflow {

	private final Logger logger = Workflow.getLogger(AuctionWorkflowImpl.class);

	private static final Duration THIRTY_SECONDS = Duration.ofSeconds(30);
	private final Timer timer = new Timer();
	private long currentPrice;
	private long lastBidTimestamp;
	private LinkedList<Bid> bids = new LinkedList<>();
	private String auctionId;
	private AuctionState state;

    @Override
    public long startAuction(String auctionId) {
			this.state = AuctionState.STARTED;

		while (!state.equals(AuctionState.COMPLETED)) {
			timer.sleep(Workflow.currentTimeMillis() + THIRTY_SECONDS.toMillis());
			if (timer.getSleepTime() > THIRTY_SECONDS.toMillis()) {
				logger.info("Ending auction, no bid received in last 30 seconds");
				state = AuctionState.COMPLETED;
			}
		}
        return currentPrice;
    }

	@Override
	public void bid(String userId, Long amount) {
		logger.info("Bid received");
		lastBidTimestamp = Workflow.currentTimeMillis();


		// use validator instead?
		boolean isValid = amount > currentPrice;
		bids.add(new Bid(userId, amount, isValid, lastBidTimestamp));

		if (!isValid) {
			logger.info("Bid rejected. Amount {} is too low", amount);
			return;
		}

		logger.info("Bid accepted. Price is now {}", amount);
		currentPrice = amount;
		timer.updateWakeUpTime(Workflow.currentTimeMillis() + THIRTY_SECONDS.toMillis());
		logger.info("Timer updated. Current sleep time is {}", timer.getSleepTime());
	}

	@Override
	public AuctionStats getStats() {
		return new AuctionStats(auctionId, state , bids);
	}

	@Override
	public void end() {
		state = AuctionState.COMPLETED;
	}
}
