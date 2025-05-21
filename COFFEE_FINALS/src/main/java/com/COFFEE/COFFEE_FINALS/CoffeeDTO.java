package com.COFFEE.COFFEE_FINALS;

import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Data Transfer Object for Coffee with validation annotations.
 * This class is used to receive and validate form data from coffee forms.
 */
public class CoffeeDTO {
    private int id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "")
    private String name;

    @NotBlank(message = "Type is required")
    @Size(min = 2, max = 30, message = "")
    private String type;

    @NotBlank(message = "Size is required")
    private String size;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than or equal to 1")
    @Max(value = 10000, message = "Price must be less than or equal to 10000")
    private Double price;

    @NotBlank(message = "Roast Level is required")
    private String roastLevel;

    @NotBlank(message = "Origin is required")
    @Size(min = 2, max = 50, message = "")
    private String origin;

    private boolean isDecaf;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    private List<String> flavorNotes;

    @NotBlank(message = "Brew Method is required")
    private String brewMethod;

    // Default constructor
    public CoffeeDTO() {
    }

    // Constructor with all fields
    public CoffeeDTO(int id, String name, String type, String size, Double price, String roastLevel, String origin,
                     boolean isDecaf, Integer stock, List<String> flavorNotes, String brewMethod) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.price = price;
        this.roastLevel = roastLevel;
        this.origin = origin;
        this.isDecaf = isDecaf;
        this.stock = stock;
        this.flavorNotes = flavorNotes;
        this.brewMethod = brewMethod;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRoastLevel() {
        return roastLevel;
    }

    public void setRoastLevel(String roastLevel) {
        this.roastLevel = roastLevel;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public boolean isDecaf() {
        return isDecaf;
    }

    public void setDecaf(boolean decaf) {
        isDecaf = decaf;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<String> getFlavorNotes() {
        return flavorNotes;
    }

    public void setFlavorNotes(List<String> flavorNotes) {
        this.flavorNotes = flavorNotes;
    }

    public String getBrewMethod() {
        return brewMethod;
    }

    public void setBrewMethod(String brewMethod) {
        this.brewMethod = brewMethod;
    }

    /**
     * Converts this CoffeeDTO to a Coffee entity
     * @param id The ID to assign to the new Coffee
     * @return A new Coffee entity with data from this DTO
     */
    public Coffee toCoffee(int id) {
        return new Coffee(id, name, type, size, price, roastLevel, origin, isDecaf, stock, flavorNotes, brewMethod);
    }

    /**
     * Creates a CoffeeDTO from a Coffee entity
     * @param coffee The Coffee entity to convert
     * @return A new CoffeeDTO with data from the Coffee entity
     */
    public static CoffeeDTO fromCoffee(Coffee coffee) {
        return new CoffeeDTO(
                coffee.getId(),
                coffee.getName(),
                coffee.getType(),
                coffee.getSize(),
                coffee.getPrice(),
                coffee.getRoastLevel(),
                coffee.getOrigin(),
                coffee.isDecaf(),
                coffee.getStock(),
                coffee.getFlavorNotes(),
                coffee.getBrewMethod()
        );
    }
} 