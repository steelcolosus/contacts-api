package controllers;

import actions.security.TokenAuth;
import controllers.base.BaseCrudController;
import models.db.User;
import models.db.contacts.*;
import models.dtos.ContactDTO;
import models.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import play.api.libs.iteratee.Cont;
import play.cache.Cached;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.interfaces.UserService;
import services.base.interfaces.contacts.AuthenticationService;
import services.base.interfaces.contacts.ContactService;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;

import java.util.List;
import java.util.UUID;

import static play.libs.Json.toJson;

@Named
@Singleton
//@TokenAuth
public class ContactController extends Controller {


    ContactService contactService;



    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    public Result save(long userId) {
        Form<ContactDTO> form = Form.form(ContactDTO.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        ContactDTO contact = form.get();
        try {
            contact.setUserId(userId);
            contact = contactService.saveContact(contact);
        } catch (NotFoundException e) {
            return notFound(toJson(e.getMessage()));
        }
        return ok(toJson(contact));
    }


    public Result update(long contactId) {
        Form<Contact> form = Form.form(Contact.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        Contact contact = form.get();
        try {
            Contact updated = contactService.updateContact(contactId, contact);
            return ok(toJson(updated));
        } catch (NotFoundException e) {
            return badRequest();
        }

    }

    public Result delete(long contactId) {
        try {
            boolean deleted = contactService.deleteContact(contactId);
            return ok(toJson(deleted));
        } catch (NotFoundException e) {
            return badRequest();
        }
    }

    public Result merge(long original, long update) {
        try {
            ContactDTO mergedContact = contactService.mergeContact(original, update);
            return ok(toJson(mergedContact));
        } catch (NotFoundException e) {
            return ok(toJson(e.getMessage()));
        }

    }

    public Result getContact(long contactId) {
        try {
            ContactDTO contact = contactService.getContact(contactId);
            return ok(toJson(contact));
        } catch (NotFoundException e) {

            return notFound(toJson(e.getMessage()));
        }

    }

    public Result getAllContacts(long userId) {
        try {
            List<ContactDTO> contacts = contactService.getAllContacts(userId);
            return ok(toJson(contacts));
        } catch (NotFoundException e) {
            return ok();
        }
    }

    public Result getFavoriteContacts(long userId) {
        List<ContactDTO> favorites = null;
        try {
            favorites = contactService.getFavorites(userId);
        } catch (NotFoundException e) {
            return notFound(toJson(e.getMessage()));
        }
        return ok(toJson(favorites));
    }

    public Result addFavorite(long contactId) {
        try {
            contactService.addToFavorite(contactId);
            return ok();
        } catch (NotFoundException e) {
            return notFound(toJson(e.getStackTrace().toString()));
        }
    }

    public Result getLastTenSearchedContacts(long userId) {
        List<ContactDTO> topTen = null;
        try {
            topTen = contactService.getLastTenSearches(userId);
            return ok(toJson(topTen));
        } catch (NotFoundException e) {
            return notFound(toJson(e.getStackTrace().toString()));
        }

    }

    public Result getMostSearchedContacts(long userId) {
        List<ContactDTO> mostSearched = null;
        try {
            mostSearched = contactService.getMostSearchedContacts(userId);
            return ok(toJson(mostSearched));
        } catch (NotFoundException e) {
            return notFound(toJson(e.getStackTrace().toString()));
        }

    }

    public Result revertContactToPreviousVersion(long contactId) {
        try {
            ContactDTO contact = contactService.revertToPreviousVersion(contactId);
            return ok(toJson(contact));
        } catch (NotFoundException e) {
            return badRequest(e.getMessage());
        }

    }

    public Result revertToVersion(long versionId){
        try {
            ContactDTO contactDTO = contactService.revertToVersion(versionId);
            return ok(toJson(contactDTO));
        } catch (NotFoundException e) {
            return notFound(toJson(e.getMessage()));
        }
    }

    public Result getContactVersions(long contactId) {
        List<ContactDTO> contactVersions = null;
        try {
            contactVersions = contactService.getContactHistory(contactId);
            return ok(toJson(contactVersions));
        } catch (NotFoundException e) {
            return notFound(toJson(e.getMessage()));
        }

    }

    public Result compareContactWithPreviewsVersion(long contactId, long versionId) {
        return ok();
    }

    public Result compareContacts(long contactId, long comparableContactId) {
        return ok();
    }

}
