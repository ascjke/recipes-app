package ru.borisov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.borisov.model.Recipe;
import ru.borisov.service.RecipeService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping(value = "/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("/new")
    public Map<String, Long> createRecipe(@Valid @RequestBody Recipe recipe,
                                          @AuthenticationPrincipal UserDetails userDetails) {

        Recipe savedRecipe = recipeService.createRecipeForUser(recipe, userDetails);
        return Map.of("id", savedRecipe.getId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateRecipe(@Valid @RequestBody Recipe recipe,
                             @PathVariable long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.updateRecipe(recipe, id, userDetails);
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable @Min(1) long id) {
        return recipeService.getRecipeById(id);
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam(required = false) String category,
                                      @RequestParam(required = false) String name) {

        if (category == null && name == null ||
                category != null && name != null ||
                category == null && name.isBlank() ||
                name == null && category.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (category == null) {
            return recipeService.getRecipesByName(name);
        } else {
            return recipeService.getRecipesByCategory(category);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable @Min(1) long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.deleteRecipe(id, userDetails);
    }
}
