package org.svuonline.anaskoara.webid.loginregistration.server;

/**
 * Created by anaskoara on 12/8/2017.
 */
public abstract class BidResponse {

    private String result;
    private String message;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
