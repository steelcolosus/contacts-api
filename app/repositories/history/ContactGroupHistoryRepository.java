package repositories.history;

import models.db.contacts.ContactGroup;
import models.db.historty.AddressHistory;
import models.db.historty.ContactGroupHistory;
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
public interface ContactGroupHistoryRepository extends JpaRepository<ContactGroupHistory, Long> {

    @Query("select cgh from ContactGroupHistory cgh where cgh.contactGroupId = ?1 order by cgh.creationDate desc ")
    Page<ContactGroupHistory> findContactGroupHistory(long contactGroupId, Pageable pageable);

    @Query("select cgh from ContactGroupHistory cgh where cgh.contactGroupId = ?1 order by cgh.creationDate desc ")
    List<ContactGroupHistory> findContactGroupHistory(long contactGroupId);

    @Query("select cg from ContactGroupHistory  cg where cg.version = ?1 ")
    List<ContactGroupHistory> findByVersion(UUID version);
}