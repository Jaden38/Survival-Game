package Event;

enum EventTypeEnum {
    RESOURCE,
    DANGER
}


public abstract class EventType {
    private EventTypeEnum name;
    private int quantity;
    private int damage;
    private String description;
    public String getName() {
        return name.toString();
    }
    public void setName(EventTypeEnum name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "name=" + name +
                '}';
    }
}
