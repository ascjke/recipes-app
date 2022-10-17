package ru.borisov.repository;

import org.springframework.data.repository.CrudRepository;
import ru.borisov.model.Recipe;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameContainsIgnoreCaseOrderByDateDesc(String name);
}
