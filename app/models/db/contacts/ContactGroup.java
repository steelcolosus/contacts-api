package models.db.contacts;

import models.db.base.AbstractEntity;
import models.db.historty.ContactGroupHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class ContactGroup extends AbstractEntity {

    @ManyToOne
    private Contact contact;
    @ManyToOne
    private Group group;


    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
