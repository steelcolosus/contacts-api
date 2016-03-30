package services;


import models.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.TokenRepository;
import repositories.UserRepository;
import services.base.AbstractService;
import services.base.interfaces.UserService;

import javax.inject.Named;
import javax.inject.Singleton;

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

	public User finByEmail( String email ) {
		return userRepository.findUserByEmail( email );
	}



	//endregion


}
