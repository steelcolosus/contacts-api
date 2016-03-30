package repositories.history;

import models.db.historty.AddressHistory;
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
public interface AddressHistoryRepository extends JpaRepository<AddressHistory, Long> {
    @Query("select ah from AddressHistory ah where ah.addressId = ?1 order by ah.creationDate desc")
    Page<AddressHistory> findAddressHistory(long addressId, Pageable pageable);

    @Query("select ah from AddressHistory ah where ah.addressId = ?1 order by ah.creationDate desc")
    List<AddressHistory> findAddressHistory(long addressId);

    @Query("select ah from AddressHistory  ah where ah.version = ?1 ")
    List<AddressHistory> findByVersion(UUID version);
}