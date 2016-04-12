package services.base.interfaces.contacts;

import models.db.User;
import models.db.contacts.CGroup;
import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by eduardo on 5/04/15.
 */

public interface ContactGroupService extends GenericService<ContactGroup, Long>
{


    void addGroup(Contact contact, CGroup group);

    ContactGroup updateGroup(Long id, ContactGroup contactGroup) throws NotFoundException;

    void deleteGroup(ContactGroup contactGroup) throws NotFoundException;

    List<ContactGroup> findByContactId(Long id);


    List<ContactGroup> mergeContactGroup(Contact contact, List<ContactGroup> contactGroupList);
}
