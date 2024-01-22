/**
 * Resource class which contains the name and quantity of a resource
 */
public class Resource {

    /** resource name */
    private String name;

    /** resource quantity */
    private int quantity;

    /**
     * Constructor for Resource class
     * @param name
     * @param quantity
     */
    public Resource(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
