/**
 * Controller class for handling coffee-related operations in the Spring Boot application.
 */
package com.SpringbootCOMPRO.coffee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class    HomeController {
    private List<Coffee> coffeeList = new ArrayList<>();

    public HomeController() {
        coffeeList.add(new Coffee(1, "Espresso", "Arabica", "Small", 3.50, "Dark", "Ethiopia", false, 10, Arrays.asList("Chocolate", "Nutty"), "Espresso"));
        coffeeList.add(new Coffee(2, "Latte", "Arabica", "Medium", 4.50, "Medium", "Brazil", false, 8, Arrays.asList("Creamy", "Sweet"), "Drip"));
        coffeeList.add(new Coffee(3, "Cappuccino", "Robusta", "Large", 5.00, "Medium", "Colombia", false, 12, Arrays.asList("Fruity", "Bold"), "French Press"));
        coffeeList.add(new Coffee(4, "Mocha", "Arabica", "Medium", 4.75, "Dark", "Guatemala", false, 6, Arrays.asList("Chocolate", "Smooth"), "Espresso"));
        coffeeList.add(new Coffee(5, "Americano", "Robusta", "Large", 3.25, "Light", "Kenya", false, 15, Arrays.asList("Citrus", "Balanced"), "Drip"));
    }

    /**
     * Handles GET requests to the root ("/") endpoint.
     * Retrieves the list of coffee objects and adds them to the model
     * to be displayed on the index page.
     *
     * @param model
     * @return The name of the Thymeleaf template to render ("index").
     */
    @GetMapping("/")
    public String getCoffees(Model model) {
        model.addAttribute("coffees", coffeeList);
        return "index";
    }
    /**
     * to GET requests to the "/delete" endpoint.
     * To remove the coffee object with the specified ID from the list.
     * After deletion, it redirects back to the main list.
     *
     * @param id
     * @return A redirect to the home page ("/") after deletion.
     */
    @GetMapping("/delete")
    public String deleteCoffee(@RequestParam int id) {
        coffeeList.removeIf(coffee -> coffee.getId() == id);
        return "redirect:/";

    }
    /**
     * responds to "/add" endpoint GET requests.
     * Returns the view for adding a new coffee entry.
     *
     * @return The name of the Thymeleaf template ("new") for adding a coffee.
     */
    @GetMapping("/add")
    public String addCoffee() {
        return "new";
    }
    /**
     * manages the "/save" endpoint's POST requests.
     * Creates a new Coffee object with the provided details and adds it to the coffee list.
     * After saving, it redirects back to the main list.
     *
     * @param name
     * @param type
     * @param size
     * @param price
     * @param roastLevel
     * @param origin
     * @param isDecaf
     * @param stock
     * @param flavorNotes
     * @param brewMethod
     * @return A redirect to the home page ("/") after saving the coffee.
     */
    @PostMapping("/save")
    public String saveCoffee(@RequestParam String name,
                             @RequestParam String type,
                             @RequestParam String size,
                             @RequestParam double price,
                             @RequestParam String roastLevel,
                             @RequestParam String origin,
                             @RequestParam boolean isDecaf,
                             @RequestParam int stock,
                             @RequestParam List<String> flavorNotes,
                             @RequestParam String brewMethod) {

        int newId = coffeeList.get(coffeeList.size() - 1).getId() + 1; // Ensure 'coffees' is properly initialized
        coffeeList.add(new Coffee(newId, name, type, size, price, roastLevel, origin, isDecaf, stock, flavorNotes, brewMethod));

        return "redirect:/";
    }
    /**
     * Handles GET requests to the "/edit" endpoint.
     * Retrieves a coffee object by its ID and adds it to the model for editing.
     * If the coffee is found, it returns the "edit" view; otherwise, it redirects to the home page.
     *
     * @param id
     * @param model
     * @return The name of the Thymeleaf template ("edit") if coffee is found, otherwise redirects to "/".
     */
    @GetMapping("/edit")
    public String editCoffee(@RequestParam int id, Model model) {
        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                model.addAttribute("coffee", coffee);
                return "edit";
            }
        }
        return "redirect:/";
    }

    /**
     * Handles POST requests to the "/update" endpoint.
     * Updates an existing coffee entry with the provided details.
     * If the coffee with the given ID is found, its properties are updated.
     * After updating, it redirects back to the main list.
     *
     * @param id
     * @param name
     * @param type
     * @param size
     * @param price
     * @param roastLevel
     * @param origin
     * @param isDecaf
     * @param stock
     * @param flavorNotes
     * @param brewMethod
     * @return
     */
    @PostMapping("/update")
    public String updateCoffee(@RequestParam int id,
                               @RequestParam String name,
                               @RequestParam String type,
                               @RequestParam String size,
                               @RequestParam double price,
                               @RequestParam String roastLevel,
                               @RequestParam String origin,
                               @RequestParam boolean isDecaf,
                               @RequestParam int stock,
                               @RequestParam List<String> flavorNotes,
                               @RequestParam String brewMethod) {

        for (Coffee coffee : coffeeList) {
            if (coffee.getId() == id) {
                coffee.setName(name);
                coffee.setType(type);
                coffee.setSize(size);
                coffee.setPrice(price);
                coffee.setRoastLevel(roastLevel);
                coffee.setOrigin(origin);
                coffee.setDecaf(isDecaf);
                coffee.setStock(stock);
                coffee.setFlavorNotes(flavorNotes);
                coffee.setBrewMethod(brewMethod);
                break;
            }
        }
        return "redirect:/";
    }
}
