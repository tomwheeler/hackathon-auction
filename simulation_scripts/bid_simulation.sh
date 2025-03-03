#!/bin/bash

# Constants
CSV_FILE="$(dirname "$(pwd)")/simulation_scripts/bids.csv"
WORKFLOW_ID="car"
SIGNAL_NAME="bid"

# Process CSV file line by line
while IFS=',' read -r user_id bid_amount delay || [ -n "$user_id" ]; do
    # Skip the header row
    if [[ "$user_id" == "user_id" ]]; then
        continue
    fi

    echo "Sending bid from $user_id: $bid_amount"
    SIGNAL_OUTPUT=$(temporal workflow signal --workflow-id "$WORKFLOW_ID" --name "$SIGNAL_NAME" --input "\"$bid_amount\"")
    echo "Signal output: $SIGNAL_OUTPUT"

    # Use the delay from the CSV file
    sleep "$delay"
done < "$CSV_FILE"

echo "Auction bids completed!"