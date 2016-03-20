package repositories.contacts;

import models.db.contacts.Address;
import models.db.contacts.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 23/10/14.
 */
@Named
@Singleton
public interface PhoneRepository extends JpaRepository<Phone, Long> {


}