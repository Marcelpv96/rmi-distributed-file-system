package webservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "rmiuser")
public class User implements Serializable {

    @Id
    @Column
    private String id;

    @Column(name = "password")
    private String password;

}
