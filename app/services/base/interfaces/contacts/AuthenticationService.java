package services.base.interfaces.contacts;

import models.db.User;
import models.db.security.Token;
import services.base.GenericService;

/**
 * Created by eduardo on 18/03/16.
 */

public interface AuthenticationService extends GenericService<User,Long> {
    public User login (User user);
    public boolean isTokenExpired(Token token);
    public void logout (Long userId);
    public User getLoggedInUser ();
    public Token getUserToken (User user);
}
