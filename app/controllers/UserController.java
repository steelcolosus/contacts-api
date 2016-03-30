package controllers;

import controllers.base.BaseCrudController;
import models.dtos.UserDTO;
import models.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Result;
import actions.security.TokenAuth;
import services.base.interfaces.contacts.AuthenticationService;
import services.base.interfaces.UserService;

import javax.inject.Named;
import javax.inject.Singleton;

import static play.libs.Json.toJson;

@Named
@Singleton
@TokenAuth
public class UserController extends BaseCrudController< User > {


	UserService userService;
	AuthenticationService authenticationService;

	@Autowired
	public UserController( UserService userService, AuthenticationService authenticationService) {
		super( userService );
		this.userService = userService;
		this.authenticationService = authenticationService;
		setUpdateClass( UserDTO.class );
	}

	public Result getLoggedInUser ()
	{
		User user = authenticationService.getLoggedInUser();
		if (user != null && user.getId() != null)
		{
			return ok(toJson(user));
		}

		return  notFound();

	}
}
