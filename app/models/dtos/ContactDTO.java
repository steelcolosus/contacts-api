package models.dtos;

import models.db.contacts.*;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 31/03/16.
 */
public class ContactDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String prefix;
    private String sufix;
    private String nickname;
    private Boolean isFavorite;
    private long versionHistoryId;
    private long userId;
    private boolean isDeleted;


    private List<Phone> phones = new ArrayList<>();

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    private List<ContactGroup> contactGroups = new ArrayList<>();


    private List<EmailAddress> emailAddresses = new ArrayList<>();

    private List<ContactSocialMedia> socialMediaList = new ArrayList<>();

    private List<Address> contactAdressList = new ArrayList<>();

    public long getVersionHistoryId() {
        return versionHistoryId;
    }

    public void setVersionHistoryId(long versionHistoryId) {
        this.versionHistoryId = versionHistoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
