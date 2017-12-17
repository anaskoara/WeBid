package  org.svuonline.anaskoara.webid.loginregistration.models;


public class User {

    private String name;
    private String login;
    private String tel;
    private String id;
    private String password;
    private String old_password;
    private String new_password;


    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  User){
            return id==((User)obj).id;
        }
        return false;
    }

    public String getTel() {
        return tel;
    }
}
