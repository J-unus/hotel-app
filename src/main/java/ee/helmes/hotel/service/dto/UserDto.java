package ee.helmes.hotel.service.dto;

import ee.helmes.hotel.domain.User;
import java.io.Serializable;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String login;

    public UserDto() {
        // Empty constructor needed for Jackson.
    }

    public UserDto(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.login = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            "}";
    }
}
