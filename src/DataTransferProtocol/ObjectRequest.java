package DataTransferProtocol;

import java.io.Serializable;

/**
 * Created by adt5 on 17/01/18.
 */
public class ObjectRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;
    private String extension;
    private String user;
    private Boolean modify;

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

    public Boolean getModify() {
        return modify;
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

    public void setModify(Boolean modify) {
        this.modify = modify;
    }
}
