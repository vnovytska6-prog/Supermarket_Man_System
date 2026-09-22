package supermarket.Models;

public class Supermarket {
    private LinkedList<FloorArea> floorAreas;

    public Supermarket() { this.floorAreas = new LinkedList<>(); }

    public LinkedList<FloorArea> getFloorAreas() { return floorAreas;}

    // floor area
    public boolean addFloorArea(String title, String level) {
        floorAreas.add(new FloorArea(title, level));
        return true;
    }

    //aisle
    public boolean addAisle(String floorAreaTitle, String aisleName,
                            double length, double width, String temperature) {
        if (isAisleNameExists(aisleName)) {
            return false;
        }

        FloorArea area = floorAreas.find(new LinkedList.Condition<FloorArea>() {
            @Override
            public boolean test(FloorArea fa) {
                return fa.getTitle().equals(floorAreaTitle);
            }
        });
        if (area != null) {
            area.addAisle(new Aisle(aisleName, length, width, temperature));
            return true;
        }
        return false;
    }

    private boolean isAisleNameExists(String aisleName) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            }
        return false;
    }

    // remove aisle
    public boolean removeAisle(String aisleName) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                if (aisle.getAisleName().equals(aisleName)) {
                    area.getAisles().remove(j);
                    return true;
                }
            }
        }
        return false;
    }


    // shelf
    public boolean addShelf(String aisleName, int shelfNumber) {
        Aisle aisle = findAisleByName(aisleName);
        if (aisle != null && aisle.getShelf(shelfNumber) == null) {
            aisle.addShelf(new Shelf(shelfNumber));
            return true;
        }
        return false;
    }

    // goodItem
    public boolean addGoodItem(String aisleName, int shelfNumber, GoodItem item) {
        Aisle aisle = findAisleByName(aisleName);
        if (aisle != null) {
            Shelf shelf = aisle.getShelf(shelfNumber);
            if (shelf != null) {
                shelf.addGoodItem(item);
                return true;
            }
        }
        return false;
    }

    // View all Stock (item search)
    public String viewAllStock() {
        StringBuilder sb = new StringBuilder();
        double supermarketTotal = 0;

        sb.append("=== Supermarket Stock Report ===\n\n");

        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            sb.append(area.toString()).append("\n");
            double areaTotal = area.getTotalValue();

            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                sb.append("  ").append(aisle.toString()).append("\n");
                double aisleTotal = aisle.getTotalValue();

                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    sb.append("    ").append(shelf.toString()).append("\n");
                    double shelfTotal = shelf.getTotalValue();

                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem item = shelf.getGoodItems().get(l);
                        sb.append("      ").append(item.toString()).append("\n");
                    }
                }
                areaTotal += aisleTotal;
            }
            supermarketTotal += areaTotal;
            sb.append("\n");
        }

        sb.append(String.format("Supermarket Total: €%.2f", supermarketTotal));
        return sb.toString();
    }

    // Search items
    public String searchGoodItems(String searchTerm) {
        StringBuilder sb = new StringBuilder();
        sb.append("Search results for: '").append(searchTerm).append("'\n\n");
        boolean found = false;

        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem item = shelf.getGoodItems().get(l);
                        if (item.getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {
                            sb.append("• ").append(item.getDescription()).append("\n");
                            sb.append("  Location: ").append(area.getTitle())
                                    .append(" → ").append(aisle.getAisleName())
                                    .append(" → Shelf ").append(shelf.getShelfNumber()).append("\n");
                            sb.append("  Quantity: ").append(item.getQuantity())
                                    .append(", Price: €").append(item.getUnitPrice()).append("\n\n");
                            found = true;
                        }
                    }
                }
            }
        }

        if (!found) {
            sb.append("No items found");
        }
        return sb.toString();
    }

    // SmartADD
    public String smartAddGoodItem(GoodItem newItem) {
        // find existing items
        GoodItem existingItem = findExistingItem(newItem);
        if (existingItem != null) {
            // quantity existing item
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());

            // updating price if it changed
            if (existingItem.getUnitPrice() != newItem.getUnitPrice()) {
                existingItem.setUnitPrice(newItem.getUnitPrice());
            }

            // location where it is
            Shelf shelf = findShelfOfItem(existingItem);
            Aisle aisle = findAisleOfShelf(shelf);
            FloorArea area = findFloorAreaOfAisle(aisle);

            return String.format("Added to existing item at: %s → %s → Shelf %d",
                    area.getTitle(), aisle.getAisleName(), shelf.getShelfNumber());
        }

        // if item isnt found, find aisle through the temperature
        Aisle suitableAisle = findSuitableAisle(newItem.getStorageTemperature());
        if (suitableAisle != null) {
            // use first shelf in that aisle or create new
            Shelf shelf = suitableAisle.getShelves().size() > 0 ?
                    suitableAisle.getShelves().get(0) : null;
            if (shelf == null) {
                shelf = new Shelf(1);
                suitableAisle.addShelf(shelf);
            }

            // add item on shelf
            shelf.addGoodItem(newItem);

            // floorarea for aisle
            FloorArea area = findFloorAreaOfAisle(suitableAisle);

            return String.format("Smart added to: %s → %s → Shelf %d",
                    area.getTitle(), suitableAisle.getAisleName(), shelf.getShelfNumber());
        }
        return "No suitable location found. Need an aisle with temperature: " + newItem.getStorageTemperature();
    }

    // find existing item
    private GoodItem findExistingItem(GoodItem target) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem item = shelf.getGoodItems().get(l);
                        if (item.isSameItem(target)) {
                            return item;  // Возвращаем САМ ТОВАР
                        }
                    }
                }
            }
        }
        return null;
    }

    // find shelf with the item we are looking for
    private Shelf findShelfOfItem(GoodItem targetItem) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        if (shelf.getGoodItems().get(l) == targetItem) {
                            return shelf;
                        }
                    }
                }
            }
        }
        return null;
    }

    // find floor area for aisle
    private FloorArea findFloorAreaOfAisle(Aisle targetAisle) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                if (area.getAisles().get(j) == targetAisle) {
                    return area;
                }
            }
        }
        return null;
    }

    private Aisle findSuitableAisle(String temperature) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                if (aisle.getTemperature().equals(temperature)) {
                    return aisle;
                }
            }
        }
        return null;
    }

    // remove item
    public boolean removeGoodItem(String description, double size, int quantity) {
        GoodItem target = new GoodItem(description, size, 0, quantity, "", "");
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    if (shelf.removeGoodItem(target, quantity)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // reset
    public void reset() {
        floorAreas = new LinkedList<>();
    }

    private Aisle findAisleByName(String aisleName) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            Aisle aisle = area.getAisle(aisleName);
            if (aisle != null) {
                return aisle;
            }
        }
        return null;
    }

    private Aisle findAisleOfShelf(Shelf targetShelf) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea area = floorAreas.get(i);
            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    if (aisle.getShelves().get(k) == targetShelf) {
                        return aisle;
                    }
                }
            }
        }
        return null;
    }
}