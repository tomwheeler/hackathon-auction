#!/bin/bash

# Constants
CSV_FILE="$(dirname "$(pwd)")/simulation_scripts/bids.csv"
WORKFLOW_ID="car"
SIGNAL_NAME="bid"
BID_TIME_WINDOW=5  # Time window in seconds

# Process CSV file line by line
while IFS=',' read -r user_id bid_amount; do
    # Skip the header row
    if [[ "$user_id" == "user_id" ]]; then
        continue
    fi

    echo "Sending bid from $user_id: $bid_amount"
    SIGNAL_OUTPUT=$(temporal workflow signal --workflow-id "$WORKFLOW_ID" --name "$SIGNAL_NAME" --input "\"$bid_amount\"")
    echo "Signal output: $SIGNAL_OUTPUT"

    # Simulate slight delay
    sleep 1
done < "$CSV_FILE"

echo "Auction completed!"