package org.svuonline.anaskoara.webid.loginregistration.models;

import java.util.List;

/**
 * Created by anaskoara on 12/2/2017.
 */

public class Product {
    private String name;
    private User user;
    private boolean sold;
    private int initialPrice;
    private int id;
    private int maxBid;
    private User buyer;




    private List<BidOffer> bids;

    public Product(int id, String s) {
        this.id=id;
        this.name=s;
    }

    public Product() {

    }


    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public int getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getMaxBid() {
        return maxBid;
    }

    public void setMaxBid(int maxBid) {
        this.maxBid = maxBid;
    }

    public void setBids(List<BidOffer> bids) {
        this.bids = bids;
    }

    public List<BidOffer> getBids() {
        return bids;
    }
}