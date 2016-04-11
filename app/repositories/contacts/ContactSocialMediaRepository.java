package repositories.contacts;

import models.db.contacts.ContactSocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by eduardo on 23/10/14.
 */
@Named
@Singleton
public interface ContactSocialMediaRepository extends JpaRepository<ContactSocialMedia, Long> {

    @Query("select csm from ContactSocialMedia csm where  csm.contact.id = ?1")
    List<ContactSocialMedia> findByContactId(long id);

}