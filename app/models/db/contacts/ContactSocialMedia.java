package models.db.contacts;

import models.db.base.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
}
