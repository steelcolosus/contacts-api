package services.history;


import models.db.contacts.*;
import models.db.historty.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import repositories.history.*;
import services.base.AbstractService;
import services.base.interfaces.history.*;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class ContactHistoryServiceImpl extends AbstractService<ContactHistory, Long> implements ContactHistoryService {


    ContactHistoryRepository contactHistoryRepository;


    @Autowired
    public ContactHistoryServiceImpl(ContactHistoryRepository contactHistoryRepository) {
        super(contactHistoryRepository);
        this.contactHistoryRepository = contactHistoryRepository;
    }

    @Override
    public UUID newVersion(Contact contact, UUID version, boolean markAsDeleted) {

        ContactHistory contactHistory = new ContactHistory();
        contactHistory.setContactId(contact.getId());
        contactHistory.setFavorite(contact.getFavorite());
        contactHistory.setFirstName(contact.getFirstName());
        contactHistory.setLastName(contact.getLastName());
        contactHistory.setMiddleName(contact.getMiddleName());
        contactHistory.setNickname(contact.getNickname());
        contactHistory.setPrefix(contact.getPrefix());
        contactHistory.setSufix(contact.getSufix());
        contactHistory.setVersion(version);
        if(markAsDeleted)
            contactHistory.setDeletedAt(DateTime.now().toDate());

        save(contactHistory);
        return contactHistory.getVersion();

    }

    @Override
    public Optional<ContactHistory> getPreviousVersion(Long contactId) {
        PageRequest page = new PageRequest(0,1);
        Optional<ContactHistory> contactHistory = contactHistoryRepository.findContactHistory(contactId,page).getContent().stream().findFirst();
        return contactHistory;
    }


    @Override
    public Contact getContact(UUID versionId) throws NotFoundException {
        ContactHistory contactHistory = contactHistoryRepository.findByVersion(versionId);
        if(contactHistory==null)
            throw new NotFoundException("contact with versionId: "+ versionId +" not found");

        Contact contact = new Contact();
        contact.setFirstName(contactHistory.getFirstName());
        contact.setFavorite(contactHistory.getFavorite());
        contact.setHit(0);
        contact.setLastName(contactHistory.getLastName());
        contact.setMiddleName(contactHistory.getMiddleName());
        contact.setNickname(contactHistory.getNickname());
        contact.setPrefix(contactHistory.getPrefix());
        contact.setSufix(contactHistory.getSufix());
        contact.setId(contactHistory.getContactId());
        return contact;
    }

    @Override
    public List<ContactHistory> getContactHistory(long contactId) throws NotFoundException {
        return contactHistoryRepository.findContactHistory(contactId);
    }
}
