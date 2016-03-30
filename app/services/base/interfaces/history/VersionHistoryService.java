package services.base.interfaces.history;

import models.db.contacts.*;
import models.db.historty.VersionHistory;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface VersionHistoryService extends GenericService<VersionHistory, Long>{

    public VersionHistory newVersion(Contact contact, boolean markAsDeleted);
    public VersionHistory newVersion(Contact contact, Address address, boolean markAsDeleted);
    public VersionHistory newVersion(Contact contact, Phone phone, boolean markAsDeleted);
    public VersionHistory newVersion(Contact contact, EmailAddress emailAddress, boolean markAsDeleted);
    public VersionHistory newVersion(Contact contact, ContactGroup contactGroup, boolean markAsDeleted);
    public VersionHistory newVersion(Contact contact, ContactSocialMedia contactSocialMedia, boolean markAsDeleted);
    public Optional<VersionHistory> getPreviousVersion(Long contactId);
    public Contact getContact(VersionHistory versionHistory) throws NotFoundException;
    List<Contact> getContactHistory(Long contactId);


}
