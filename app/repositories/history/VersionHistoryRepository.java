package repositories.history;

import models.db.historty.PhoneHistory;
import models.db.historty.VersionHistory;
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
public interface VersionHistoryRepository extends JpaRepository<VersionHistory, Long> {

    @Query("select vh from VersionHistory vh where vh.contactId = ?1 ")
    Page<VersionHistory> findVersionHistory(long contactId, Pageable pageable);

    @Query("select vh from VersionHistory vh where vh.contactId = ?1 order by vh.creationDate desc ")
    List<VersionHistory> findVersionHistory(long contactId);


}