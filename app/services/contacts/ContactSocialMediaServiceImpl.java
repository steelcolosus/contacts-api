package services.contacts;


import models.db.contacts.ContactSocialMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.ContactSocialMediaRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.ContactSocialMediaService;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class ContactSocialMediaServiceImpl extends AbstractService<ContactSocialMedia, Long> implements ContactSocialMediaService {


    ContactSocialMediaRepository contactSocialMediaRepository;

    @Autowired
    public ContactSocialMediaServiceImpl(ContactSocialMediaRepository contactSocialMediaRepository) {
        super(contactSocialMediaRepository);

    }
}
