package models.db.historty;

import models.db.base.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import java.util.UUID;

import static javax.persistence.FetchType.EAGER;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class VersionHistory extends AbstractEntity {

    private long contactId;

    private UUID contactVersion;

    private UUID addressVersion;

    private UUID contactGroupVersion;

    private UUID contactSocialMediaVersion;

    private UUID emailAddressVersion;

    private UUID phoneVersion;

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public UUID getContactVersion() {
        return contactVersion;
    }

    public void setContactVersion(UUID contactVersion) {
        this.contactVersion = contactVersion;
    }

    public UUID getAddressVersion() {
        return addressVersion;
    }

    public void setAddressVersion(UUID addressVersion) {
        this.addressVersion = addressVersion;
    }

    public UUID getContactGroupVersion() {
        return contactGroupVersion;
    }

    public void setContactGroupVersion(UUID contactGroupVersion) {
        this.contactGroupVersion = contactGroupVersion;
    }

    public UUID getContactSocialMediaVersion() {
        return contactSocialMediaVersion;
    }

    public void setContactSocialMediaVersion(UUID contactSocialMediaVersion) {
        this.contactSocialMediaVersion = contactSocialMediaVersion;
    }

    public UUID getEmailAddressVersion() {
        return emailAddressVersion;
    }

    public void setEmailAddressVersion(UUID emailAddressVersion) {
        this.emailAddressVersion = emailAddressVersion;
    }

    public UUID getPhoneVersion() {
        return phoneVersion;
    }

    public void setPhoneVersion(UUID phoneVersion) {
        this.phoneVersion = phoneVersion;
    }
}
