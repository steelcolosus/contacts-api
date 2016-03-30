package controllers;

import actions.security.TokenAuth;
import controllers.base.BaseCrudController;
import models.db.User;
import models.db.contacts.Contact;
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
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;

import java.util.List;

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

    public Result addContact() {
        Form<Contact> form = Form.form(Contact.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        Contact contact = form.get();
        contact = contactService.save(contact);
        return ok(toJson(contact));
    }

    public Result updateContact(long contactId) {
        Form<Contact> form = Form.form(Contact.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        Contact contact = form.get();
        try {
            Contact updated = contactService.update(contactId, contact);
            return ok(toJson(updated));
        } catch (NotFoundException e) {
            return badRequest();
        }

    }

    public Result deleteContact(long contactId) {
        try {
            boolean deleted = contactService.delete(contactId);
            return ok(toJson(deleted));
        } catch (NotFoundException e) {
            return badRequest();
        }
    }

    public Result mergeContact(long original, long update) {
        try {
            Contact mergedContact = contactService.mergeContact(original, update);
            return ok(toJson(mergedContact));
        } catch (NotFoundException e) {
            return ok(toJson(e.getMessage()));
        }

    }

    public Result getContact(long contactId) {
        try {
            Contact contact = contactService.getContact(contactId);
            return ok(toJson(contact));
        } catch (NotFoundException e) {

            return notFound();
        }

    }

    public Result getAllContacts(long userId) {
        try {
            List<Contact> contacts = contactService.getAllContacts(userId);
            return ok(toJson(contacts));
        } catch (NotFoundException e) {
            return ok();
        }
    }

    public Result getFavoriteContacts(long userId) {
        List<Contact> favorites = contactService.getFavorites(userId);
        return ok(toJson(favorites));
    }

    public Result getLastTenSearchedContacts(long userId) {
        List<Contact> topTen = contactService.getLastTenSearches(userId);
        return ok(toJson(topTen));
    }


    public Result getMostSearchedContacts(long userId) {
        List<Contact> mostSearched = contactService.getMostSearchedContacts(userId);
        return ok(toJson(mostSearched));
    }

    public Result revertContactToPreviewsVersion(long contactId) {
        try {
            Contact contact = contactService.revertToPreviousVersion(contactId);
            return ok(toJson(contact));
        } catch (NotFoundException e) {
            return badRequest();
        }

    }

    public Result compareContactWithPreviewsVersion(long contactId, long versionId) {
        return ok();
    }

    public Result compareContacts(long contactId, long comparableContactId) {
        return ok();
    }

}
