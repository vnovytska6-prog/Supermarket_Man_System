package supermarket.Models;

public class FloorArea {
    private String title;
    private String level;
    private LinkedList<Aisle> aisles;

    public FloorArea(String title, String level) {
        this.title = title;
        this.level = level;
        this.aisles = new LinkedList<>();
    }

    public String getTitle() { return title; }
    public String getLevel() { return level; }
    public LinkedList<Aisle> getAisles() { return aisles; }

    public void addAisle(Aisle aisle) {
        aisles.add(aisle);
    }

    public Aisle getAisle(String aisleName) {
        return aisles.find(new LinkedList.Condition<Aisle>() {
            @Override
            public boolean test(Aisle aisle) {
                return aisle.getAisleName().equals(aisleName);
            }
        });
    }

    public double getTotalValue() {
        double total = 0;
        for (int i = 0; i < aisles.size(); i++) {
            total += aisles.get(i).getTotalValue();
        }
        return total;
    }

    public int getTotalAisles() {
        return aisles.size();
    }

    @Override
    public String toString() {
        return String.format("Floor Area '%s': %d aisles = €%.2f",
                title, getTotalAisles(), getTotalValue());
    }
}
