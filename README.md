# hackathon-auction
Hackathon project for the cyan table

## Start the Worker

```bash
mvn compile exec:java -Dexec.mainClass="io.temporal.hackathon.AuctionWorker"
```

## Start the Workflow

```bash
mvn compile exec:java -Dexec.mainClass="io.temporal.hackathon.Starter"
```


## Send a bid (via a Signal)

### Using the CLI

```bash
temporal workflow signal --workflow-id 'car' --name 'bid' --input '"tom"' --input '100'
```

### Using the `AuctionBidder` program 

This takes the username from the environment:

```bash
mvn compile exec:java -Dexec.mainClass="io.temporal.hackathon.AuctionBidder" -Dexec.args="100"
```
