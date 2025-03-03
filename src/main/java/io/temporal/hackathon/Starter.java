package io.temporal.hackathon;

public class Starter {

    public static void main(String[] args) {
        Auction auction = new AuctionImpl();
        String hello = auction.sayHello(args[0]);
        System.out.println(hello);
    }
}
