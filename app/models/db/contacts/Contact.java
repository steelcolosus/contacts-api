package models.db.contacts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.db.User;
import models.db.base.AbstractEntity;
import models.db.historty.ContactHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eduardo on 14/03/16.
 */
@SuppressWarnings("serial")
@Entity
public class Contact extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String middleName;
    private String prefix;
    private String sufix;
    private String nickname;
    private Boolean isFavorite;
    private Date deletedAt;

    @JsonIgnore
    private long hit;



    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Phone> phones = new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContactGroup> contactGroups= new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EmailAddress> emailAddresses= new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ContactSocialMedia> socialMediaList= new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Address> contactAdressList= new ArrayList<>();


    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public long getHit() {
        return hit;
    }

    public void setHit(long hit) {
        this.hit = hit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSufix() {
        return sufix;
    }

    public void setSufix(String sufix) {
        this.sufix = sufix;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<ContactGroup> getContactGroups() {
        return contactGroups;
    }

    public void setContactGroups(List<ContactGroup> contactGroups) {
        this.contactGroups = contactGroups;
    }

    public List<EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<EmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public List<ContactSocialMedia> getSocialMediaList() {
        return socialMediaList;
    }

    public void setSocialMediaList(List<ContactSocialMedia> socialMediaList) {
        this.socialMediaList = socialMediaList;
    }

    public List<Address> getContactAdressList() {
        return contactAdressList;
    }

    public void setContactAdressList(List<Address> contactAdressList) {
        this.contactAdressList = contactAdressList;
    }
}
