package services.base.interfaces.history;

import models.db.contacts.Address;
import models.db.contacts.Contact;
import models.db.contacts.EmailAddress;
import models.db.historty.AddressHistory;
import models.db.historty.ContactSocialMediaHistory;
import models.db.historty.EmailAddressHistory;
import services.base.GenericService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface EmailAddressHistoryService extends GenericService<EmailAddressHistory, Long>{

    public UUID newVersion(EmailAddress address, UUID version, boolean markAsDeleted);
    public Optional<EmailAddressHistory> getPreviousVersion(Long emailAddressId);

    List<EmailAddress> getEmailAddress(UUID emailAddressVersion);
}
