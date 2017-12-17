package org.svuonline.anaskoara.webid.loginregistration.server;


import org.svuonline.anaskoara.webid.loginregistration.models.User;

import java.util.HashMap;

public class ServerRequest extends BidRequest {


    private User user;
    private HashMap<String,String> parameters;



    public void setUser(User user) {
        this.user = user;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }
}
