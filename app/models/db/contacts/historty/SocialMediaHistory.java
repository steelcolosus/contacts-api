package models.db.contacts.historty;

import models.db.base.AbstractEntityHistory;

import javax.persistence.Entity;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class SocialMediaHistory extends AbstractEntityHistory {

    private String name;

}