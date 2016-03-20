package repositories.contacts;

import models.db.contacts.ContactSocialMedia;
import models.db.contacts.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 23/10/14.
 */
@Named
@Singleton
public interface ContactSocialMediaRepository extends JpaRepository<ContactSocialMedia, Long> {


}