package project.persistence.dbRestUtils;

/**
 * Created by leo on 23.2.2016.
 *
 * Data structure representing a projectile.
 */
public class Projectile extends Item {
    private int quantity;

    //<editor-fold desc="Getters and Setters">
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //</editor-fold>
}
