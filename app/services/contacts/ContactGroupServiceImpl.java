package services.contacts;


import models.db.contacts.CGroup;
import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.libs.F;
import repositories.contacts.ContactGroupRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.ContactGroupService;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class ContactGroupServiceImpl extends AbstractService<ContactGroup, Long> implements ContactGroupService {

    ContactGroupRepository contactGroupRepository;
    VersionHistoryService versionHistoryService;
    @Autowired
    public ContactGroupServiceImpl(ContactGroupRepository contactGroupRepository,VersionHistoryService versionHistoryService) {
        super(contactGroupRepository);
        this.contactGroupRepository = contactGroupRepository;
        this.versionHistoryService  = versionHistoryService;

    }

    @Override
    public void addGroup(Contact contact, CGroup group){
        ContactGroup contactGroup = new ContactGroup();
        contactGroup.setContact(contact);
        contactGroup.setCGroup(group);
        super.save(contactGroup);
        List<ContactGroup> contactGroupList = findByContactId(contact.getId());
        F.Promise.promise(() -> versionHistoryService.newContactGroupVersion(contact, contactGroupList, false));

    }

    @Override
    public ContactGroup updateGroup(Long id, ContactGroup contactGroup) throws NotFoundException {
        ContactGroup updatedContactGroup =  update(id,contactGroup);
        List<ContactGroup> contactGroupList = findByContactId(updatedContactGroup.getContact().getId());
        F.Promise.promise(() -> versionHistoryService.newContactGroupVersion(contactGroup.getContact(), contactGroupList, false));
        return updatedContactGroup;
    }

    @Override
    public void deleteGroup(ContactGroup contactGroup) throws NotFoundException {
        delete(contactGroup.getId());
        F.Promise.promise(() -> versionHistoryService.newContactGroupVersion(contactGroup.getContact(), findByContactId(contactGroup.getContact().getId()), false));
    }

    @Override
    public List<ContactGroup> findByContactId(Long id) {
        return contactGroupRepository.findByContactId(id);
    }

    @Override
    public List<ContactGroup> mergeContactGroup(Contact contact, List<ContactGroup> contactGroupList) {
        List<ContactGroup> originalContactSocialMedia = contactGroupRepository.findByContactId(contact.getId());
        for (ContactGroup contactGroupMedia : contactGroupList) {
            ContactGroup newContactGroup = new ContactGroup();
            newContactGroup.setContact(contact);
            newContactGroup.setCGroup(contactGroupMedia.getCGroup());
            contactGroupRepository.save(newContactGroup);
            originalContactSocialMedia.add(newContactGroup);
        }
        return originalContactSocialMedia;
    }

}
