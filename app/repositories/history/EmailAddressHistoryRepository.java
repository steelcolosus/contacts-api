package repositories.history;

import models.db.historty.ContactSocialMediaHistory;
import models.db.historty.EmailAddressHistory;
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
public interface EmailAddressHistoryRepository extends JpaRepository<EmailAddressHistory, Long> {

    @Query("select eah from EmailAddressHistory eah where eah.baseEmailAddressId = ?1 order by eah.creationDate desc ")
    Page<EmailAddressHistory> findEmailAddressHistory(long emailAddressId, Pageable pageable);

    @Query("select eah from EmailAddressHistory eah where eah.baseEmailAddressId = ?1 order by eah.creationDate desc ")
    List<EmailAddressHistory> findEmailAddressHistory(long emailAddressId);

    @Query("select eah from EmailAddressHistory  eah where eah.version = ?1 ")
    List<EmailAddressHistory> findByVersion(UUID version);
}