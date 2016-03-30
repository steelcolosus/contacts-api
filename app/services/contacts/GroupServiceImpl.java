package services.contacts;


import models.db.contacts.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import repositories.contacts.GroupRepository;
import services.base.AbstractService;
import services.base.interfaces.contacts.GroupService;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by eduardo on 24/10/14.
 */

@Named
@Singleton
@Transactional
public class GroupServiceImpl extends AbstractService<Group, Long > implements GroupService {


	GroupRepository groupRepository;

	@Autowired
	public GroupServiceImpl(GroupRepository groupRepository ) {
		super(groupRepository);

	}
}
