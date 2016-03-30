package repositories.history;

import models.db.historty.ContactHistory;
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
public interface ContactHistoryRepository extends JpaRepository<ContactHistory, Long> {

    @Query("select ch from ContactHistory ch where ch.contactId = ?1  order by ch.creationDate desc")
    Page<ContactHistory> findContactHistory(long contactId, Pageable pageable);

    @Query("select ch from ContactHistory ch where ch.contactId = ?1  order by ch.creationDate desc")
    List<ContactHistory> findContactHistory(long contactId);

    @Query("select ch from ContactHistory  ch where ch.version = ?1 ")
    ContactHistory findByVersion(UUID version);

}