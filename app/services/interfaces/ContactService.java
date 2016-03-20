package services.interfaces;

import models.db.User;
import models.db.contacts.*;
import services.base.GenericService;

import java.util.List;

/**
 * Created by eduardo on 19/03/16.
 */
public interface ContactService extends GenericService<Contact, Long>{
    public void addAddress(int userId, Address address);
    public void addPhone(int userId, Phone phone);
    public void addSocialMedia(int contactId,int socialMediaId);
    public void addGroup(int contactId, int groupId );
    public void addEmailAddress(int contactId, EmailAddress emailAddress);
    public void mergeContact(Contact original, Contact updated);
    public Contact getFavorite();
    public List<Contact> getLastTenSearches();
    public User revertToPreviusVersion();
}
