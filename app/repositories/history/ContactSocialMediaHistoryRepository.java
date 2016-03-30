package repositories.history;

import models.db.contacts.Contact;
import models.db.historty.AddressHistory;
import models.db.historty.ContactSocialMediaHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

/**
 * Created by eduardo on 23/10/14.
 */
@Named
@Singleton
public interface ContactSocialMediaHistoryRepository extends JpaRepository<ContactSocialMediaHistory, Long> {

    @Query("select csmh from ContactSocialMediaHistory csmh where csmh.contactSocialMediaId = ?1 order by csmh.creationDate")
    Page<ContactSocialMediaHistory> findContactSocialMediaHistory(long contactSocialMediaId, Pageable pageable);

    @Query("select csmh from ContactSocialMediaHistory csmh where csmh.contactSocialMediaId = ?1 order by csmh.creationDate")
    List<ContactSocialMediaHistory> findContactSocialMediaHistory(long contactSocialMediaId);

    @Query("select csmh from ContactSocialMediaHistory  csmh where csmh.version = ?1 ")
    List<ContactSocialMediaHistory> findByVersion(UUID version);

}