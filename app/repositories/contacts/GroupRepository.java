package repositories.contacts;

import models.db.contacts.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 23/10/14.
 */
@Named
@Singleton
public interface GroupRepository extends JpaRepository<Group, Long> {


}