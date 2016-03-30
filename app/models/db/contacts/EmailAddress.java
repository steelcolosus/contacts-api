package models.db.contacts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.db.base.AbstractEntity;
import models.db.historty.EmailAddressHistory;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class EmailAddress extends AbstractEntity {

    private String email;

    @JsonIgnore
    @ManyToOne( cascade = CascadeType.ALL,fetch = EAGER)
    private Contact contact;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
