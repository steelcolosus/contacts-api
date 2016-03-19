package controllers;


import utils.constants.StatusCode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.dtos.LoginDTO;
import models.db.security.Token;
import models.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import actions.security.TokenAuth;
import actions.security.TokenAuthAction;
import services.interfaces.AuthenticationService;
import services.interfaces.UserService;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class SecurityController extends Controller /* extends Action.Simple */ {

    public UserService userService;
    public AuthenticationService authenticationService;


    @Autowired
    public SecurityController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }


    @Transactional
    public Result signUp() {
        Form<LoginDTO> form = Form.form(LoginDTO.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(form.errorsAsJson());
        } else {
            LoginDTO loginDTO = form.get();
            User user = userService.finByEmail(loginDTO.email);
            if (user != null) {
                return status(StatusCode.USER_EXISTS, "User already exists");
            }
            user = new User();
            user.setEmail(loginDTO.getEmail());
            user.setPassword(loginDTO.getPassword());
            user = userService.save(user);
            if (user == null) {
                return status(StatusCode.BAD_CREDENTIALS);
            } else {
                return ok();
            }
        }
    }

    // returns an authToken
    @Transactional
    public Result login() {
        Form<LoginDTO> loginForm = Form.form(LoginDTO.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }

        LoginDTO loginDTO = loginForm.get();

        User user = userService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());


        if (user == null) {
            return status(StatusCode.BAD_CREDENTIALS);
        } else {
            user = authenticationService.login(user);
            if (user == null) {
                return status(StatusCode.TOKEN_EXPIRED);
            }
            Token token = authenticationService.getUserToken(user);
            ObjectNode authTokenJson = Json.newObject();
            authTokenJson.put("id", user.getId());
            authTokenJson.put("name", user.firstName + " " + user.lastName);
            authTokenJson.put("role", "admin");
            authTokenJson.put(TokenAuthAction.AUTH_TOKEN, token.getAuthToken());
            response().setCookie(TokenAuthAction.AUTH_TOKEN, token.getAuthToken());
            return ok(authTokenJson);
        }
    }


    @Transactional
    @TokenAuth
    public Result logout() {
        response().discardCookie(TokenAuthAction.AUTH_TOKEN);
        //User user = TokenAuthAction.getUser();
        User user = authenticationService.getLoggedInUser();
        authenticationService.logout(user.getId());
        return ok("logged out");
    }


}