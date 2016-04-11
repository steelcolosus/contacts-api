package services.contacts;


import models.db.contacts.Address;
import models.db.contacts.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.libs.F;
import repositories.contacts.AddressRepository;
import repositories.contacts.ContactRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.AddressService;
import services.base.interfaces.history.VersionHistoryService;
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
public class AddressServiceImpl extends AbstractService<Address, Long> implements AddressService {


    AddressRepository addressRepository;
    ContactRepository contactRepository;
    VersionHistoryService versionHistoryService;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, VersionHistoryService versionHistoryService,ContactRepository contactRepository) {
        super(addressRepository);
        this.addressRepository = addressRepository;
        this.contactRepository = contactRepository;
        this.versionHistoryService = versionHistoryService;

    }


    @Override
    public List<Address> findByContactId(long contactId) {
        return addressRepository.findByContactId(contactId);
    }

    @Override
    public List<Address> mergeContactAddress(Contact original, List<Address> addressList) {
        List<Address> originalAddressList = addressRepository.findByContactId(original.getId());
        if (originalAddressList == null)
            originalAddressList = new ArrayList<>();

        for (Address address : addressList) {
            Address newAddress = new Address();
            newAddress.setAddress(address.getAddress());
            newAddress.setContact(original);
            originalAddressList.add(newAddress);
        }

        addressRepository.save(originalAddressList);
        return originalAddressList;
    }

    @Override
    public void addAddress(Long contactId, Address address) throws NotFoundException {
        Contact contact = contactRepository.findOne(contactId);
        address.setContact(contact);
        save(address);
        List<Address> addresses = findByContactId(contact.getId());
        F.Promise.promise(() -> versionHistoryService.newAddressVersion(contact, addresses, false));
    }

    @Override
    public Address updateAddress(Long id, Address address) throws NotFoundException {
        Address updatedAddress = update(id, address);
        List<Address> addresses = findByContactId(updatedAddress.getContact().getId());
        F.Promise.promise(() -> versionHistoryService.newAddressVersion(address.getContact(), addresses, false));
        return updatedAddress;
    }

    @Override
    public void deleteAddress(Address address) throws NotFoundException {
        delete(address.getId());
        F.Promise.promise(() -> versionHistoryService.newAddressVersion(address.getContact(), addressRepository.findByContactId(address.getContact().getId()), false));
    }

}
