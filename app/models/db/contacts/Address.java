package models.db.contacts;

import models.db.base.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.EAGER;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class Address extends AbstractEntity {
    private String address;
    @ManyToOne( cascade = CascadeType.ALL,fetch = EAGER)
    private Contact contact;
}
