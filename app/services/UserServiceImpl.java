package services;


import constants.UserLoginStatus;
import models.security.Token;
import models.user.User;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.mvc.Http;
import repositories.TokenRepository;
import repositories.UserRepository;
import services.base.AbstractService;
import services.interfaces.UserService;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.UUID;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class UserServiceImpl extends AbstractService< User, Long > implements UserService {


	UserRepository  userRepository;
	TokenRepository tokenRepository;


	@Autowired
	public UserServiceImpl( UserRepository userRepository, TokenRepository tokenRepository ) {
		super( userRepository );
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
	}


	//region Custom methods
	@Transactional( readOnly = true )
	public User findByEmailAndPassword( String email, String password ) {
		return userRepository.findUserByEmailAndPassword( email, User.getMD5( password ) );
	}

	@Transactional( readOnly = true )
	public Long count() {
		return userRepository.count();
	}


	@Transactional
	public User login( User user ) {

		String authToken = UUID.randomUUID().toString();

		Token token = tokenRepository.findTokenByUserId(user.id);

		//TODO add logic for expired token


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

	public User finByEmail( String email ) {
		return userRepository.findUserByEmail( email );
	}

	public Token getUserToken( User user ) {
		return tokenRepository.findTokenByUserId( user.getId() );
	}
	//endregion


}
