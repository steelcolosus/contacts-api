package services.base.interfaces.contacts;

import models.db.contacts.Contact;
import models.db.contacts.Phone;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by eduardo on 19/03/16.
 */

public interface PhoneService extends GenericService<Phone, Long>{

    void addPhone(Contact contact, Phone address) throws NotFoundException;

    Phone updatePhone(Long id, Phone phone) throws NotFoundException;

    void deletePhone(Phone phone) throws NotFoundException;

    List<Phone> findByContactId(Long id);


    List<Phone> mergeContactPhones(Contact original, List<Phone> phoneList);
}
