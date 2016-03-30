package controllers;

import controllers.base.BaseCrudController;
import models.db.contacts.Group;
import org.springframework.beans.factory.annotation.Autowired;
import actions.security.TokenAuth;
import services.base.interfaces.contacts.GroupService;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
@TokenAuth
public class GroupController extends BaseCrudController<Group> {


	GroupService grupService;


	@Autowired
	public GroupController(GroupService grupService) {
		super( grupService );
	}


}
