package org.svuonline.anaskoara.webid.loginregistration.models;

/**
 * Created by anaskoara on 12/8/2017.
 */

public class BidOffer {
    private int	id;
    private User user;
    private Product product;
    private int offeredPrice;


    public Product getProduct() {
        return product;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public int getOfferedPrice() {
        return offeredPrice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOfferedPrice(int offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
