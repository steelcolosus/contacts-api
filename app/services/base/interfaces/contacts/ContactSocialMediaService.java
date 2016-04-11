package services.base.interfaces.contacts;

import models.db.User;
import models.db.contacts.Contact;
import models.db.contacts.ContactSocialMedia;
import models.db.contacts.SocialMedia;
import services.base.GenericService;
import utils.exceptions.NotFoundException;

import java.util.List;

/**
 * Created by eduardo on 5/04/15.
 */

public interface ContactSocialMediaService extends GenericService<ContactSocialMedia, Long>
{


    List<ContactSocialMedia> findByContactId(Long id);

    ContactSocialMedia updateSocialMedia(Long id, ContactSocialMedia contactSocialMedia)throws NotFoundException;

    void deleteSocialMedia(ContactSocialMedia contactSocialMedia) throws NotFoundException;


    List<ContactSocialMedia> mergeContactSocialMedia(Contact contact, List<ContactSocialMedia> contactSocialMediaList);
}
