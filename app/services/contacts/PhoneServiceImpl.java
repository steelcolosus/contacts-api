package services.contacts;


import models.db.contacts.Contact;
import models.db.contacts.EmailAddress;
import models.db.contacts.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.libs.F;
import repositories.contacts.PhoneRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.PhoneService;
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
public class PhoneServiceImpl extends AbstractService<Phone, Long> implements PhoneService {


    PhoneRepository phoneRepository;
    VersionHistoryService versionHistoryService;

    @Autowired
    public PhoneServiceImpl(PhoneRepository phoneRepository,VersionHistoryService versionHistoryService) {
        super(phoneRepository);
        this.phoneRepository = phoneRepository;
        this.versionHistoryService = versionHistoryService;

    }

    @Override
    public void addPhone(Contact contact, Phone address) throws NotFoundException {
        address.setContact(contact);
        save(address);
        List<Phone> phoneList = findByContactId(contact.getId());
        F.Promise.promise(() -> versionHistoryService.newPhoneVersion(contact, phoneList, false));
    }

    @Override
    public Phone updatePhone(Long id, Phone phone) throws NotFoundException {
        Phone updatedPhone = update(id,phone);
        List<Phone> addresses = findByContactId(phone.getContact().getId());
        F.Promise.promise(() -> versionHistoryService.newPhoneVersion(phone.getContact(), addresses, false));
        return updatedPhone;
    }

    @Override
    public void deletePhone(Phone phone) throws NotFoundException {
        delete(phone.getId());
        F.Promise.promise(() -> versionHistoryService.newPhoneVersion(phone.getContact(), findByContactId(phone.getContact().getId()), false));
    }

    @Override
    public List<Phone> findByContactId(Long id) {
        return phoneRepository.findByContactId(id);
    }

    @Override
    public List<Phone> mergeContactPhones(Contact original, List<Phone> phoneList) {
        List<Phone> originalPhone = phoneRepository.findByContactId(original.getId());

        if (originalPhone == null)
            originalPhone = new ArrayList<>();

        for (Phone phone : phoneList) {
            if (!containsPhone(phone, originalPhone)) {
                Phone _phone = new Phone();
                _phone.setContact(original);
                _phone.setLabel(phone.getLabel());
                _phone.setPhoneNumber(phone.getPhoneNumber());
                originalPhone.add(_phone);
            }
        }
        phoneRepository.save(originalPhone);
        return originalPhone;
    }

    private boolean containsPhone(Phone phone, List<Phone> originalPhoneList) {
        for (Phone originalPhone : originalPhoneList) {
            if (phone.getPhoneNumber().trim().equalsIgnoreCase(originalPhone.getPhoneNumber())) {
                return true;
            }
        }
        return false;
    }


}
