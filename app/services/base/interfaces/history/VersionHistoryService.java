package services.base.interfaces.history;

import models.db.contacts.*;
import models.db.historty.VersionHistory;
import models.dtos.ContactDTO;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface VersionHistoryService extends GenericService<VersionHistory, Long>{

    VersionHistory newVersion(Contact contact, List<Address> addressList, List<ContactGroup> contactGroups, List<ContactSocialMedia> ContactSocialMediaList, List<Phone> phoneList,List<EmailAddress> emailAddressList, boolean markAsDeleted);
    VersionHistory newAddressVersion(Contact contact, List<Address> address, boolean markAsDeleted);
    VersionHistory newPhoneVersion(Contact contact, List<Phone> phone, boolean markAsDeleted);
    VersionHistory newEmailAddressVersion(Contact contact, List<EmailAddress> emailAddress, boolean markAsDeleted);
    VersionHistory newContactGroupVersion(Contact contact, List<ContactGroup> contactGroup, boolean markAsDeleted);
    VersionHistory newContactSocialMediaVersion(Contact contact, List<ContactSocialMedia> contactSocialMedia, boolean markAsDeleted);
    Optional<VersionHistory> getCurrentVersion(Long contactId);

    Optional<VersionHistory> getPreviousVersion(Long contactId);

    ContactDTO getContact(long versionHistoryId) throws NotFoundException;
    List<ContactDTO> getContactHistory(Long contactId) throws NotFoundException;


}
