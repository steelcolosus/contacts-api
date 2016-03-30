package models.db.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by eduardo on 18/03/16.
 */
@MappedSuperclass
public class AbstractHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    private UUID version;

    private Date deletedAt;

    @JsonIgnore
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    public Date creationDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getVersion() {
        return version;
    }

    public void setVersion(UUID version) {
        this.version = version;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
