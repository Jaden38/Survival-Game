package Event;

import javafx.scene.image.ImageView;

public class Event {

    private double x;
    private double y;
    private String type;
    public ImageView imageView;
    private EventType eventType;

    private boolean hasAlreadyVisited;


    public Event(double x, double y, String type, ImageView imageView, EventType eventType) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.imageView = imageView;
        this.eventType = eventType;
        this.hasAlreadyVisited=false;
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public boolean isHasAlreadyVisited() {
        return hasAlreadyVisited;
    }

    public void setHasAlreadyVisited(boolean hasAlreadyVisited) {
        this.hasAlreadyVisited = hasAlreadyVisited;
    }

    @Override
    public String toString() {
        return "Event{" +
                "x=" + x +
                ", y=" + y +
                ", type='" + type + '\'' +
                ", imageView=" + imageView.toString() +
                ", eventType=" + eventType.getName()+
                '}';
    }

}
