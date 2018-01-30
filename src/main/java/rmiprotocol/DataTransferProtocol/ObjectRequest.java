package rmiprotocol.DataTransferProtocol;

import java.io.Serializable;

public class ObjectRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String extension;
    private String user;

    public ObjectRequest() {
    }

    public String getTitle() {
        return title;
    }

    public String getExtension() {
        return extension;
    }

    public String getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
