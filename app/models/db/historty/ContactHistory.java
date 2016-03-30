package models.db.historty;

import models.db.base.AbstractHistoryEntity;
import models.db.contacts.Contact;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class ContactHistory extends AbstractHistoryEntity {



    private String firstName;
    private String lastName;
    private String middleName;
    private String prefix;
    private String sufix;
    private String nickname;
    private Boolean isFavorite;


    private long contactId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSufix() {
        return sufix;
    }

    public void setSufix(String sufix) {
        this.sufix = sufix;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
}
