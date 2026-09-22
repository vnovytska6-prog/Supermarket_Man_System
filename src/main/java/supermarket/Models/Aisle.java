package supermarket.Models;

public class Aisle {
    private String aisleName;
    private double length;
    private double width;
    private String temperature;
    private LinkedList<Shelf> shelves;

    public Aisle(String aisleName, double length, double width, String temperature) {
        this.aisleName = aisleName;
        this.length = length;
        this.width = width;
        this.temperature = temperature;
        this.shelves = new LinkedList<>();
    }

    public String getAisleName() { return aisleName; }
    public double getLength() { return length; }
    public double getWidth() { return width; }
    public String getTemperature() { return temperature; }
    public LinkedList<Shelf> getShelves() { return shelves; }

    public void addShelf(Shelf shelf) {
        shelves.add(shelf);
    }

    public Shelf getShelf(int shelfNumber) {
        return shelves.find(new LinkedList.Condition<Shelf>() {
            @Override
            public boolean test(Shelf shelf) {
                return shelf.getShelfNumber() == shelfNumber;
            }
        });
    }

    public double getTotalValue() {
        double total = 0;
        for (int i = 0; i < shelves.size(); i++) {
            total += shelves.get(i).getTotalValue();
        }
        return total;
    }

    public int getTotalShelves() {
        return shelves.size();
    }

    public int getTotalItems() {
        int total = 0;
        for (int i = 0; i < shelves.size(); i++) {
            total += shelves.get(i).getTotalItemsCount();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Aisle '%s': %d shelves = €%.2f",
                aisleName, getTotalShelves(), getTotalValue());
    }
}