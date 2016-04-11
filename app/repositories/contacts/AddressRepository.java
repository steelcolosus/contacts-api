package repositories.contacts;

import models.db.contacts.Address;
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
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("select a from Address a where a.contact.id = ?1")
    List<Address> findByContactId(long id);
}