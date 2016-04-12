package controllers;

import actions.security.TokenAuth;
import models.db.contacts.Address;
import models.db.contacts.CGroup;
import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import org.springframework.beans.factory.annotation.Autowired;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.interfaces.contacts.AddressService;
import services.base.interfaces.contacts.ContactGroupService;
import services.base.interfaces.contacts.ContactService;
import services.base.interfaces.contacts.GroupService;
import services.base.interfaces.history.VersionHistoryService;
import utils.exceptions.NotFoundException;

import javax.inject.Named;
import javax.inject.Singleton;

import static play.libs.Json.toJson;

@Named
@Singleton
@TokenAuth
public class ContactGroupController extends Controller {


    GroupService groupService;
    ContactService contactService;
    ContactGroupService contactGroupService;


    @Autowired
    public ContactGroupController(GroupService groupService, ContactService contactService,  ContactGroupService contactGroupService) {
        this.groupService = groupService;
        this.contactService = contactService;
        this.contactGroupService = contactGroupService;

    }


    public Result save(long contactId, long groupId) {
            Contact contact = contactService.findById(contactId);
            CGroup group = groupService.findById(groupId);
            if (contact == null || group == null)
                return notFound();
            contactGroupService.addGroup(contact,group);
            return ok();

    }

    public Result delete(long id) {
        try {
            ContactGroup contactGroup = contactGroupService.findById(id);
            contactGroupService.deleteGroup(contactGroup);
            return ok();
        } catch (NotFoundException e) {
            return notFound();
        }
    }

    public Result update(long id) {
        Form<ContactGroup> form = Form.form(ContactGroup.class).bindFromRequest();
        if (form.hasErrors())
            return badRequest();
        ContactGroup contactGroup = form.get();
        try {
            ContactGroup updated = contactGroupService.updateGroup(id, contactGroup);
            return ok(toJson(updated));
        } catch (NotFoundException e) {
            return badRequest();
        }
    }

}
