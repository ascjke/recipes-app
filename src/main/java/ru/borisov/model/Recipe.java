package ru.borisov.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "recipes")
public class Recipe {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private long id;

    @Column
    @NotNull
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column
    @NotNull
    @NotBlank(message = "Category is mandatory")
    private String category;

    @Column
    @UpdateTimestamp
    private LocalDateTime date;

    @Column
    @NotBlank(message = "Description is mandatory")
    private String description;

    @Column
    @ElementCollection
    @NotNull(message = "Ingredients shouldn't be null")
    @Size(min = 1, message = "At least one ingredient is required")
    private List<String> ingredients;

    @Column
    @ElementCollection
    @NotNull
    @Size(min = 1, message = "At least one direction is required")
    private List<String> directions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public void copyOf(Recipe recipe) {
        name = recipe.name;
        description = recipe.description;
        category = recipe.category;
        ingredients = recipe.ingredients;
        directions = recipe.directions;
    }
}

