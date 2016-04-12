package services.contacts;


import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import models.db.contacts.ContactSocialMedia;
import models.db.contacts.SocialMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.libs.F;
import repositories.contacts.ContactSocialMediaRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.ContactSocialMediaService;
import services.base.interfaces.history.VersionHistoryService;
import utils.ObjectUtils;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class ContactSocialMediaServiceImpl extends AbstractService<ContactSocialMedia, Long> implements ContactSocialMediaService {


    ContactSocialMediaRepository contactSocialMediaRepository;
    VersionHistoryService versionHistoryService;

    @Autowired
    public ContactSocialMediaServiceImpl(ContactSocialMediaRepository contactSocialMediaRepository, VersionHistoryService versionHistoryService) {
        super(contactSocialMediaRepository);
        this.contactSocialMediaRepository = contactSocialMediaRepository;
        this.versionHistoryService = versionHistoryService;
    }



    @Override
    public List<ContactSocialMedia> findByContactId(Long id) {
        return contactSocialMediaRepository.findByContactId(id);
    }

    @Override
    public ContactSocialMedia updateSocialMedia(Long id, ContactSocialMedia contactSocialMedia)throws NotFoundException {
        ContactSocialMedia updatedContactSocialMedia = update(id,contactSocialMedia);
        List<ContactSocialMedia> contactSocialMediaList = findByContactId(contactSocialMedia.getContact().getId());
        F.Promise.promise(() -> versionHistoryService.newContactSocialMediaVersion(updatedContactSocialMedia.getContact(), contactSocialMediaList, false));
        return null;
    }

    @Override
    public void deleteSocialMedia(ContactSocialMedia contactSocialMedia) throws NotFoundException {
        delete(contactSocialMedia.getId());
        F.Promise.promise(() -> versionHistoryService.newContactSocialMediaVersion(contactSocialMedia.getContact(), findByContactId(contactSocialMedia.getContact().getId()), false));
    }

    @Override
   public List<ContactSocialMedia> mergeContactSocialMedia(Contact contact, List<ContactSocialMedia> contactSocialMediaList) {
        List<ContactSocialMedia> originalContactSocialMedia = contactSocialMediaRepository.findByContactId(contact.getId());
        for (ContactSocialMedia contactSocialMedia : contactSocialMediaList) {
            ContactSocialMedia newContactSocialMedia = new ContactSocialMedia();
            newContactSocialMedia.setContact(contact);
            newContactSocialMedia.setSocialMedia(contactSocialMedia.getSocialMedia());
            newContactSocialMedia.setUrl(contactSocialMedia.getUrl());
            contactSocialMediaRepository.save(newContactSocialMedia);
            originalContactSocialMedia.add(newContactSocialMedia);
        }
        return originalContactSocialMedia;
    }


}
