package webservice.model;

import javax.persistence.*;

@Entity
@Table(name = "mytubeuser")
public class User {

    @Id
    @Column(name = "userName")
    private String userName;

/*
    @Column(name = "password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
*/
    @Column(name = "localAddress")
    private String localAddress;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }
}
