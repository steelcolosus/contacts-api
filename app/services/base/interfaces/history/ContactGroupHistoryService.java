package services.base.interfaces.history;

import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import models.db.historty.AddressHistory;
import models.db.historty.ContactGroupHistory;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface ContactGroupHistoryService extends GenericService<ContactGroupHistory, Long>{

    public UUID newVersion(ContactGroup contactGroup, Long groupId, UUID contactHistoryVersion, UUID version, boolean markAsDeleted);
    public Optional<ContactGroupHistory> getPreviousVersion(Long contactGroupId);
    public List<ContactGroup> getContactGroup(UUID contactGroupVersion) throws NotFoundException;


}
