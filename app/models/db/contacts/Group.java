package models.db.contacts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.db.base.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class Group extends AbstractEntity {

    private String name;



    @JsonIgnore
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContactGroup> contactList= new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContactGroup> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactGroup> contactList) {
        this.contactList = contactList;
    }
}
