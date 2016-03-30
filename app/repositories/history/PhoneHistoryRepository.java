package repositories.history;

import models.db.historty.EmailAddressHistory;
import models.db.historty.PhoneHistory;
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
public interface PhoneHistoryRepository extends JpaRepository<PhoneHistory, Long> {

    @Query("select ph from PhoneHistory ph where ph.phoneId = ?1 order by ph.creationDate desc ")
    Page<PhoneHistory> findPhoneHistory(long contactId, Pageable pageable);

    @Query("select ph from PhoneHistory ph where ph.version = ?1 order by ph.creationDate desc ")
    List<PhoneHistory> findPhoneHistory(UUID phoneVersion);

    @Query("select ph from PhoneHistory  ph where ph.version = ?1 ")
    PhoneHistory findByVersion(UUID version);
}