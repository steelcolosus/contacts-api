package actions.security;


import utils.constants.UserLoginStatus;
import models.db.security.Token;
import models.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import repositories.TokenRepository;
import services.interfaces.AuthenticationService;

import javax.inject.Named;

@Named
public class TokenAuthAction extends Action.Simple {
    public final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String AUTH_TOKEN = "authToken";


    public TokenRepository tokenRepository;
    public AuthenticationService authenticationService;

    @Autowired
    public TokenAuthAction(TokenRepository tokenRepository,AuthenticationService authenticationService) {
        this.tokenRepository = tokenRepository;
        this.authenticationService = authenticationService;
    }

    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

    @Override
    public Promise<Result> call(Context ctx) throws Throwable {
        User user = null;
        Token token = null;
        String[] authTokenHeaderValues = ctx.request().headers().get(AUTH_TOKEN_HEADER);

        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {

            //user = models.user.User.findByAuthToken(authTokenHeaderValues[0]);
            token = tokenRepository.findUserByAuthToken(authTokenHeaderValues[0]);
            if (token != null) {

                UserLoginStatus status = token.status;

                if (authenticationService.isTokenExpired(token)) {
                    return F.Promise.pure((Result) unauthorized("Token expired"));
                } else {
                    user = token.user;
                    if (user != null) {
                        ctx.args.put("user", user);
                        return delegate.call(ctx);
                    }
                }
            } else {
                return F.Promise.pure((Result) unauthorized(".|."));
            }

        }

        return F.Promise.pure((Result) unauthorized());
    }
}