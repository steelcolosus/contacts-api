package models.db.contacts.historty;

import models.db.base.AbstractEntity;

import javax.persistence.Entity;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class VersionHistory extends AbstractEntity {

    private long contactVersion;
    private long addressVersion;
    private long contactGroupVersion;
    private long contactSocialMediaVersion;
    private long emailAddressVersion;
    private long groupVersion;
    private long phoneVersion;
    private long socialMediaVersion;

}
