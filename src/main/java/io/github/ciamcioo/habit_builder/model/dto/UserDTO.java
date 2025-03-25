package io.github.ciamcioo.habit_builder.model.dto;

import jakarta.validation.constraints.*;

import java.util.Objects;

public record UserDTO(
        @NotBlank(message = "Email field cannot be blank")
        @Email(message = "Email must have the correct format")
        String email,

        @NotBlank(message = "Username field cannot be blank")
        @Size(max = 30, message = "Username must be less than 30 characters")
        String username,

        @Size(min = 2, max = 30,
              message = "First name must be in range from 2 to 30 characters")
        String firstName,

        @Size(min = 2, max = 50,
              message = "Last name must be in a range from 2 to 50 characters" )
        String lastName,

        @PositiveOrZero(message = "Age must not be a negative number")
        Integer age
) {
        public UserDTO(String email, String username, String firstName, String lastName, Integer age) {
                this.email = email;
                this.username = username;
                this.firstName = firstName != null ? firstName : "unspecified";
                this.lastName = lastName != null ? lastName : "unspecified";
                this.age = age != null ? age : 0;
        }


        @Override
        public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                UserDTO userDTO = (UserDTO) o;
                return  Objects.equals(email, userDTO.email) &&
                        Objects.equals(username, userDTO.username) &&
                        Objects.equals(lastName, userDTO.lastName) &&
                        Objects.equals(firstName, userDTO.firstName) &&
                        Objects.equals(age, userDTO.age);
        }

        @Override
        public int hashCode() {
                return Objects.hash(email, username, firstName, lastName, age);
        }

        @Override
        public String toString() {
                return "UserDTO{" +
                        "email='" + email + '\'' +
                        ", username='" + username + '\'' +
                        ", firstName='" + firstName + '\'' +
                        ", lastName='" + lastName + '\'' +
                        ", age=" + age +
                        '}';
        }
}
