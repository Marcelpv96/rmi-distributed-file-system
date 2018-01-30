package webservice.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "file")
public class File implements Serializable {

    @Id
    @Column
    private String id;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "userName")
    private String userName;

    @Column(name = "encryped")
    private boolean encrypted;

    @Column(name = "extension")
    private String extension;

    @Column(name = "address")
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toUpperCase(String str) {
        return str.toUpperCase();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    @Override
    public String toString() {
        return String.format("File[id=%d, fileName='%s', address='%s', category=%s]", id, fileName, address, extension);
    }
}