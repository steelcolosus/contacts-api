package services.interfaces;

import models.db.User;
import services.base.GenericService;

/**
 * Created by eduardo on 5/04/15.
 */
public interface UserService extends GenericService<User, Long>
{
    public User findByEmailAndPassword (String email, String password);
    public Long count ();
    public User finByEmail (String email);


}
