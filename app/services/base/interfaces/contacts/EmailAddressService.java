package services.base.interfaces.contacts;

import models.db.User;
import models.db.contacts.Contact;
import models.db.contacts.EmailAddress;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by eduardo on 5/04/15.
 */

public interface EmailAddressService extends GenericService<EmailAddress, Long>
{


    void addEmailAddress(Contact contact, EmailAddress address) throws NotFoundException;

    EmailAddress updateEmailAddress(Long id, EmailAddress address) throws NotFoundException;

    void deleteEmailAddress(EmailAddress emailAddress) throws NotFoundException;

    List<EmailAddress> findByContactId(Long id);

    List<EmailAddress> mergeContactEmailAddress(Contact original, List<EmailAddress> emailAddressList);
}
