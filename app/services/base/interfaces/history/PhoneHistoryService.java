package services.base.interfaces.history;

import models.db.contacts.Contact;
import models.db.contacts.Phone;
import models.db.historty.PhoneHistory;
import services.base.GenericService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface PhoneHistoryService extends GenericService<PhoneHistory, Long>{

    public UUID newVersion(Phone address, UUID version, boolean markAsDeleted);
    public Optional<PhoneHistory> getPreviousVersion(Long phoneId);

    List<Phone> getContactPhone(UUID contactPhoneVersion);
}
