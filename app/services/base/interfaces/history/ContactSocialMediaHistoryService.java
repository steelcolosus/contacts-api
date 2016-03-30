package services.base.interfaces.history;

import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import models.db.contacts.ContactSocialMedia;
import models.db.contacts.SocialMedia;
import models.db.historty.AddressHistory;
import models.db.historty.ContactGroupHistory;
import models.db.historty.ContactSocialMediaHistory;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 19/03/16.
 */

public interface ContactSocialMediaHistoryService extends GenericService<ContactSocialMediaHistory, Long>{

    UUID newVersion(ContactSocialMedia contactSocialMedia, UUID contactVersion, Long socialMediaId, UUID version, boolean markAsDeleted);
    Optional<ContactSocialMediaHistory> getPreviousVersion(Long contactSocialMediaId);

    List<ContactSocialMedia> getContactSocialMedia(UUID contactSocialMediaVersion) throws NotFoundException;
}
