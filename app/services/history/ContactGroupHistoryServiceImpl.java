package services.history;

import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import models.db.contacts.Group;
import models.db.historty.ContactGroupHistory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.GroupRepository;
import repositories.history.ContactGroupHistoryRepository;
import services.base.AbstractService;
import services.base.interfaces.history.ContactGroupHistoryService;
import utils.exceptions.NotFoundException;

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
public class ContactGroupHistoryServiceImpl extends AbstractService<ContactGroupHistory, Long> implements ContactGroupHistoryService {

    ContactGroupHistoryRepository contactGroupHistoryRepository;
    GroupRepository groupRepository;

    @Autowired
    public ContactGroupHistoryServiceImpl(ContactGroupHistoryRepository contactGroupHistoryRepository, GroupRepository groupRepository) {
        super(contactGroupHistoryRepository);
        this.contactGroupHistoryRepository = contactGroupHistoryRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public UUID newVersion(ContactGroup contactGroup, Long groupId, UUID contactHistoryVersion, UUID version, boolean markAsDeleted) {
        ContactGroupHistory contactGroupHistory = new ContactGroupHistory();
        contactGroupHistory.setContactGroupId(contactGroup.getId());
        contactGroupHistory.setGroupId(groupId);
        contactGroupHistory.setContactVersion(contactHistoryVersion);
        contactGroupHistory.setVersion(version);
        if (markAsDeleted)
            contactGroupHistory.setDeletedAt(DateTime.now().toDate());
        save(contactGroupHistory);
        return contactGroupHistory.getVersion();
    }

    @Override
    public Optional<ContactGroupHistory> getPreviousVersion(Long contactGroupId) {
        return contactGroupHistoryRepository.findContactGroupHistory(contactGroupId, new PageRequest(0, 1)).getContent().stream().findFirst();
    }

    @Override
    public List<ContactGroup> getContactGroup(UUID contactGroupVersion) throws NotFoundException {
        List<ContactGroupHistory> contactGroupHistoryList = contactGroupHistoryRepository.findByVersion(contactGroupVersion);
        List<ContactGroup> contactGroups = new ArrayList<>();
        for (ContactGroupHistory contactGroupHistory : contactGroupHistoryList) {
            ContactGroup contactGroup = new ContactGroup();
            Group group = groupRepository.findOne(contactGroupHistory.getGroupId());
            if (group == null)
                throw new NotFoundException("Group element not found with id: " + contactGroupHistory.getGroupId());
            contactGroup.setGroup(group);
            contactGroup.setId(contactGroupHistory.getContactGroupId());
            contactGroups.add(contactGroup);

        }
        return contactGroups;
    }


}
