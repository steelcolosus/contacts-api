package repositories.contacts;

import models.db.User;
import models.db.contacts.Contact;
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
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("select c from Contact c where c.user.id = ?1 and c.isFavorite = true")
    List<Contact> findFavoriteContacts(long userId);

    @Query("select c from Contact c where c.user.id = ?1")
    List<Contact> findContactByUserId(long userId);

    @Query("select c from Contact c where c.user.id = ?1  order by c.hit desc")
    List<Contact> findMostSearchedContacts(long userId);

    @Query("select c from Contact c where c.user.id = ?1  order by c.hit desc")
    List<Contact> findLastTenSearchedContacts(long userId);

}