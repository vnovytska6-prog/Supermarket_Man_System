package supermarket.Models;

public class GoodItem {
    private String description;
    private double size;
    private double unitPrice;
    private int quantity;
    private String storageTemperature;
    private String photoUrl;

    public GoodItem(String description, double size, double unitPrice,
                    int quantity, String storageTemperature, String photoUrl) {
        this.description = description;
        this.size = size;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.storageTemperature = storageTemperature;
        this.photoUrl = photoUrl;
    }

    // getter
    public String getDescription() { return description; }
    public double getSize() { return size; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public String getStorageTemperature() { return storageTemperature;}
    public String getPhotoUrl() {return photoUrl;}

    // setter
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public double getTotalValue() {
        return quantity * unitPrice;
    }

    public boolean isSameItem(GoodItem other) {
        return this.description.equals(other.description) &&
                this.size == other.size;
    }

    @Override
    public String toString() {
        return String.format("%s (%.0f): %d @ €%.2f = €%.2f",
                description, size, quantity, unitPrice, getTotalValue());
    }
}
