package services.base.interfaces.contacts;

import models.db.contacts.*;
import models.dtos.ContactDTO;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by eduardo on 19/03/16.
 */

public interface ContactService extends GenericService<Contact, Long> {


    ContactDTO saveContact(ContactDTO contactDTO) throws NotFoundException;

    Contact updateContact(Long id, Contact contact) throws NotFoundException;

    boolean deleteContact(Long id) throws NotFoundException;

    void addToFavorite(long contactId) throws NotFoundException;


    ContactDTO getContact(long contactId) throws NotFoundException;

    List<ContactDTO> getAllContacts(long userId) throws NotFoundException;

    ContactDTO mergeContact(long original, long updated) throws NotFoundException;

    List<ContactDTO> getFavorites(long userId) throws NotFoundException;

    List<ContactDTO> getLastTenSearches(long userId) throws NotFoundException;

    List<ContactDTO> getMostSearchedContacts(long userId) throws NotFoundException;

    ContactDTO revertToPreviousVersion(long contactId) throws NotFoundException;

    ContactDTO revertToVersion(long id) throws NotFoundException;

    List<ContactDTO> getContactHistory(long contactId) throws NotFoundException;
}
