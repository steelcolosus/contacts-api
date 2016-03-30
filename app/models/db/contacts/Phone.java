package models.db.contacts;

import models.db.base.AbstractEntity;
import models.db.historty.PhoneHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class Phone extends AbstractEntity {

    private String label;
    private String phoneNumber;

    @ManyToOne( cascade = CascadeType.ALL,fetch = EAGER)
    private Contact contact;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
