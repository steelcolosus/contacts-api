package models.db.historty;

import models.db.base.AbstractHistoryEntity;
import models.db.contacts.EmailAddress;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class EmailAddressHistory extends AbstractHistoryEntity {

    private String emailAddress;


    private long baseEmailAddressId;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getBaseEmailAddressId() {
        return baseEmailAddressId;
    }

    public void setBaseEmailAddressId(long baseEmailAddressId) {
        this.baseEmailAddressId = baseEmailAddressId;
    }
}
