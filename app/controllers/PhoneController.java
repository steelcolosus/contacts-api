package controllers;

import actions.security.TokenAuth;
import models.db.contacts.Address;
import models.db.contacts.Contact;
import models.db.contacts.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.interfaces.contacts.AddressService;
import services.base.interfaces.contacts.ContactService;
import services.base.interfaces.contacts.PhoneService;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;

import static play.libs.Json.toJson;

@Named
@Singleton
@TokenAuth
public class PhoneController extends Controller {


    PhoneService phoneService;
    ContactService contactService;


    @Autowired
    public PhoneController(PhoneService phoneService, ContactService contactService) {
        this.phoneService = phoneService;
        this.contactService = contactService;
    }


    public Result save(long contactId) {
        Form<Phone> form = Form.form(Phone.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest(form.errorsAsJson());
        Phone phone = form.get();

        Contact contact = contactService.findById(contactId);
        try {
            phoneService.addPhone(contact,phone);
        } catch (NotFoundException e) {
            return notFound();
        }
        return ok(toJson(phone));


    }

    public Result delete(long id) {
        try {
            Phone phone = phoneService.findById(id);
            if (phone != null) {
                phoneService.deletePhone(phone);
                return ok();
            }
        } catch (NotFoundException e) {
            return notFound();
        }
        return notFound();
    }

    public Result update(long id) {
        Form<Phone> form = Form.form(Phone.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        Phone phone = form.get();
        try {
            Phone updated = phoneService.updatePhone(id, phone);
            return ok(toJson(updated));
        } catch (NotFoundException e) {
            return badRequest();
        }
    }

}
