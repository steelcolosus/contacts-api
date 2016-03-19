package models.db.contacts;

import models.db.base.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import static javax.persistence.FetchType.EAGER;
/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class Phone extends AbstractEntity {

    private String label;
    private String phoneNumber;

    @ManyToOne( cascade = CascadeType.ALL,fetch = EAGER)
    private Contact contact;

}
