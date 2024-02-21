package Item;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;

public class ResourceModel {
    private final StringProperty name;
    private final IntegerProperty quantity;
    private final ObjectProperty<ImageView> logo;

    public ResourceModel(Resource resource, ImageView logo) {
        this.name = new SimpleStringProperty(resource.getName());
        this.quantity = new SimpleIntegerProperty(resource.getQuantity());
        this.logo = new SimpleObjectProperty<>(logo);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public ImageView getLogo() {
        return logo.get();
    }

    public ObjectProperty<ImageView> logoProperty() {
        return logo;
    }

    public void setLogo(ImageView logo) {
        this.logo.set(logo);
    }
}