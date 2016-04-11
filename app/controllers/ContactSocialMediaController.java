package controllers;

import models.db.contacts.*;
import org.springframework.beans.factory.annotation.Autowired;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.interfaces.contacts.ContactGroupService;
import services.base.interfaces.contacts.ContactService;
import services.base.interfaces.contacts.ContactSocialMediaService;
import services.base.interfaces.contacts.SocialMediaService;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;

import static play.libs.Json.toJson;

@Named
@Singleton
//@TokenAuth
public class ContactSocialMediaController extends Controller {


    SocialMediaService socialMediaService;
    ContactService contactService;
    ContactSocialMediaService contactSocialMediaService;


    @Autowired
    public ContactSocialMediaController(SocialMediaService socialMediaService, ContactService contactService, ContactSocialMediaService contactSocialMediaService) {
        this.socialMediaService = socialMediaService;
        this.contactService = contactService;
        this.contactSocialMediaService = contactSocialMediaService;

    }


    public Result save(long contactId, long groupId) {
            Contact contact = contactService.findById(contactId);
            SocialMedia socialMedia = socialMediaService.findById(groupId);
            if (contact == null || socialMedia == null)
                return notFound();
            ContactSocialMedia contactSocialMedia = new ContactSocialMedia();
            contactSocialMedia.setSocialMedia(socialMedia);
            contactSocialMedia.setContact(contact);
            contactSocialMediaService.save(contactSocialMedia);
            return ok();

    }

    public Result delete(long id) {
        try {
            ContactSocialMedia contactSocialMedia = contactSocialMediaService.findById(id);
            contactSocialMediaService.deleteSocialMedia(contactSocialMedia);
            return ok();
        } catch (NotFoundException e) {
            return notFound();
        }
    }

    public Result update(long id) {
        Form<ContactSocialMedia> form = Form.form(ContactSocialMedia.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        ContactSocialMedia contactSocialMedia = form.get();
        try {
            contactSocialMediaService.updateSocialMedia(id, contactSocialMedia);
            return ok();
        } catch (NotFoundException e) {
            return badRequest();
        }
    }

}
