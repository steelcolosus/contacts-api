package services.contacts;


import models.db.User;
import models.db.contacts.*;
import models.db.historty.VersionHistory;
import models.dtos.ContactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.libs.F;
import repositories.contacts.ContactRepository;
import services.base.AbstractService;
import services.base.interfaces.UserService;
import services.base.interfaces.contacts.*;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
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
    AddressService addressService;
    ContactGroupService contactGroupService;
    ContactSocialMediaService contactSocialMediaService;
    EmailAddressService emailAddressService;
    GroupService groupService;
    PhoneService phoneService;
    SocialMediaService socialMediaService;
    VersionHistoryService versionHistoryService;
    UserService userService;


    @Autowired
    public ContactServiceImpl(ContactRepository contactRepository,
                              AddressService addressService,
                              ContactGroupService contactGroupService,
                              ContactSocialMediaService contactSocialMediaService,
                              EmailAddressService emailAddressService,
                              GroupService groupService,
                              PhoneService phoneService,
                              SocialMediaService socialMediaService,
                              VersionHistoryService versionHistoryService,
                              UserService userService) {

        super(contactRepository);
        this.contactRepository = contactRepository;
        this.addressService = addressService;
        this.contactGroupService = contactGroupService;
        this.contactSocialMediaService = contactSocialMediaService;
        this.emailAddressService = emailAddressService;
        this.groupService = groupService;
        this.phoneService = phoneService;
        this.socialMediaService = socialMediaService;
        this.versionHistoryService = versionHistoryService;
        this.userService = userService;
    }


    @Override
    public ContactDTO saveContact(ContactDTO contactDTO) throws NotFoundException {
        Contact contact = getContactFromDTO(contactDTO);
        Contact newContact;
        if (contactDTO.getId() != null)
            newContact = update(contactDTO.getId(), contact);
        else
            newContact = save(contact);
        List<Address> addressList = contactDTO.getContactAdressList() != null ? contactDTO.getContactAdressList() : new ArrayList<>();
        if (!addressList.isEmpty()) {
            addressList.stream().forEach((address) -> address.setContact(newContact));
            addressService.save(addressList);
            contactDTO.setContactAdressList(addressList);
        }
        List<Phone> phoneList = contactDTO.getPhones() != null ? contactDTO.getPhones() : new ArrayList<>();
        if (!phoneList.isEmpty()) {
            phoneList.stream().forEach((phone) -> phone.setContact(newContact));
            phoneService.save(phoneList);
            contactDTO.setPhones(phoneList);
        }
        List<EmailAddress> emailAddressList = contactDTO.getEmailAddresses() != null ? contactDTO.getEmailAddresses() : new ArrayList<>();
        if (!emailAddressList.isEmpty()) {
            emailAddressList.stream().forEach((emailAddress -> emailAddress.setContact(newContact)));
            emailAddressService.save(emailAddressList);
            contactDTO.setEmailAddresses(emailAddressList);
        }
        List<ContactGroup> contactGroupList = contactDTO.getContactGroups() != null ? contactDTO.getContactGroups() : new ArrayList<>();
        if (!contactGroupList.isEmpty()) {
            contactGroupList.stream().forEach(contactGroup -> contactGroup.setContact(newContact));
            contactGroupService.save(contactGroupList);
            contactDTO.setContactGroups(contactGroupList);
        }
        List<ContactSocialMedia> contactSocialMediaList = contactDTO.getSocialMediaList() != null ? contactDTO.getSocialMediaList() : new ArrayList<>();
        if (!contactSocialMediaList.isEmpty()) {
            contactSocialMediaList.stream().forEach(contactSocialMedia -> contactSocialMedia.setContact(newContact));
            contactSocialMediaService.save(contactSocialMediaList);
            contactDTO.setSocialMediaList(contactSocialMediaList);
        }
        F.Promise.promise(() -> versionHistoryService.newVersion(newContact, addressList, contactGroupList, contactSocialMediaList, phoneList, emailAddressList, false));
        return contactDTO;
    }

    @Override
    public Contact updateContact(Long id, Contact contact) throws NotFoundException {
        Contact contactToUpdate = update(id, contact);
        List<Address> addressList = addressService.findByContactId(id);
        List<ContactGroup> contactGroupList = contactGroupService.findByContactId(id);
        List<ContactSocialMedia> contactSocialMediaList = contactSocialMediaService.findByContactId(id);
        List<Phone> phoneList = phoneService.findByContactId(id);
        List<EmailAddress> emailAddressList = emailAddressService.findByContactId(id);
        F.Promise.promise(() -> versionHistoryService.newVersion(contactToUpdate, addressList, contactGroupList, contactSocialMediaList, phoneList, emailAddressList, false));
        return contactToUpdate;
    }

    @Override
    public boolean deleteContact(Long id) throws NotFoundException {
        Contact contact = contactRepository.findOne(id);
        if (contact == null)
            return false;
        List<Address> addressList = addressService.findByContactId(contact.getId());
        List<ContactGroup> contactGroupList = contactGroupService.findByContactId(contact.getId());
        List<ContactSocialMedia> contactSocialMediaList = contactSocialMediaService.findByContactId(contact.getId());
        List<Phone> phoneList = phoneService.findByContactId(contact.getId());
        List<EmailAddress> emailAddressList = emailAddressService.findByContactId(contact.getId());
        contact.setDeleted(true);
        save(contact);
        F.Promise.promise(() -> versionHistoryService.newVersion(contact, addressList, contactGroupList, contactSocialMediaList, phoneList, emailAddressList, true));
        return true;
    }

    @Override
    public void addToFavorite(long contactId) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        if (contact == null)
            throw new NotFoundException("contact not found while trying to add to favorite with id: " + contactId);
        contact.setFavorite(true);
        save(contact);
    }

    @Override
    public ContactDTO getContact(long contactId) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        if (contact == null)
            throw new NotFoundException("Contact not found with id: " + contactId);
        long newHit = contact.getHit() + 1;
        contact.setHit(newHit);
        ContactDTO contactDTO = getDtoFromContact(contact);
        contactRepository.save(contact);
        return contactDTO;
    }

    @Override
    public List<ContactDTO> getAllContacts(long userId) throws NotFoundException {
        List<Contact> contacts = contactRepository.findContactByUserId(userId);
        if (contacts == null || contacts.isEmpty())
            throw new NotFoundException("Can't find contacts for specified userId: " + userId);
        List<ContactDTO> contactDTOList = new ArrayList<>();
        for (Contact contact : contacts) {
            contactDTOList.add(getContact(contact.getId()));
        }
        return contactDTOList;
    }

    @Override
    public ContactDTO mergeContact(long originalId, long updateId) throws NotFoundException {
        Contact original = contactRepository.findOne(originalId);
        Contact update = contactRepository.findOne(updateId);
        if (original == null || update == null)
            throw new NotFoundException("Unable to merge original contact id: " + originalId + " with contact id: " + updateId);
        Contact newContact = mergeContactBasicInfo(original, update);
        ContactDTO updateContactDTO = getDtoFromContact(update);
        ContactDTO originalContactDTO = getDtoFromContact(original);
        List<Phone> phoneList = phoneService.mergeContactPhones(original, updateContactDTO.getPhones());
        List<ContactGroup> contactGroupList = contactGroupService.mergeContactGroup(original, updateContactDTO.getContactGroups());
        List<EmailAddress> emailAddressList = emailAddressService.mergeContactEmailAddress(original, updateContactDTO.getEmailAddresses());
        List<ContactSocialMedia> contactSocialMediaList = contactSocialMediaService.mergeContactSocialMedia(original, updateContactDTO.getSocialMediaList());
        List<Address> addressList = addressService.mergeContactAddress(original, updateContactDTO.getContactAdressList());
        Contact mergedContact = contactRepository.save(newContact);
        F.Promise.promise(() -> versionHistoryService.newVersion(mergedContact, addressList, contactGroupList, contactSocialMediaList, phoneList, emailAddressList, false));
        originalContactDTO.setContactAdressList(addressList);
        originalContactDTO.setContactGroups(contactGroupList);
        originalContactDTO.setEmailAddresses(emailAddressList);
        originalContactDTO.setPhones(phoneList);
        originalContactDTO.setSocialMediaList(contactSocialMediaList);
        deleteContact(updateId);
        return originalContactDTO;
    }

    @Override
    public List<ContactDTO> getFavorites(long userId) throws NotFoundException {
        List<Contact> contacts = contactRepository.findFavoriteContacts(userId);
        if (contacts == null)
            throw new NotFoundException("No contacts found for user id: " + userId);
        List<ContactDTO> contactDTOList = new ArrayList<>();
        for (Contact contact : contacts) {
            contactDTOList.add(getContact(contact.getId()));
        }
        return contactDTOList;
    }

    @Override
    public List<ContactDTO> getLastTenSearches(long userId) throws NotFoundException {
        List<Contact> contacts = contactRepository.findLastTenSearchedContacts(userId);
        if (contacts == null)
            throw new NotFoundException("Trying to get top 10 searches, No contacts found for user id: " + userId);
        List<ContactDTO> contactDTOList = new ArrayList<>();
        for (Contact contact : contacts) {
            contactDTOList.add(getContact(contact.getId()));
        }
        return contactDTOList;
    }

    @Override
    public List<ContactDTO> getMostSearchedContacts(long userId) throws NotFoundException {
        List<Contact> contacts = contactRepository.findMostSearchedContacts(userId);
        if (contacts == null)
            throw new NotFoundException("Fetching Most searched contacts, No contacts found for user id: " + userId);
        List<ContactDTO> contactDTOList = new ArrayList<>();
        for (Contact contact : contacts) {
            contactDTOList.add(getContact(contact.getId()));
        }
        return contactDTOList;
    }

    @Override
    public ContactDTO revertToPreviousVersion(long contactId) throws NotFoundException {
        Optional<VersionHistory> _versionHistory = versionHistoryService.getPreviousVersion(contactId);
        if (!_versionHistory.isPresent())
            throw new NotFoundException("No Version history found for contact id: " + contactId);
        clearContact(contactId);
        ContactDTO contactDTO = versionHistoryService.getContact(_versionHistory.get().getId());
        contactDTO = saveContact(contactDTO);
        return contactDTO;
    }

    @Override
    public ContactDTO revertToVersion(long versionId) throws NotFoundException {
        VersionHistory _versionHistory = versionHistoryService.findById(versionId);
        if (_versionHistory == null)
            throw new NotFoundException("No Version history found for version id: " + versionId);
        Contact contact = contactRepository.findOne(_versionHistory.getContactId());
        Long contactId = contact.getId();
        clearContact(contactId);
        ContactDTO contactDTO = versionHistoryService.getContact(_versionHistory.getId());
        if (contactDTO.isDeleted())
            contactDTO.setDeleted(false);
        contactDTO = saveContact(contactDTO);
        return contactDTO;
    }

    @Override
    public List<ContactDTO> getContactHistory(long contactId) throws NotFoundException {
        return versionHistoryService.getContactHistory(contactId);
    }

    private void clearContact(long id) throws NotFoundException {
        addressService.delete(addressService.findByContactId(id));
        phoneService.delete(phoneService.findByContactId(id));
        emailAddressService.delete(emailAddressService.findByContactId(id));
        contactGroupService.delete(contactGroupService.findByContactId(id));
        contactSocialMediaService.delete(contactSocialMediaService.findByContactId(id));
    }

    private Contact mergeContactBasicInfo(Contact original, Contact update) {
        if (update.getFirstName() != null)
            original.setFirstName(update.getFirstName());
        if (update.getLastName() != null)
            original.setLastName(update.getLastName());
        if (update.getMiddleName() != null)
            original.setMiddleName(update.getMiddleName());
        if (update.getPrefix() != null)
            original.setPrefix(update.getPrefix());
        if (update.getNickname() != null)
            original.setNickname(update.getNickname());
        if (update.getSufix() != null)
            original.setSufix(update.getSufix());
        if (update.getFavorite() != null && !update.getFavorite())
            original.setFavorite(update.getFavorite());
        return original;
    }

    private Contact getContactFromDTO(ContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setFirstName(contactDTO.getFirstName());
        contact.setLastName(contactDTO.getLastName());
        contact.setMiddleName(contactDTO.getMiddleName());
        contact.setNickname(contactDTO.getNickname());
        contact.setFavorite(contactDTO.getFavorite());
        contact.setPrefix(contactDTO.getPrefix());
        contact.setSufix(contactDTO.getSufix());
        contact.setId(contactDTO.getId());
        User user = userService.findById(contactDTO.getUserId());
        contact.setUser(user);
        contact.setDeleted(contactDTO.isDeleted());
        return contact;

    }

    private ContactDTO getDtoFromContact(Contact contact) {
        Long contactId = contact.getId();
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());
        contactDTO.setMiddleName(contact.getMiddleName());
        contactDTO.setNickname(contact.getNickname());
        contactDTO.setFavorite(contact.getFavorite());
        contactDTO.setPrefix(contact.getPrefix());
        contactDTO.setSufix(contact.getSufix());
        contactDTO.setId(contact.getId());
        contactDTO.setUserId(contact.getUser().getId());
        contactDTO.setDeleted(contact.isDeleted());

        contactDTO.setContactAdressList(addressService.findByContactId(contactId));
        contactDTO.setContactGroups(contactGroupService.findByContactId(contactId));
        contactDTO.setEmailAddresses(emailAddressService.findByContactId(contactId));
        contactDTO.setPhones(phoneService.findByContactId(contactId));
        contactDTO.setContactAdressList(addressService.findByContactId(contactId));

        return contactDTO;
    }
}
