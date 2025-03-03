package io.temporal.hackathon;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.SignalMethod;

@WorkflowInterface
public interface Auction {

	@WorkflowMethod
	int startAuction(String name);


	@SignalMethod
	void bid(int price);

	@SignalMethod
	void end();

}
