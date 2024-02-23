package Player;

import Interface.CharacterObserver;
import Item.Danger;
import Item.Resource;
import Item.Tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Character class describes the components of a character in the game.
 */
public class Character {

    private int level;

    private double experience;

    /**
     * character name
     */
    private String name;

    /**
     * character health
     */
    private int health;

    /**
     * character hunger
     */
    private int hunger;

    /**
     * character thirst
     */
    private int thirst;

    /**
     * character tools
     */
    private Tool[] tools;

    /**
     * character resources
     */
    public Inventory playerInventory;

    private List<CharacterObserver> observers;

    public Player player;
    public boolean isDead = false;

    /**
     * Constructor for Character class
     *
     * @param level
     * @param experience
     * @param name
     * @param health
     * @param hunger
     * @param thirst
     * @param tools
     * @param resources
     * @param dangers
     */
    public Character(String name,
                     int health,
                     int hunger,
                     int thirst) {
        this.level = 1;
        this.experience = 0;
        this.name = name;
        this.health = health;
        this.hunger = hunger;
        this.thirst = thirst;
        this.playerInventory = new Inventory();
        observers = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public Tool[] getTool() {
        return tools;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setHealth(int health) {
        this.health = health;
        if (health <= 0) {
            this.health = 0;
        }
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
        if (thirst <= 0) {
            this.thirst = 0;
            notifyThirstObservers();
        }
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
        if (hunger <= 0) {
            this.hunger = 0;
            notifyHungerObservers();
        }
    }

    public void addObserver(CharacterObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CharacterObserver observer) {
        observers.remove(observer);
    }

    private void notifyHealthObservers() {
        for (CharacterObserver observer : observers) {
            observer.onHealthZero();
        }
    }

    private void notifyThirstObservers() {
        for (CharacterObserver observer : observers) {
            observer.onThirstZero();
        }
    }

    private void notifyHungerObservers() {
        for (CharacterObserver observer : observers) {
            observer.onHungerZero();
        }
    }

    public void setTools(Tool[] tools) {
        this.tools = tools;
    }


    // Methods to interact with tne environment

    /**
     * Collect a resource in the environment. The resource will be added to the character's resources.
     *
     * @param resource a resource to collect
     */
    public void collectResource(Resource resource) {
        throw new UnsupportedOperationException();
    }

    /**
     * Choose a tool to use. Currently I don't know how to implement this method.
     *
     * @param tool an object tool
     */
    public void useTool(Tool tool) {
        throw new UnsupportedOperationException();
    }

    /**
     * Encounter a danger in the environment. The character's health will be reduced.
     *
     * @param danger an object danger
     */
    public void encouterDanger(Danger danger) {
        // Reduce the character's health
        this.health -= danger.getDamage();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public void decreaseHealth(int amount) {
        int newHealth = health - amount;
        if (newHealth <= 0) {
            notifyHealthObservers();
        }
        setHealth(newHealth);
    }

    public void decreaseHunger(int amount) {
        int newHunger = hunger - amount;
        if (newHunger <= 0) {
            notifyHungerObservers();
        }
        setHunger(newHunger);
    }

    public void decreaseThirst(int amount) {
        int newThirst = thirst - amount;
        if (newThirst <= 0) {
            notifyThirstObservers();
        }
        setThirst(newThirst);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Display the character's status
     */
    @Override
    public String toString() {
        return "Character{" +
                "level=" + level +
                ", experience=" + experience +
                ", name='" + name + '\'' +
                ", health=" + health +
                ", hunger=" + hunger +
                ", thirst=" + thirst +
                ", tools=" + Arrays.toString(tools) +
                ", playerInventory=" + playerInventory.toString() +
                '}';
    }
}