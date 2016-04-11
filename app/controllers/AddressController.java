package controllers;

import models.db.contacts.Address;
import models.db.contacts.Contact;
import models.db.contacts.EmailAddress;
import models.db.contacts.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.interfaces.contacts.AddressService;
import services.base.interfaces.contacts.ContactService;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

import static play.libs.Json.toJson;

@Named
@Singleton
//@TokenAuth
public class AddressController extends Controller {


    AddressService addressService;




    @Autowired
    public AddressController(AddressService addressService, ContactService contactService) {
        this.addressService = addressService;
    }


    public Result save(long contactId) {
        Form<Address> form = Form.form(Address.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest(form.errorsAsJson());
        Address address = form.get();

        try {
            addressService.addAddress(contactId,address);
        } catch (NotFoundException e) {
            return notFound();
        }
        return ok();


    }

    public Result delete(long id) {
        try {
            Address address = addressService.findById(id);
            if (address != null) {
                addressService.deleteAddress(address);
                return ok();
            }
        } catch (NotFoundException e) {
            return notFound();
        }
        return notFound();
    }

    public Result update(long id) {
        Form<Address> form = Form.form(Address.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        Address address = form.get();
        try {
            Address updated = addressService.updateAddress(id, address);
            return ok(toJson(updated));
        } catch (NotFoundException e) {
            return badRequest();
        }
    }

}
