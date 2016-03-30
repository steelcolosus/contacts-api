package models.db.contacts;

import models.db.base.AbstractEntity;

import javax.persistence.Entity;

/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class SocialMedia extends AbstractEntity {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
