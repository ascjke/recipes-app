package ru.borisov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.borisov.model.Recipe;
import ru.borisov.model.User;
import ru.borisov.repository.RecipeRepository;

import java.util.List;


@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    public Recipe createRecipeForUser(Recipe recipe, UserDetails userDetails) {

        User user = getCurrentUser(userDetails);
        recipe.setUser(user);

        return recipeRepository.save(recipe);
    }

    public void updateRecipe(Recipe newRecipe, long id, UserDetails userDetails) {

        Recipe recipeFromDb = getRecipeById(id);

        User currentUser = getCurrentUser(userDetails);
        checkPermissionsOfCurrentUserOverTheseRecipe(recipeFromDb, currentUser);

        recipeFromDb.copyOf(newRecipe);
        recipeRepository.save(recipeFromDb);
    }

    public Recipe getRecipeById(long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Recipe with id= " + id + " not found"));
    }

    public List<Recipe> getRecipesByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> getRecipesByName(String name) {
        return recipeRepository.findByNameContainsIgnoreCaseOrderByDateDesc(name);
    }

    public void deleteRecipe(long id, UserDetails userDetails) {

        Recipe recipeFromDb = getRecipeById(id);

        User currentUser = getCurrentUser(userDetails);
        checkPermissionsOfCurrentUserOverTheseRecipe(recipeFromDb, currentUser);

        recipeRepository.deleteById(id);
    }

    private static void checkPermissionsOfCurrentUserOverTheseRecipe(Recipe recipe, User user) {
        if (!(recipe.getUser().getPassword().equals(user.getPassword()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Current user doesn't have permissions for update or delete a recipe");
        }
    }

    private static User getCurrentUser(UserDetails userDetails) {
        User user = new User();
        user.setEmail(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        return user;
    }

}