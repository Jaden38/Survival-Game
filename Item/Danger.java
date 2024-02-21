package Item;

import Event.EventType;

import java.util.Random;

/**
 * Danger class describes the different kinds of dangers that can be found in the    game.
 */
public class Danger extends EventType {
    /**
     * danger name
     */
    private String name;

    /**
     * danger description
     */
    private String description;
    /**
     * danger quantity
     */
    private int quantity;
    /**
     * danger damage
     */
    private int damage;

    private double experience;

    /**
     * Constructor for Danger class
     *
     * @param name
     * @param description
     * @param quantity
     * @param damage
     */
    public Danger(String name, String description, int quantity, int damage) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.damage = damage;
        setExperience();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public int getDamage() {
        return damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience() {
        Random random = new Random();
        this.experience = DataLists.dangerXp.get(random.nextInt(DataLists.dangerXp.size())) * getQuantity();
    }

    @Override
    public String toString() {
        return "Name : " + name +
                "\nDescription : " + description +
                "\nQuantity : " + quantity +
                "\nDamage : " + damage;
    }
}
