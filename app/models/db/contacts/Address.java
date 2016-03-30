package models.db.contacts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.db.base.AbstractEntity;
import models.db.historty.AddressHistory;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class Address extends AbstractEntity {
    private String address;

    @JsonIgnore
    @ManyToOne( cascade = CascadeType.ALL,fetch = EAGER)
    private Contact contact;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
