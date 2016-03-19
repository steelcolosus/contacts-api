package models.db.contacts.historty;

import models.db.base.AbstractEntityHistory;

import javax.persistence.*;

/**
 * Created by eduardo on 15/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class ContactHistory extends AbstractEntityHistory {



    private String firstName;
    private String lastName;
    private String middleName;
    private String prefix;
    private String sufix;
    private String nickname;




}
