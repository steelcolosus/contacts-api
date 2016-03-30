package controllers;

import controllers.base.BaseCrudController;
import models.db.contacts.SocialMedia;
import org.springframework.beans.factory.annotation.Autowired;
import actions.security.TokenAuth;
import services.base.interfaces.contacts.SocialMediaService;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
@TokenAuth
public class SocialMediaController extends BaseCrudController<SocialMedia> {


	SocialMediaService socialMediaService;


	@Autowired
	public SocialMediaController(SocialMediaService socialMediaService) {
		super( socialMediaService );
	}


}
