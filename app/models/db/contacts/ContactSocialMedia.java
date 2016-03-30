package models.db.contacts;

import models.db.base.AbstractEntity;
import models.db.historty.ContactSocialMediaHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class ContactSocialMedia extends AbstractEntity {

    private String url;

    @ManyToOne
    private Contact contact;
    @ManyToOne
    private SocialMedia socialMedia;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public SocialMedia getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }
}
