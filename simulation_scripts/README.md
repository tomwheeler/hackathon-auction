# Auction Bid Simulation with Temporal

## Overview

This project simulates an auction where users submit bids via a CSV file. The bids are processed within a **5-second time window**, and signals are sent to a **Temporal workflow** to determine the highest bidder.

## Features

- Reads **bids from a CSV file**.
- Sends **bid signals to a Temporal workflow**.
- Ends the auction after **30 seconds** and determines the winner.

## Requirements

- Temporal Server
- Temporal CLI
- Maven

## Installation

1. **Install Temporal CLI**:
   Follow the instructions on the [Temporal CLI GitHub page](https://github.com/temporalio/tctl) to install the Temporal CLI.

2. **Install Maven**:
   Follow the instructions on the [Maven website](https://maven.apache.org/install.html) to install Maven.

3. **Start Temporal Server** (if not running):
   ```sh
   temporal server start-dev
   ```

## Running the Auction

1. **Prepare a CSV file** (`bids.csv`) with the following format:
   ```csv
   user_id,bid_amount
   user1,12000
   user2,15000
   user3,9000  # This will be rejected
   user4,20000
   ```

2. **Run the auction script**:
   ```sh
   ./simulation_scripts/bid_simulation.sh
   ```

3. The auction will run for **30 seconds**, and valid bids will be sent as **signals** to the Temporal workflow.

4. After 30 seconds, the workflow determines the **highest bidder** and announces the winner.

## Example Output

```sh
Sending bid from user1: 12000
Signal output: Bid received: user1 - $12000
Sending bid from user2: 15000
Signal output: Bid received: user2 - $15000
Sending bid from user3: 9000
Signal output: Bid from user3 rejected (below 10000$)
Sending bid from user4: 20000
Signal output: Bid received: user4 - $20000
Auction completed!
```

## License

MIT License