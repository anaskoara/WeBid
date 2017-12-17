package org.svuonline.anaskoara.webid.loginregistration.server;

import org.svuonline.anaskoara.webid.loginregistration.models.Product;

public class ProductResponse extends BidResponse{
    private Product product;
    public Product getProduct() {
        return product;
    }
}
