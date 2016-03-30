package models.db.historty;

import models.db.base.AbstractHistoryEntity;

import javax.persistence.Entity;

import java.util.UUID;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class ContactGroupHistory extends AbstractHistoryEntity {


    private Long groupId;


    private UUID contactVersion;


    private long contactGroupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupVersion) {
        this.groupId = groupVersion;
    }

    public UUID getContactVersion() {
        return contactVersion;
    }

    public void setContactVersion(UUID contactVersion) {
        this.contactVersion = contactVersion;
    }

    public long getContactGroupId() {
        return contactGroupId;
    }

    public void setContactGroupId(long contactGroupId) {
        this.contactGroupId = contactGroupId;
    }
}
