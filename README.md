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


## Send a bid (Signal)

```bash
temporal workflow signal --workflow-id 'car' --name 'bid' --input '{"user": "tom", "amount": 100}'
```
