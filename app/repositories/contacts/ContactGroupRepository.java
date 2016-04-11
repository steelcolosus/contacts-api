package repositories.contacts;

import models.db.contacts.ContactGroup;
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
public interface ContactGroupRepository extends JpaRepository<ContactGroup, Long> {

    @Query("select cg from ContactGroup cg where cg.contact.id = ?1")
    List<ContactGroup> findByContactId(long id);
}