package io.spring.demo.issuesdashboard.user;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Rob Winch
 */
@Document
public class User {
    @Id
    private Long id;

    @NotEmpty(message = "This field is required")
    private String password;

    @NotEmpty(message = "This field is required")
    @Indexed
    private String firstName;

    @NotEmpty(message = "This field is required")
    private String lastName;

    public User() {}

    public User(User user) {
        this(user.getId(), user.getPassword(), user.getFirstName(), user.getLastName());
    }

    public User(Long id, String password, String firstName,
            String lastName) {
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
