package repositories.contacts;

import models.db.contacts.Address;
import models.db.contacts.EmailAddress;
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
public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {

    @Query("select ea from EmailAddress ea where ea.contact.id = ?1")
    List<EmailAddress> findByContactId(long id);
}