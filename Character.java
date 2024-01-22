/**
 * Character class describes the components of a character in the game.
 */
public class Character {

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
    private Resource[] resources;

    /**
     * character dangers
     */
    private Danger[] dangers;

    /**
     * Constructor for Character class
     *
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
                     int thirst,
                     Tool[] tools,
                     Resource[] resources,
                     Danger[] dangers) {
        this.name = name;
        this.health = health;
        this.hunger = hunger;
        this.thirst = thirst;
        this.tools = tools;
        this.resources = resources;
        this.dangers = dangers;
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

    public Resource[] getResources() {
        return resources;
    }

    public Danger[] getDangers() {
        return dangers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        if (health < 0) {
            System.out.println("Health can't be negative.");
        } else {
            this.health = health;
        }
    }

    public void setHunger(int hunger) {
        if (hunger < 0) {
            System.out.println("Hunger can't be negative.");
        } else {
            this.hunger = hunger;
        }
    }

    public void setThirst(int thirst) {
        if (thirst < 0) {
            System.out.println("Thirst can't be negative.");
        } else {
            this.thirst = thirst;
        }
    }

    public void setTools(Tool[] tools) {
        this.tools = tools;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public void setDangers(Danger[] dangers) {
        this.dangers = dangers;
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

    /**
     * Display the character's status
     */
    public String toString() {
        return "Name: " + name + "\n" +
                "Health: " + health + "\n" +
                "Hunger: " + hunger + "\n" +
                "Thirst: " + thirst + "\n" +
                "Tools: " + tools + "\n" +
                "Resources: " + resources + "\n" +
                "Dangers: " + dangers + "\n";
    }
}