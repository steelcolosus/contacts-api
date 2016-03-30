package services.base.interfaces.history;

import models.db.contacts.*;
import models.db.historty.ContactHistory;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface ContactHistoryService extends GenericService<ContactHistory, Long>{

    public UUID newVersion(Contact contact, UUID version, boolean markAsDeleted);

    public Contact getContact(UUID versionId) throws NotFoundException;
    public Optional<ContactHistory> getPreviousVersion(Long contactId);
    public List<ContactHistory> getContactHistory(long userId) throws NotFoundException;


}
