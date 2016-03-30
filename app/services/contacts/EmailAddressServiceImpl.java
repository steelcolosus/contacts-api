package services.contacts;


import models.db.contacts.EmailAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.EmailAddressRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.EmailAddressService;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class EmailAddressServiceImpl extends AbstractService<EmailAddress, Long > implements EmailAddressService {


	EmailAddressRepository emailAddressRepository;

	@Autowired
	public EmailAddressServiceImpl(EmailAddressRepository emailAddressRepository ) {
		super(emailAddressRepository);

	}
}
