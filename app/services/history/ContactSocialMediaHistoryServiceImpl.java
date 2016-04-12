package services.history;

import models.db.contacts.Contact;
import models.db.contacts.ContactSocialMedia;
import models.db.contacts.SocialMedia;
import models.db.historty.ContactSocialMediaHistory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.SocialMediaRepository;
import repositories.history.ContactSocialMediaHistoryRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.SocialMediaService;
import services.base.interfaces.history.ContactSocialMediaHistoryService;
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
public class ContactSocialMediaHistoryServiceImpl extends AbstractService<ContactSocialMediaHistory,Long> implements ContactSocialMediaHistoryService {

    ContactSocialMediaHistoryRepository contactSocialMediaHistoryRepository;
    SocialMediaRepository socialMediaRepository;


    @Autowired
    public ContactSocialMediaHistoryServiceImpl(ContactSocialMediaHistoryRepository contactSocialMediaHistoryRepository,SocialMediaRepository socialMediaRepository) {
        super(contactSocialMediaHistoryRepository);
        this.contactSocialMediaHistoryRepository = contactSocialMediaHistoryRepository;
        this.socialMediaRepository = socialMediaRepository;
    }

    @Override
    public UUID newVersion(ContactSocialMedia contactSocialMedia, UUID contactVersion, Long socialMediaId, UUID version, boolean markAsDeleted) {

        ContactSocialMediaHistory contactSocialMediaHistory = new ContactSocialMediaHistory();
        contactSocialMediaHistory.setContactSocialMediaId(contactSocialMedia.getId());
        contactSocialMediaHistory.setContactVersion(contactVersion);
        contactSocialMediaHistory.setSocialMediaId(socialMediaId);
        contactSocialMediaHistory.setUrl(contactSocialMedia.getUrl());
        contactSocialMediaHistory.setVersion(version);
        if(markAsDeleted)
            contactSocialMediaHistory.setDeletedAt(DateTime.now().toDate());
        save(contactSocialMediaHistory);
        return contactSocialMediaHistory.getVersion();
    }

    @Override
    public Optional<ContactSocialMediaHistory> getPreviousVersion(Long contactSocialMediaId) {
        return contactSocialMediaHistoryRepository.findContactSocialMediaHistory(contactSocialMediaId,new PageRequest(0,1)).getContent().stream().findFirst();
    }

    @Override
    public List<ContactSocialMedia> getContactSocialMedia(UUID contactSocialMediaVersion) throws NotFoundException{
        List<ContactSocialMediaHistory> contactSocialMediaHistoryList = contactSocialMediaHistoryRepository.findByVersion(contactSocialMediaVersion);
        List<ContactSocialMedia>  contactSocialMediaList = new ArrayList();
        for(ContactSocialMediaHistory contactSocialMediaHistory : contactSocialMediaHistoryList){
            ContactSocialMedia contactSocialMedia = new ContactSocialMedia();
            SocialMedia socialMedia = socialMediaRepository.findOne(contactSocialMediaHistory.getSocialMediaId());
            if(socialMedia==null)
                throw new NotFoundException("Social media element with id: "+contactSocialMediaHistory.getSocialMediaId());
            contactSocialMedia.setSocialMedia(socialMedia);
            contactSocialMedia.setUrl(contactSocialMediaHistory.getUrl());
            //contactSocialMedia.setId(contactSocialMediaHistory.getContactSocialMediaId());
            contactSocialMediaList.add(contactSocialMedia);
        }

        return contactSocialMediaList;
    }
}
