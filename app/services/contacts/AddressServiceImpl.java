package services.contacts;


import models.db.contacts.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.AddressRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.AddressService;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class AddressServiceImpl extends AbstractService<Address, Long> implements AddressService {


    AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        super(addressRepository);

    }
}
