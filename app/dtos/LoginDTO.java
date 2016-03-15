package dtos;

import play.data.validation.Constraints;

/**
 * Created by eduardo on 14/12/15.
 */
public class LoginDTO {
    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
