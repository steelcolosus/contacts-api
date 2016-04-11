package services.contacts;


import models.db.contacts.Contact;
import models.db.contacts.EmailAddress;
import models.db.contacts.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.libs.F;
import repositories.contacts.EmailAddressRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.EmailAddressService;
import services.base.interfaces.history.VersionHistoryService;
import utils.ObjectUtils;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class EmailAddressServiceImpl extends AbstractService<EmailAddress, Long> implements EmailAddressService {


    EmailAddressRepository emailAddressRepository;
    VersionHistoryService versionHistoryService;

    @Autowired
    public EmailAddressServiceImpl(EmailAddressRepository emailAddressRepository,VersionHistoryService versionHistoryService) {
        super(emailAddressRepository);
        this.emailAddressRepository = emailAddressRepository;
        this.versionHistoryService = versionHistoryService;
    }

    @Override
    public void addEmailAddress(Contact contact, EmailAddress address) throws NotFoundException {
        address.setContact(contact);
        save(address);
        List<EmailAddress> addresses = findByContactId(contact.getId());
        F.Promise.promise(() -> versionHistoryService.newEmailAddressVersion(contact, addresses, false));
    }

    @Override
    public EmailAddress updateEmailAddress(Long id, EmailAddress address) throws NotFoundException {
        EmailAddress updatedAddress = save(address);
        List<EmailAddress> addresses = findByContactId(address.getContact().getId());
        F.Promise.promise(() -> versionHistoryService.newEmailAddressVersion(address.getContact(), addresses, false));
        return updatedAddress;
    }

    @Override
    public void deleteEmailAddress(EmailAddress emailAddress) throws NotFoundException {
        delete(emailAddress.getId());
        F.Promise.promise(() -> versionHistoryService.newEmailAddressVersion(emailAddress.getContact(), findByContactId(emailAddress.getContact().getId()), false));
    }

    @Override
    public List<EmailAddress> findByContactId(Long id) {
        return emailAddressRepository.findByContactId(id);
    }

    @Override
    public List<EmailAddress> mergeContactEmailAddress(Contact original, List<EmailAddress> emailAddressList) {

        List<EmailAddress> originalEmailAddressList = emailAddressRepository.findByContactId(original.getId());

        if (originalEmailAddressList == null)
            originalEmailAddressList = new ArrayList<>();

        for (EmailAddress emailAddress : emailAddressList) {
            if (!containsEmailAddress(emailAddress, originalEmailAddressList)) {
                EmailAddress mEmailAddress = new EmailAddress();
                mEmailAddress.setEmail(emailAddress.getEmail());
                mEmailAddress.setContact(original);
                originalEmailAddressList.add(emailAddress);
            }
        }
        emailAddressRepository.save(originalEmailAddressList);
        return originalEmailAddressList;
    }


    private boolean containsEmailAddress(EmailAddress email, List<EmailAddress> emailAddressList) {
        for (EmailAddress originalEmail : emailAddressList) {
            if (email.getEmail().trim().equalsIgnoreCase(originalEmail.getEmail().trim())) {
                return true;
            }
        }
        return false;
    }

}
