package Player;

import Item.Resource;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Resource> resources;

    public Inventory() {
        this.resources = new ArrayList<>();
    }

    // Add a resource to the inventory
    public void addResource(Resource resource) {
        // Check if the inventory already contains the resource
        for (Resource existingResource : resources) {
            if (existingResource.getName().equals(resource.getName())) {
                // If found, update its quantity and return
                existingResource.setQuantity(existingResource.getQuantity() + resource.getQuantity());
                return;
            }
        }
        // If not found, add the resource to the inventory
        resources.add(resource);
    }

    // Remove a resource from the inventory
    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    // Get all resources in the inventory
    public List<Resource> getResources() {
        return resources;
    }

    // Get the quantity of a specific resource in the inventory
    public int getResourceQuantity(String resourceName) {
        int totalQuantity = 0;
        for (Resource resource : resources) {
            if (resource.getName().equals(resourceName)) {
                totalQuantity += resource.getQuantity();
            }
        }
        return totalQuantity;
    }

    // Get a specific resource from the inventory
    public Resource getResource(String resourceName) {
        for (Resource resource : resources) {
            if (resource.getName().equals(resourceName)) {
                return resource;
            }
        }
        return null; // Resource not found
    }

    // Check if the inventory contains a specific resource
    public boolean containsResource(String resourceName) {
        for (Resource resource : resources) {
            if (resource.getName().equals(resourceName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Resource resource : resources) {
            sb.append(resource.toString()).append("\n");
        }
        return sb.toString();
    }
}
