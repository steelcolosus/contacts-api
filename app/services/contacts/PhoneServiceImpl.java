package services.contacts;


import models.db.contacts.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.PhoneRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.PhoneService;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class PhoneServiceImpl extends AbstractService<Phone, Long > implements PhoneService {


	PhoneRepository phoneRepository;

	@Autowired
	public PhoneServiceImpl(PhoneRepository phoneRepository) {
		super(phoneRepository);

	}
}
