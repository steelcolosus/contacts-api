package controllers;

import controllers.base.BaseCrudController;
import dtos.UserDTO;
import models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import play.mvc.Result;
import security.TokenAuth;
import services.interfaces.UserService;

import javax.inject.Named;
import javax.inject.Singleton;

import static play.libs.Json.toJson;

@Named
@Singleton
@TokenAuth
public class UserController extends BaseCrudController< User > {


	UserService userService;


	@Autowired
	public UserController( UserService userService ) {
		super( userService );
		this.userService = userService;
		setUpdateClass( UserDTO.class );
	}

	public Result getLoggedInUser ()
	{
		User user = userService.getLoggedInUser();
		if (user != null && user.getId() != null)
		{
			return ok(toJson(user));
		}

		return  notFound();
	}
}
