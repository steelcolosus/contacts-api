package models.db.contacts;

import models.db.base.AbstractEntity;

import javax.persistence.*;

/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class ContactGroup extends AbstractEntity {

    @ManyToOne
    private Contact contact;
    @ManyToOne
    private CGroup group;


    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public CGroup getCGroup() {
        return group;
    }

    public void setCGroup(CGroup CGroup) {
        this.group = CGroup;
    }
}
