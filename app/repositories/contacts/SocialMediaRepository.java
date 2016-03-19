package repositories.contacts;

import models.db.contacts.SocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 23/10/14.
 */
@Named
@Singleton
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {


}