package services.base.interfaces.contacts;

import models.db.User;
import models.db.contacts.*;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by eduardo on 19/03/16.
 */

public interface ContactService extends GenericService<Contact, Long>{

    public void addAddress(long userId, Address address) throws NotFoundException;
    public void addPhone(long userId, Phone phone) throws NotFoundException;
    public void addSocialMedia(long contactId,long socialMediaId) throws NotFoundException;
    public void addGroup(long contactId, long groupId ) throws NotFoundException;
    public void addEmailAddress(long contactId, EmailAddress emailAddress) throws NotFoundException;

    public Contact getContact(long contactId) throws NotFoundException;
    public List<Contact> getAllContacts(long userId) throws NotFoundException;

    public Contact mergeContact(long original, long updated)throws NotFoundException;
    public List<Contact> getFavorites(long userId);
    public List<Contact> getLastTenSearches(long userId);
    public List<Contact> getMostSearchedContacts(long userId);
    public Contact revertToPreviousVersion(long contactId) throws NotFoundException;
}
