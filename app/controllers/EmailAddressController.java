package controllers;

import actions.security.TokenAuth;
import models.db.contacts.Address;
import models.db.contacts.Contact;
import models.db.contacts.EmailAddress;
import org.springframework.beans.factory.annotation.Autowired;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.interfaces.contacts.ContactService;
import services.base.interfaces.contacts.EmailAddressService;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;

import static play.libs.Json.toJson;

@Named
@Singleton
@TokenAuth
public class EmailAddressController extends Controller {


    EmailAddressService emailAddressService;
    ContactService contactService;


    @Autowired
    public EmailAddressController(EmailAddressService emailAddressService, ContactService contactService, VersionHistoryService versionHistoryService) {
        this.emailAddressService = emailAddressService;
        this.contactService = contactService;
    }


    public Result save(long contactId) {
        Form<EmailAddress> form = Form.form(EmailAddress.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest(form.errorsAsJson());
        EmailAddress emailAddress = form.get();

        Contact contact = contactService.findById(contactId);
        try {
            emailAddressService.addEmailAddress(contact,emailAddress);
        } catch (NotFoundException e) {
            return notFound();
        }
        return ok(toJson(contact));


    }

    public Result delete(long id) {
        try {
            EmailAddress emailAddress = emailAddressService.findById(id);
            emailAddressService.deleteEmailAddress(emailAddress);
            return ok();
        } catch (NotFoundException e) {
            return notFound();
        }
    }

    public Result update(long id) {
        Form<EmailAddress> form = Form.form(EmailAddress.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        EmailAddress emailAddress = form.get();
        try {
            EmailAddress updated = emailAddressService.update(id, emailAddress);
            return ok(toJson(updated));
        } catch (NotFoundException e) {
            return badRequest();
        }
    }

}
