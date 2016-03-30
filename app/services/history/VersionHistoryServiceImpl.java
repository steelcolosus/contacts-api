package services.history;


import models.db.contacts.*;
import models.db.historty.ContactHistory;
import models.db.historty.VersionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import repositories.history.ContactHistoryRepository;
import repositories.history.VersionHistoryRepository;
import services.base.AbstractService;
import services.base.interfaces.history.*;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class VersionHistoryServiceImpl extends AbstractService<VersionHistory, Long> implements VersionHistoryService {

    ContactHistoryService contactHistoryService;
    VersionHistoryRepository versionHistoryRepository;
    AddressHistoryService addressHistoryService;
    ContactGroupHistoryService contactGroupHistoryService;
    ContactSocialMediaHistoryService contactSocialMediaHistoryService;
    PhoneHistoryService phoneHistoryService;
    EmailAddressHistoryService emailAddressHistoryService;

    @Autowired
    public VersionHistoryServiceImpl(ContactHistoryService contactHistoryService,
                                     AddressHistoryService addressHistoryService,
                                     VersionHistoryRepository versionHistoryRepository,
                                     ContactGroupHistoryService contactGroupHistoryService,
                                     ContactSocialMediaHistoryService contactSocialMediaHistoryService,
                                     PhoneHistoryService phoneHistoryService,
                                     EmailAddressHistoryService emailAddressHistoryService) {
        super(versionHistoryRepository);
        this.contactHistoryService = contactHistoryService;
        this.addressHistoryService = addressHistoryService;
        this.versionHistoryRepository = versionHistoryRepository;
        this.contactGroupHistoryService = contactGroupHistoryService;
        this.contactSocialMediaHistoryService = contactSocialMediaHistoryService;
        this.phoneHistoryService = phoneHistoryService;
        this.emailAddressHistoryService = emailAddressHistoryService;
    }

    @Override
    public VersionHistory newVersion(Contact contact, boolean markAsDeleted) {

        UUID contactVersion = generateContactVersion(contact, markAsDeleted);
        UUID addressVersion = generateAddressVersion(contact.getContactAdressList(), false);
        UUID contactGroupVersion = generateContactGroupVersion(contact.getContactGroups(), contactVersion, false);
        UUID contactSocialMediaVersion = generateContactSocialMediaVersion(contact.getSocialMediaList(), contactVersion, false);
        UUID contactPhoneVersion = generateContactPhoneVersion(contact.getPhones(), false);
        UUID emailAddressVersion = generateContactEmailVersion(contact.getEmailAddresses(), false);

        VersionHistory versionHistory = new VersionHistory();
        versionHistory.setAddressVersion(addressVersion);
        versionHistory.setContactGroupVersion(contactGroupVersion);
        versionHistory.setContactSocialMediaVersion(contactSocialMediaVersion);
        versionHistory.setEmailAddressVersion(contactPhoneVersion);
        versionHistory.setPhoneVersion(emailAddressVersion);

        save(versionHistory);

        return versionHistory;
    }

    @Override
    public VersionHistory newVersion(Contact contact, Address address, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getPreviousVersion(contact.getId());
        VersionHistory versionHistory = previous.get();
        List<Address> addresses = contact.getContactAdressList();
        addresses.add(address);
        UUID addressVersion = generateAddressVersion(addresses, markAsDeleted);
        versionHistory.setAddressVersion(addressVersion);
        save(versionHistory);
        return versionHistory;
    }

    @Override
    public VersionHistory newVersion(Contact contact, Phone phone, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getPreviousVersion(contact.getId());
        VersionHistory versionHistory = previous.get();
        List<Phone> phones = contact.getPhones();
        phones.add(phone);
        UUID phoneVersion = generateContactPhoneVersion(phones, markAsDeleted);
        versionHistory.setPhoneVersion(phoneVersion);
        save(versionHistory);
        return versionHistory;
    }

    @Override
    public VersionHistory newVersion(Contact contact, EmailAddress emailAddress, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getPreviousVersion(contact.getId());
        VersionHistory versionHistory = previous.get();
        List<EmailAddress> emailAddresses = contact.getEmailAddresses();
        emailAddresses.add(emailAddress);
        UUID phoneVersion = generateContactEmailVersion(emailAddresses, markAsDeleted);
        versionHistory.setEmailAddressVersion(phoneVersion);
        save(versionHistory);
        return versionHistory;
    }


    @Override
    public VersionHistory newVersion(Contact contact, ContactGroup contactGroup, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getPreviousVersion(contact.getId());
        VersionHistory versionHistory = previous.get();
        List<ContactGroup> contactGroups = contact.getContactGroups();
        contactGroups.add(contactGroup);
        UUID contactGroupVersion = generateContactGroupVersion(contactGroups, versionHistory.getContactVersion(), markAsDeleted);
        versionHistory.setContactGroupVersion(contactGroupVersion);
        save(versionHistory);
        return versionHistory;
    }


    @Override
    public VersionHistory newVersion(Contact contact, ContactSocialMedia contactSocialMedia, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getPreviousVersion(contact.getId());
        VersionHistory versionHistory = previous.get();
        List<ContactSocialMedia> contactSocialMedias = contact.getSocialMediaList();
        contactSocialMedias.add(contactSocialMedia);
        UUID contactSocialMediaVersion = generateContactSocialMediaVersion(contactSocialMedias, versionHistory.getContactVersion(), markAsDeleted);
        versionHistory.setContactSocialMediaVersion(contactSocialMediaVersion);
        save(versionHistory);
        return versionHistory;
    }

    @Override
    public Optional<VersionHistory> getPreviousVersion(Long contactId) {
        return versionHistoryRepository.findVersionHistory(contactId, new PageRequest(0, 1, Sort.Direction.ASC, "creationDate")).getContent().stream().findFirst();
    }

    @Override
    public Contact getContact(VersionHistory versionHistory) throws NotFoundException {

        Contact contact = contactHistoryService.getContact(versionHistory.getContactVersion());
        List<ContactGroup> contactGroup = contactGroupHistoryService.getContactGroup(versionHistory.getContactGroupVersion());
        List<Address> addressList = addressHistoryService.getAddress(versionHistory.getAddressVersion());
        List<ContactSocialMedia> contactSocialMediaList = contactSocialMediaHistoryService.getContactSocialMedia(versionHistory.getContactSocialMediaVersion());
        List<EmailAddress> emailAddressList = emailAddressHistoryService.getEmailAddress(versionHistory.getEmailAddressVersion());
        List<Phone> phoneList = phoneHistoryService.getContactPhone(versionHistory.getPhoneVersion());

        contact.setContactGroups(contactGroup);
        contact.setContactAdressList(addressList);
        contact.setSocialMediaList(contactSocialMediaList);
        contact.setEmailAddresses(emailAddressList);
        contact.setPhones(phoneList);

        return contact;
    }

    @Override
    public List<Contact> getContactHistory(Long contactId) {
        List<Contact> contacts = new ArrayList<>();
        try {
            List<VersionHistory> contactHistoryList = versionHistoryRepository.findVersionHistory(contactId);
            for (VersionHistory contactHistory : contactHistoryList) {
                contacts.add(getContact(contactHistory));
            }
        } catch (NotFoundException e) {
            return Collections.emptyList();
        }
        return contacts;
    }

    private UUID generateContactVersion(Contact contact, boolean markAsDeleted) {
        return contactHistoryService.newVersion(contact, UUID.randomUUID(), markAsDeleted);
    }

    private UUID generateAddressVersion(List<Address> addressList, boolean markAsDeleted) {
        UUID addressVersion = UUID.randomUUID();
        for (Address address : addressList) {
            addressHistoryService.newVersion(address, addressVersion, markAsDeleted);
        }
        return addressVersion;
    }

    private UUID generateContactGroupVersion(List<ContactGroup> contactGroupsList, UUID contactId, boolean markAsDeleted) {
        UUID contactGroupHistoryVersion = UUID.randomUUID();
        for (ContactGroup contactGroup : contactGroupsList) {
            contactGroupHistoryService.newVersion(contactGroup, contactGroup.getGroup().getId(), contactId, contactGroupHistoryVersion, markAsDeleted);
        }
        return contactGroupHistoryVersion;
    }

    private UUID generateContactSocialMediaVersion(List<ContactSocialMedia> contactSocialMediaList, UUID contactVersion, boolean markAsDeleted) {
        UUID contactSocialMediaHistoryVersion = UUID.randomUUID();
        for (ContactSocialMedia contactSocialMedia : contactSocialMediaList) {
            contactSocialMediaHistoryVersion = contactSocialMediaHistoryService.newVersion(contactSocialMedia, contactVersion, contactSocialMedia.getSocialMedia().getId(), contactSocialMediaHistoryVersion, markAsDeleted);
        }
        return contactSocialMediaHistoryVersion;
    }

    private UUID generateContactPhoneVersion(List<Phone> phoneList, boolean markAsDeleted) {
        UUID phoneVersion = UUID.randomUUID();
        for (Phone phone : phoneList) {
            phoneHistoryService.newVersion(phone, phoneVersion, markAsDeleted);
        }
        return phoneVersion;
    }

    private UUID generateContactEmailVersion(List<EmailAddress> emailAddressList, boolean markAsDeleted) {
        UUID emailAddressVersion = UUID.randomUUID();
        for (EmailAddress emailAddress : emailAddressList) {
            emailAddressHistoryService.newVersion(emailAddress, emailAddressVersion, markAsDeleted);
        }
        return emailAddressVersion;
    }

}
