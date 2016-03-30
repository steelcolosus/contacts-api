package services.contacts;


import models.db.User;
import models.db.contacts.*;
import models.db.historty.ContactHistory;
import models.db.historty.VersionHistory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.DateTimeContext;
import org.springframework.transaction.annotation.Transactional;

import play.libs.F;
import repositories.contacts.*;
import services.base.AbstractService;
import services.base.interfaces.history.VersionHistoryService;
import utils.ObjectUtils;
import utils.exceptions.NotFoundException;
import services.base.interfaces.contacts.ContactService;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class ContactServiceImpl extends AbstractService<Contact, Long> implements ContactService {


    ContactRepository contactRepository;
    AddressRepository addressRepository;
    ContactGroupRepository contactGroupRepository;
    ContactSocialMediaRepository contactSocialMediaRepository;
    EmailAddressRepository emailAddressRepository;
    GroupRepository groupRepository;
    PhoneRepository phoneRepository;
    SocialMediaRepository socialMediaRepository;
    VersionHistoryService versionHistoryService;


    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository,
                              AddressRepository addressRepository,
                              ContactGroupRepository contactGroupRepository,
                              ContactSocialMediaRepository contactSocialMediaRepository,
                              EmailAddressRepository emailAddressRepository,
                              GroupRepository groupRepository,
                              PhoneRepository phoneRepository,
                              SocialMediaRepository socialMediaRepository,
                              VersionHistoryService versionHistoryService) {

        super(contactRepository);
        this.addressRepository = addressRepository;
        this.contactGroupRepository = contactGroupRepository;
        this.contactSocialMediaRepository = contactSocialMediaRepository;
        this.emailAddressRepository = emailAddressRepository;
        this.groupRepository = groupRepository;
        this.phoneRepository = phoneRepository;
        this.socialMediaRepository = socialMediaRepository;
        this.versionHistoryService = versionHistoryService;
    }

    @Transactional
    @Override
    public Contact save(Contact contact) {
        Contact newContact = super.save(contact);
        F.Promise.promise(() -> versionHistoryService.newVersion(newContact, false));
        return newContact;
    }

    @Override
    public Contact update(Long id, Contact contact) throws NotFoundException {

        Contact contactToUpdate = findById(id);
        final Contact contactToVersion = contactToUpdate;

        if (contactToUpdate == null)
            throw new NotFoundException("Contact not found with id: " + id);

        ObjectUtils.copyProperties(contact, contactToUpdate);
        contactToUpdate = save(contactToUpdate);
        F.Promise.promise(() -> versionHistoryService.newVersion(contactToVersion, false));
        return contactToUpdate;
    }

    @Override
    public boolean delete(Long id) {
        Contact contact = contactRepository.findOne(id);
        if (contact == null)
            return false;
        contact.setDeletedAt(DateTime.now().toDate());
        save(contact);
        F.Promise.promise(() -> versionHistoryService.newVersion(contact, true));
        return true;
    }

    @Override
    public void addAddress(long contactId, Address address) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        address.setContact(contact);
        addressRepository.save(address);
        F.Promise.promise(() -> versionHistoryService.newVersion(contact, address, false));
    }

    @Override
    public void addPhone(long contactId, Phone phone) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        phone.setContact(contact);
        phoneRepository.save(phone);
        F.Promise.promise(() -> versionHistoryService.newVersion(contact, phone, false));
    }

    @Override
    public void addSocialMedia(long contactId, long socialMediaId) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        SocialMedia socialMedia = socialMediaRepository.findOne(socialMediaId);
        if (contact == null || socialMedia == null) {
            throw new NotFoundException("Contact not found with id:" + contactId);
        }
        ContactSocialMedia contactSocialMedia = new ContactSocialMedia();
        contactSocialMedia.setContact(contact);
        contactSocialMedia.setSocialMedia(socialMedia);
        contactSocialMediaRepository.save(contactSocialMedia);
        F.Promise.promise(() -> versionHistoryService.newVersion(contact, contactSocialMedia, false));
    }

    @Override
    public void addGroup(long contactId, long groupId) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        Group group = groupRepository.findOne(groupId);
        if (contact == null || group == null) {
            throw new NotFoundException("Couldn't add user to specified group");
        }
        ContactGroup contactGroup = new ContactGroup();
        contactGroup.setContact(contact);
        contactGroup.setGroup(group);
        contactGroupRepository.save(contactGroup);
        F.Promise.promise(() -> versionHistoryService.newVersion(contact, contactGroup, false));
    }

    @Override
    public void addEmailAddress(long contactId, EmailAddress emailAddress) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        if (contact == null)
            throw new NotFoundException("Contact not found with id: " + contactId);
        emailAddress.setContact(contact);
        emailAddressRepository.save(emailAddress);
        F.Promise.promise(() -> versionHistoryService.newVersion(contact, emailAddress, false));
    }

    @Override
    public Contact getContact(long contactId) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        if (contact == null)
            throw new NotFoundException("Contact not found with id: " + contactId);
        long newHit = contact.getHit() + 1;
        contact.setHit(newHit);
        F.Promise.promise(() -> contactRepository.save(contact));
        return contact;
    }

    @Override
    public List<Contact> getAllContacts(long userId) throws NotFoundException {
        List<Contact> contacts = contactRepository.findContactByUserId(userId);
        if (contacts == null || contacts.isEmpty())
            throw new NotFoundException("Can't find contacts for specified userId: " + userId);
        return contacts;
    }

    @Override
    public Contact mergeContact(long originalId, long updateId) throws NotFoundException {
        Contact original = contactRepository.findOne(originalId);
        Contact update = contactRepository.findOne(updateId);
        if (original == null || update == null)
            throw new NotFoundException("Unable to merge original contact id: " + originalId + " with contact id: " + updateId);
        mergeContactBasicInfo(original, update);
        mergeContactPhones(original, update);
        original.getContactGroups().addAll(update.getContactGroups());
        mergeContactEmails(original, update);
        original.getSocialMediaList().addAll(update.getSocialMediaList());
        original.getContactAdressList().addAll(update.getContactAdressList());
        Contact mergedContact = contactRepository.save(original);
        F.Promise.promise(() -> versionHistoryService.newVersion(mergedContact, false));
        return mergedContact;
    }

    private void mergeContactEmails(Contact original, Contact update) {
        for (EmailAddress updateEmail : update.getEmailAddresses()) {
            boolean duplicate = false;
            for (EmailAddress originalEmail : original.getEmailAddresses()) {
                if (updateEmail.getEmail().trim().equalsIgnoreCase(originalEmail.getEmail())) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate)
                original.getEmailAddresses().add(updateEmail);
        }
    }

    private void mergeContactPhones(Contact original, Contact update) {
        for (Phone updatePhone : update.getPhones()) {
            boolean duplicate = false;
            for (Phone originalPhone : original.getPhones()) {
                if (updatePhone.getPhoneNumber().trim().equalsIgnoreCase(originalPhone.getPhoneNumber())) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate)
                original.getPhones().add(updatePhone);
        }
    }

    private void mergeContactBasicInfo(Contact original, Contact update) {
        original.setFirstName(update.getFirstName());
        original.setLastName(update.getLastName());
        original.setMiddleName(update.getMiddleName());
        original.setPrefix(update.getPrefix());
        original.setNickname(update.getNickname());
        if (update.getFavorite())
            original.setFavorite(update.getFavorite());
    }

    @Override
    public List<Contact> getFavorites(long userId) {
        return contactRepository.findFavoriteContacts(userId);
    }

    @Override
    public List<Contact> getLastTenSearches(long userId) {
        List<Contact> contacts = contactRepository.findLastTenSearchedContacts(userId);
        return contacts;
    }

    @Override
    public List<Contact> getMostSearchedContacts(long userId) {
        List<Contact> contacts = contactRepository.findMostSearchedContacts(userId);
        return contacts;
    }

    @Override
    public Contact revertToPreviousVersion(long contactId) throws NotFoundException {
        Optional<VersionHistory> _versionHistory = versionHistoryService.getPreviousVersion(contactId);
        if (!_versionHistory.isPresent())
            return null;
        Contact contact = versionHistoryService.getContact(_versionHistory.get());
        contact = update(contactId, contact);
        return contact;
    }
}
