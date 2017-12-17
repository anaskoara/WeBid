package org.svuonline.anaskoara.webid.loginregistration.server;

import org.svuonline.anaskoara.webid.loginregistration.models.Product;
import org.svuonline.anaskoara.webid.loginregistration.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerResponse  extends BidResponse{

    private User user;
    private HashMap<String,String> data;
    private ArrayList<Product> products;

    public User getUser() {
        return user;
    }

    public HashMap<String, String> getData() {
        return data;
    }


    public ArrayList<Product> getProducts() {
        return products;
    }
}
