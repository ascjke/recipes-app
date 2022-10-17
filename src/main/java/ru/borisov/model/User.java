package ru.borisov.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column
    @Email(regexp = ".+@.+\\..+", message = "Should be valid email")
    private String email;

    @Column
    @NotNull
    @NotBlank
    @Size(min = 8, message = "password should contain at least 8 characters and shouldn't be blank")
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Recipe> recipes;
}
