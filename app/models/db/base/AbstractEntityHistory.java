package models.db.base;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by eduardo on 18/03/16.
 */
@MappedSuperclass
public class AbstractEntityHistory extends AbstractEntity{


    public Date deletedAt;
    public long version;
}
