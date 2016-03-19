package services;

import utils.constants.UserLoginStatus;
import models.db.User;
import models.db.security.Token;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import play.mvc.Http;
import repositories.TokenRepository;
import repositories.UserRepository;
import services.base.AbstractService;
import services.interfaces.AuthenticationService;

import java.util.UUID;

/**
 * Created by eduardo on 18/03/16.
 */
public class AuthenticationServiceImpl extends AbstractService<User,Long> implements AuthenticationService {
    UserRepository userRepository;
    TokenRepository tokenRepository;


    public AuthenticationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository ) {
        super( userRepository );
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public User login( User user ) {

        String authToken = UUID.randomUUID().toString();

        Token token = tokenRepository.findTokenByUserId(user.id);

        if (token == null)
        {
            token = new Token();
            token.setStatus(UserLoginStatus.NEW);
        }
        else
        {
            token.setStatus(UserLoginStatus.ACTIVE);
        }


        token.setAuthToken(authToken);
        token.setExpirationDate(DateTime.now().plusDays(1).toDate());
        token.setUser(user);
        tokenRepository.save(token);

        return user;

    }

    public boolean isTokenExpired(Token token) {
        Long expired = token.expirationDate.getTime();
        Long now = DateTime.now().getMillis();
        if(now>expired){
            return true;
        }
        return false;
    }

    @Transactional
    public void logout( Long userId ) {

        Token token = tokenRepository.findTokenByUserId(userId);
        token.setAuthToken( null );
        token.setStatus( UserLoginStatus.OUT );

        tokenRepository.save( token );
    }

    public User getLoggedInUser() {
        //TODO check for null
        return ( User ) Http.Context.current().args.get( "user" );
    }



    public Token getUserToken( User user ) {
        return tokenRepository.findTokenByUserId( user.getId() );
    }
}
