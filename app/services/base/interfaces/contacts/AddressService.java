package services.base.interfaces.contacts;

import models.db.User;
import models.db.contacts.Address;
import models.db.contacts.Contact;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by eduardo on 5/04/15.
 */

public interface AddressService extends GenericService<Address, Long>
{


    List<Address> findByContactId(long contactId);

    List<Address> mergeContactAddress(Contact original, List<Address> addressList);

    void addAddress(Long contactId, Address address) throws NotFoundException;

    Address updateAddress(Long id, Address address) throws NotFoundException;

    void deleteAddress(Address address) throws NotFoundException;
}
