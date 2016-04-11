package services.history;


import models.db.contacts.*;
import models.db.historty.VersionHistory;
import models.dtos.ContactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
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
    public VersionHistory newVersion(Contact contact, List<Address> addressList, List<ContactGroup> contactGroups, List<ContactSocialMedia> contactSocialMediaList, List<Phone> phoneList, List<EmailAddress> emailAddressList, boolean markAsDeleted) {

        Optional<UUID> contactVersion = generateContactVersion(contact, markAsDeleted);
        Optional<UUID> addressVersion = !addressList.isEmpty() ? generateAddressVersion(addressList, false) : Optional.empty();
        Optional<UUID> contactGroupVersion = !contactGroups.isEmpty() ? generateContactGroupVersion(contactGroups, contactVersion.get(), false) : Optional.empty();
        Optional<UUID> contactSocialMediaVersion = !contactSocialMediaList.isEmpty() ? generateContactSocialMediaVersion(contactSocialMediaList, contactVersion.get(), false) : Optional.empty();
        Optional<UUID> contactPhoneVersion = !phoneList.isEmpty() ? generateContactPhoneVersion(phoneList, false) : Optional.empty();
        Optional<UUID> emailAddressVersion = !emailAddressList.isEmpty() ? generateContactEmailVersion(emailAddressList, false) : Optional.empty();

        VersionHistory versionHistory = new VersionHistory();
        versionHistory.setContactId(contact.getId());
        versionHistory.setContactVersion(contactVersion.get());

        if (addressVersion.isPresent())
            versionHistory.setAddressVersion(addressVersion.get());
        if (contactGroupVersion.isPresent())
            versionHistory.setContactGroupVersion(contactGroupVersion.get());
        if (contactSocialMediaVersion.isPresent())
            versionHistory.setContactSocialMediaVersion(contactSocialMediaVersion.get());
        if (contactPhoneVersion.isPresent())
            versionHistory.setPhoneVersion(contactPhoneVersion.get());
        if (emailAddressVersion.isPresent())
            versionHistory.setEmailAddressVersion(emailAddressVersion.get());

        save(versionHistory);

        return versionHistory;
    }

    @Override
    public VersionHistory newAddressVersion(Contact contact, List<Address> addresses, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getCurrentVersion(contact.getId());
        VersionHistory versionHistory = previous.get();
        VersionHistory newVersionHistory = new VersionHistory();

        Optional<UUID> addressVersion = generateAddressVersion(addresses, markAsDeleted);

        newVersionHistory.setAddressVersion(addressVersion.get());
        newVersionHistory.setContactGroupVersion(versionHistory.getContactGroupVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());
        newVersionHistory.setContactSocialMediaVersion(versionHistory.getContactSocialMediaVersion());
        newVersionHistory.setContactVersion(versionHistory.getContactVersion());
        newVersionHistory.setEmailAddressVersion(versionHistory.getEmailAddressVersion());
        newVersionHistory.setPhoneVersion(versionHistory.getPhoneVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());

        save(newVersionHistory);
        return newVersionHistory;
    }

    @Override
    public VersionHistory newPhoneVersion(Contact contact, List<Phone> phones, boolean markAsDeleted) {

        Optional<VersionHistory> previous = getCurrentVersion(contact.getId());

        VersionHistory versionHistory = previous.get();

        Optional<UUID> phoneVersion = generateContactPhoneVersion(phones, markAsDeleted);

        VersionHistory newVersionHistory = new VersionHistory();

        newVersionHistory.setPhoneVersion(phoneVersion.get());
        newVersionHistory.setAddressVersion(versionHistory.getAddressVersion());
        newVersionHistory.setContactGroupVersion(versionHistory.getContactGroupVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());
        newVersionHistory.setContactSocialMediaVersion(versionHistory.getContactSocialMediaVersion());
        newVersionHistory.setContactVersion(versionHistory.getContactVersion());
        newVersionHistory.setEmailAddressVersion(versionHistory.getEmailAddressVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());

        save(newVersionHistory);
        return newVersionHistory;
    }

    @Override
    public VersionHistory newEmailAddressVersion(Contact contact, List<EmailAddress> emailAddresses, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getCurrentVersion(contact.getId());
        VersionHistory versionHistory = previous.get();

        Optional<UUID> emailVersion = generateContactEmailVersion(emailAddresses, markAsDeleted);

        VersionHistory newVersionHistory = new VersionHistory();
        newVersionHistory.setPhoneVersion(versionHistory.getPhoneVersion());
        newVersionHistory.setAddressVersion(versionHistory.getAddressVersion());
        newVersionHistory.setContactGroupVersion(versionHistory.getContactGroupVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());
        newVersionHistory.setContactSocialMediaVersion(versionHistory.getContactSocialMediaVersion());
        newVersionHistory.setContactVersion(versionHistory.getContactVersion());
        newVersionHistory.setEmailAddressVersion(emailVersion.get());
        newVersionHistory.setContactId(versionHistory.getContactId());
        save(newVersionHistory);

        return newVersionHistory;
    }


    @Override
    public VersionHistory newContactGroupVersion(Contact contact, List<ContactGroup> contactGroups, boolean markAsDeleted) {
        Optional<VersionHistory> previous = getCurrentVersion(contact.getId());
        VersionHistory versionHistory = previous.get();

        Optional<UUID> contactGroupVersion = generateContactGroupVersion(contactGroups, versionHistory.getContactVersion(), markAsDeleted);

        VersionHistory newVersionHistory = new VersionHistory();
        newVersionHistory.setPhoneVersion(versionHistory.getPhoneVersion());
        newVersionHistory.setAddressVersion(versionHistory.getAddressVersion());
        newVersionHistory.setContactGroupVersion(contactGroupVersion.get());
        newVersionHistory.setContactId(versionHistory.getContactId());
        newVersionHistory.setContactSocialMediaVersion(versionHistory.getContactSocialMediaVersion());
        newVersionHistory.setContactVersion(versionHistory.getContactVersion());
        newVersionHistory.setEmailAddressVersion(versionHistory.getEmailAddressVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());
        save(newVersionHistory);

        return newVersionHistory;
    }


    @Override
    public VersionHistory newContactSocialMediaVersion(Contact contact, List<ContactSocialMedia> contactSocialMedias, boolean markAsDeleted) {

        Optional<VersionHistory> previous = getCurrentVersion(contact.getId());
        VersionHistory versionHistory = previous.get();

        Optional<UUID> contactSocialMediaVersion = generateContactSocialMediaVersion(contactSocialMedias, versionHistory.getContactVersion(), markAsDeleted);

        VersionHistory newVersionHistory = new VersionHistory();
        newVersionHistory.setPhoneVersion(versionHistory.getPhoneVersion());
        newVersionHistory.setAddressVersion(versionHistory.getAddressVersion());
        newVersionHistory.setContactGroupVersion(versionHistory.getContactGroupVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());
        newVersionHistory.setContactSocialMediaVersion(contactSocialMediaVersion.get());
        newVersionHistory.setContactVersion(versionHistory.getContactVersion());
        newVersionHistory.setEmailAddressVersion(versionHistory.getEmailAddressVersion());
        newVersionHistory.setContactId(versionHistory.getContactId());
        save(newVersionHistory);

        return newVersionHistory;
    }

    @Override
    public Optional<VersionHistory> getCurrentVersion(Long contactId) {
        return versionHistoryRepository.findVersionHistory(contactId, new PageRequest(0, 1, Sort.Direction.DESC, "creationDate")).getContent().stream().findFirst();
    }

    @Override
    public Optional<VersionHistory> getPreviousVersion(Long contactId){
        return versionHistoryRepository.findVersionHistory(contactId, new PageRequest(0, 2, Sort.Direction.DESC, "creationDate")).getContent().stream().skip(1).findFirst();
    }

    @Override
    public ContactDTO getContact(long versionHistoryId) throws NotFoundException {

        ContactDTO contactDTO = new ContactDTO();
        VersionHistory versionHistory = versionHistoryRepository.findOne(versionHistoryId);

        Contact contact = contactHistoryService.getContact(versionHistory.getContactVersion());
        List<ContactGroup> contactGroup = contactGroupHistoryService.getContactGroup(versionHistory.getContactGroupVersion());
        List<Address> addressList = addressHistoryService.getAddress(versionHistory.getAddressVersion());
        List<ContactSocialMedia> contactSocialMediaList = contactSocialMediaHistoryService.getContactSocialMedia(versionHistory.getContactSocialMediaVersion());
        List<EmailAddress> emailAddressList = emailAddressHistoryService.getEmailAddress(versionHistory.getEmailAddressVersion());
        List<Phone> phoneList = phoneHistoryService.getContactPhone(versionHistory.getPhoneVersion());

        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());
        contactDTO.setMiddleName(contact.getMiddleName());
        contactDTO.setNickname(contact.getNickname());
        contactDTO.setFavorite(contact.getFavorite());

        contactDTO.setContactGroups(contactGroup);
        contactDTO.setContactAdressList(addressList);
        contactDTO.setSocialMediaList(contactSocialMediaList);
        contactDTO.setEmailAddresses(emailAddressList);
        contactDTO.setPhones(phoneList);

        contactDTO.setId(contact.getId());

        contactDTO.setVersionHistoryId(versionHistoryId);

        return contactDTO;
    }

    @Override
    public List<ContactDTO> getContactHistory(Long contactId) throws NotFoundException {
        List<ContactDTO> contacts = new ArrayList<>();

            List<VersionHistory> contactHistoryList = versionHistoryRepository.findVersionHistory(contactId);
            for (VersionHistory contactHistory : contactHistoryList) {
                contacts.add(getContact(contactHistory.getId()));
            }

        return contacts;
    }


    private Optional<UUID> generateContactVersion(Contact contact, boolean markAsDeleted) {
        return Optional.of(contactHistoryService.newVersion(contact, UUID.randomUUID(), markAsDeleted));
    }

    private Optional<UUID> generateAddressVersion(List<Address> addressList, boolean markAsDeleted) {

        if (addressList.isEmpty())
            return Optional.empty();

        UUID addressVersion = UUID.randomUUID();
        for (Address address : addressList) {
            addressHistoryService.newVersion(address, addressVersion, markAsDeleted);
        }
        return Optional.of(addressVersion);
    }

    private Optional<UUID> generateContactGroupVersion(List<ContactGroup> contactGroupsList, UUID contactId, boolean markAsDeleted) {

        if (contactGroupsList.isEmpty())
            return Optional.empty();

        UUID contactGroupHistoryVersion = UUID.randomUUID();
        for (ContactGroup contactGroup : contactGroupsList) {
            contactGroupHistoryService.newVersion(contactGroup, contactGroup.getCGroup().getId(), contactId, contactGroupHistoryVersion, markAsDeleted);
        }
        return Optional.of(contactGroupHistoryVersion);
    }

    private Optional<UUID> generateContactSocialMediaVersion(List<ContactSocialMedia> contactSocialMediaList, UUID contactVersion, boolean markAsDeleted) {

        if (contactSocialMediaList.isEmpty())
            return Optional.empty();

        UUID contactSocialMediaHistoryVersion = UUID.randomUUID();
        for (ContactSocialMedia contactSocialMedia : contactSocialMediaList) {
            contactSocialMediaHistoryVersion = contactSocialMediaHistoryService.newVersion(contactSocialMedia, contactVersion, contactSocialMedia.getSocialMedia().getId(), contactSocialMediaHistoryVersion, markAsDeleted);
        }
        return Optional.of(contactSocialMediaHistoryVersion);
    }

    private Optional<UUID> generateContactPhoneVersion(List<Phone> phoneList, boolean markAsDeleted) {

        if (phoneList.isEmpty())
            return Optional.empty();

        UUID phoneVersion = UUID.randomUUID();
        for (Phone phone : phoneList) {
            phoneHistoryService.newVersion(phone, phoneVersion, markAsDeleted);
        }
        return Optional.of(phoneVersion);
    }

    private Optional<UUID> generateContactEmailVersion(List<EmailAddress> emailAddressList, boolean markAsDeleted) {

        if (emailAddressList.isEmpty())
            return Optional.empty();

        UUID emailAddressVersion = UUID.randomUUID();
        for (EmailAddress emailAddress : emailAddressList) {
            emailAddressHistoryService.newVersion(emailAddress, emailAddressVersion, markAsDeleted);
        }
        return Optional.of(emailAddressVersion);
    }

}
