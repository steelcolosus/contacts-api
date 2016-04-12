package services.history;

import models.db.contacts.Contact;
import models.db.contacts.EmailAddress;
import models.db.historty.EmailAddressHistory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import repositories.history.EmailAddressHistoryRepository;
import services.base.AbstractService;
import services.base.interfaces.history.EmailAddressHistoryService;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 22/03/16.
 */
@Named
@Singleton
@Transactional
public class EmailAddressHistoryServiceImpl extends AbstractService<EmailAddressHistory,Long> implements EmailAddressHistoryService {

    EmailAddressHistoryRepository emailAddressHistoryRepository;


    @Autowired
    public EmailAddressHistoryServiceImpl(EmailAddressHistoryRepository emailAddressHistoryRepository) {
        super(emailAddressHistoryRepository);
        this.emailAddressHistoryRepository = emailAddressHistoryRepository;
    }

    @Override
    public UUID newVersion(EmailAddress emailAddress, UUID version, boolean markAsDeleted) {
        EmailAddressHistory emailAddressHistory = new EmailAddressHistory();
        emailAddressHistory.setBaseEmailAddressId(emailAddress.getId());
        emailAddressHistory.setEmailAddress(emailAddress.getEmail());
        emailAddressHistory.setVersion(version);
        if(markAsDeleted)
            emailAddressHistory.setDeletedAt(DateTime.now().toDate() );
        save(emailAddressHistory);
        return emailAddressHistory.getVersion();

    }

    @Override
    public Optional<EmailAddressHistory> getPreviousVersion(Long emailAddressId) {
        return emailAddressHistoryRepository.findEmailAddressHistory(emailAddressId,new PageRequest(0,1)).getContent().stream().findFirst();
    }

    @Override
    public List<EmailAddress> getEmailAddress(UUID emailAddressVersion){
        List<EmailAddressHistory> contactEmailAddress = emailAddressHistoryRepository.findByVersion(emailAddressVersion);
        List<EmailAddress> emailAddressList = new ArrayList<>();
        for(EmailAddressHistory emailAddressHistory : contactEmailAddress){
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setEmail(emailAddressHistory.getEmailAddress());
            //emailAddress.setId(emailAddressHistory.getBaseEmailAddressId());
            emailAddressList.add(emailAddress);

        }
        return emailAddressList;
    }
}
