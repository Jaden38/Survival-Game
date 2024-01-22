/**
 * Danger class describes the different kinds of dangers that can be found in the game.
 */
public class Danger {

    /** danger name */
    private String name;

    /** danger description */
    private String description;

    /** danger probability */
    private int probability;

    /** danger damage */
    private int damage;

    /**
     * Constructor for Danger class
     * @param name
     * @param description
     * @param probability
     * @param damage
     */
    public Danger(String name, String description, int probability, int damage) {
        this.name = name;
        this.description = description;
        this.probability = probability;
        this.damage = damage;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getProbability() {
        return probability;
    }

    public int getDamage() {
        return damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
