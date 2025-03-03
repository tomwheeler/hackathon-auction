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

```bash
mvn compile exec:java -Dexec.mainClass="io.temporal.hackathon.AuctionBidder" -Dexec.args="{bid_price}"
```
