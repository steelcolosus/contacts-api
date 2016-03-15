package controllers;


import constants.StatusCode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dtos.LoginDTO;
import models.security.Token;
import models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import security.TokenAuth;
import security.TokenAuthAction;
import services.interfaces.UserService;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class SecurityController extends Controller /* extends Action.Simple */ {

    public UserService userService;


    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
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
                return verifyUser(user);
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
            return verifyUser(user);
        }
    }


    @Transactional
    @TokenAuth
    public Result logout() {
        response().discardCookie(TokenAuthAction.AUTH_TOKEN);
        //User user = TokenAuthAction.getUser();
        User user = userService.getLoggedInUser();
        userService.logout(user.getId());
        return ok("logged out");
    }


    private Result verifyUser(User user) {
        user = userService.login(user);
        if (user == null) {
            return status(StatusCode.TOKEN_EXPIRED);
        }
        Token token = userService.getUserToken(user);
        ObjectNode authTokenJson = Json.newObject();
        authTokenJson.put("id", user.getId());
        authTokenJson.put("name", user.firstName + " " + user.lastName);
        authTokenJson.put("role", "admin");
        authTokenJson.put(TokenAuthAction.AUTH_TOKEN, token.getAuthToken());
        response().setCookie(TokenAuthAction.AUTH_TOKEN, token.getAuthToken());
        return ok(authTokenJson);
    }


}