/**
 * Tool class which contains the tool used to survive
 */
public class Tool {

    /** tool name */
    private String name;

    /** tool quantity */
    private int quantity;

    /** tool durability */
    private int durability;

    /**
     * Constructor for Tool class
     * @param name
     * @param quantity
     */
    public Tool(String name,
                int quantity,
                int durability) {
        this.name = name;
        this.quantity = quantity;
        this.durability = durability;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    // Set quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
