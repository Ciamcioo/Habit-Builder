package io.github.ciamcioo.habit_builder.util;

import io.github.ciamcioo.habit_builder.model.dto.UserDTO;
import io.github.ciamcioo.habit_builder.model.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class UserBuilder {

    private UUID    id;
    private String  email;
    private String  username;
    private String  firstName;
    private String  lastName;
    private Integer age;

    public static UserBuilder getInstance() {
        return new UserBuilder();
    }

    @NotNull
    public  UserBuilder withTestValues() {
        this.id         = UUID.randomUUID();
        this.email      = "fooBar@gmail.com";
        this.username   = "FooBar";
        this.firstName  = "Foo";
        this.lastName   = "Bar";
        this.age        = 21;

        return this;
    }

    public UserBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withUsername(String username){
        this.username = username;
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder withAge(Integer age) {
        this.age = age;
        return this;
    }

    public User buildUser() {
        return new User(
               this.id,
               this.email,
               this.username,
               this.firstName,
               this.lastName,
               this.age
        );
    }

    public UserDTO buildUserDTO() {
        return new UserDTO(
               this.email,
               this.username,
               this.firstName,
               this.lastName,
               this.age
        );
    }




}
