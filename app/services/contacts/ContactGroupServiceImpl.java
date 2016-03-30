package services.contacts;


import models.db.contacts.ContactGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.ContactGroupRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.ContactGroupService;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class ContactGroupServiceImpl extends AbstractService<ContactGroup, Long> implements ContactGroupService {


    ContactGroupRepository contactGroupRepository;

    @Autowired
    public ContactGroupServiceImpl(ContactGroupRepository contactGroupRepository) {
        super(contactGroupRepository);

    }
}
