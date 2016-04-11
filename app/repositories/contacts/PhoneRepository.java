package repositories.contacts;

import models.db.contacts.Address;
import models.db.contacts.Phone;
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
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Query("select p from Phone p where p.contact.id = ?1")
    List<Phone> findByContactId(long id);

}