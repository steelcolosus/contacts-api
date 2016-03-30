package models.db.historty;

import models.db.base.AbstractHistoryEntity;

import javax.persistence.Entity;

import java.util.UUID;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class ContactSocialMediaHistory extends AbstractHistoryEntity {


    private UUID contactVersion;

    private Long socialMediaId;

    private String url;

    private long contactSocialMediaId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UUID getContactVersion() {
        return contactVersion;
    }

    public void setContactVersion(UUID contactVersion) {
        this.contactVersion = contactVersion;
    }

    public Long getSocialMediaId() {
        return socialMediaId;
    }

    public void setSocialMediaId(Long socialMediaVersion) {
        this.socialMediaId = socialMediaVersion;
    }

    public long getContactSocialMediaId() {
        return contactSocialMediaId;
    }

    public void setContactSocialMediaId(long contactSocialMediaId) {
        this.contactSocialMediaId = contactSocialMediaId;
    }
}
