package services.contacts;


import models.db.contacts.CGroup;
import models.db.contacts.Contact;
import models.db.contacts.ContactGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import play.libs.F;
import repositories.contacts.GroupRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.GroupService;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class GroupServiceImpl extends AbstractService<CGroup, Long > implements GroupService {


	GroupRepository groupRepository;

	@Autowired
	public GroupServiceImpl(GroupRepository groupRepository ) {
		super(groupRepository);

	}


}
