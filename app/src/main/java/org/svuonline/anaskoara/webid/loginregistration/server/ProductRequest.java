package org.svuonline.anaskoara.webid.loginregistration.server;

import org.svuonline.anaskoara.webid.loginregistration.models.Product;
import org.svuonline.anaskoara.webid.loginregistration.models.User;

/**
 * Created by anaskoara on 12/8/2017.
 */

public class ProductRequest extends BidRequest {
    private int id;
    private User user;
    private int bid;
    private Product product;


    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }


    public void setProduct(Product product) {
        this.product = product;
    }

}