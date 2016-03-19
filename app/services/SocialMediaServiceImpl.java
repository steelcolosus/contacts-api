package services;


import models.db.contacts.SocialMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.SocialMediaRepository;
import services.base.AbstractService;
import services.interfaces.SocialMediaService;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class SocialMediaServiceImpl extends AbstractService<SocialMedia, Long > implements SocialMediaService {


	SocialMediaRepository socialMediaRepository;

	@Autowired
	public SocialMediaServiceImpl(SocialMediaRepository socialMediaRepository ) {
		super(socialMediaRepository);

	}
}
