package services.history;

import models.db.contacts.Contact;
import models.db.contacts.Phone;
import models.db.historty.PhoneHistory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import repositories.history.PhoneHistoryRepository;
import services.base.AbstractService;
import services.base.interfaces.history.PhoneHistoryService;

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
public class PhoneHistoryServiceImpl extends AbstractService<PhoneHistory,Long> implements PhoneHistoryService {

    PhoneHistoryRepository phoneHistoryRepository;


    @Autowired
    public PhoneHistoryServiceImpl(PhoneHistoryRepository phoneHistoryRepository) {
        super(phoneHistoryRepository);
        this.phoneHistoryRepository = phoneHistoryRepository;
    }

    @Override
    public UUID newVersion(Phone phone, UUID version, boolean markAsDeleted) {

        PhoneHistory phoneHistory = new PhoneHistory();
        phoneHistory.setPhoneId(phone.getId());
        phoneHistory.setLabel(phone.getLabel());
        phoneHistory.setPhoneNumber(phone.getPhoneNumber());
        phoneHistory.setVersion(version);
        if(markAsDeleted)
            phoneHistory.setDeletedAt(DateTime.now().toDate());
        save(phoneHistory);
        return phoneHistory.getVersion();

    }

    @Override
    public Optional<PhoneHistory> getPreviousVersion(Long phoneId) {
        return phoneHistoryRepository.findPhoneHistory(phoneId,new PageRequest(0,1)).getContent().stream().findFirst();
    }

    @Override
    public List<Phone> getContactPhone(UUID contactPhoneVersion){
        List<PhoneHistory> phoneHistoryList  = phoneHistoryRepository.findPhoneHistory(contactPhoneVersion);
        List<Phone> phoneList = new ArrayList();
        for(PhoneHistory phoneHistory : phoneHistoryList){
            Phone phone = new Phone();
            phone.setLabel(phoneHistory.getLabel());
            phone.setPhoneNumber(phoneHistory.getPhoneNumber());
            //phone.setId(phoneHistory.getPhoneId());
            phoneList.add(phone);
        }
        return phoneList;
    }
}
