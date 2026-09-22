package supermarket;

import supermarket.Models.*;
import java.io.*;
import java.util.StringTokenizer;

public class SupermarketAPI {
    private Supermarket supermarket;

    public SupermarketAPI() {
        this.supermarket = new Supermarket();
    }

    public Supermarket getSupermarket() {
        return supermarket;
    }

    // save to file
    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            Supermarket sm = supermarket;

            // save Floor Areas
            for (int i = 0; i < sm.getFloorAreas().size(); i++) {
                FloorArea area = sm.getFloorAreas().get(i);
                writer.println("FLOORAREA:" + area.getTitle() + ":" + area.getLevel());

                // save Aisles
                for (int j = 0; j < area.getAisles().size(); j++) {
                    Aisle aisle = area.getAisles().get(j);
                    writer.println("AISLE:" + aisle.getAisleName() + ":" +
                            aisle.getLength() + ":" + aisle.getWidth() + ":" +
                            aisle.getTemperature());

                    // save Shelves
                    for (int k = 0; k < aisle.getShelves().size(); k++) {
                        Shelf shelf = aisle.getShelves().get(k);
                        writer.println("SHELF:" + shelf.getShelfNumber());

                        // save GoodItems
                        for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                            GoodItem item = shelf.getGoodItems().get(l);
                            writer.println("GOODITEM:" + item.getDescription() + ":" +
                                    item.getSize() + ":" + item.getUnitPrice() + ":" +
                                    item.getQuantity() + ":" + item.getStorageTemperature() + ":" +
                                    item.getPhotoUrl());
                        }
                    }
                }
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        supermarket.reset();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            FloorArea currentArea = null;
            Aisle currentAisle = null;
            Shelf currentShelf = null;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                StringTokenizer tokenizer = new StringTokenizer(line, ":");
                if (!tokenizer.hasMoreTokens()) continue;
                String type = tokenizer.nextToken();

                switch (type) {
                    case "FLOORAREA":
                        if (tokenizer.countTokens() >= 2) {
                            String title = tokenizer.nextToken();
                            String level = tokenizer.nextToken();
                            currentArea = new FloorArea(title, level);
                            supermarket.getFloorAreas().add(currentArea);
                        }
                        break;

                    case "AISLE":
                        if (currentArea != null && tokenizer.countTokens() >= 4) {
                            String aisleName = tokenizer.nextToken();
                            double length = Double.parseDouble(tokenizer.nextToken());
                            double width = Double.parseDouble(tokenizer.nextToken());
                            String temp = tokenizer.nextToken();

                            currentAisle = new Aisle(aisleName, length, width, temp);
                            currentArea.getAisles().add(currentAisle);
                        }
                        break;

                    case "SHELF":
                        if (currentAisle != null && tokenizer.hasMoreTokens()) {
                            int shelfNum = Integer.parseInt(tokenizer.nextToken());
                            currentShelf = new Shelf(shelfNum);
                            currentAisle.getShelves().add(currentShelf);
                        }
                        break;

                    case "GOODITEM":
                        if (currentShelf != null && tokenizer.countTokens() >= 5) {
                            String desc = tokenizer.nextToken();
                            double size = Double.parseDouble(tokenizer.nextToken());
                            double price = Double.parseDouble(tokenizer.nextToken());
                            int quantity = Integer.parseInt(tokenizer.nextToken());
                            String itemTemp = tokenizer.nextToken();

                            GoodItem item = new GoodItem(desc, size, price, quantity, itemTemp, "");
                            currentShelf.addGoodItem(item);
                        }
                        break;
                }
            }
        }
    }

    // sample data for future use in the system
    public void createSampleData() {
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addFloorArea("Bakery", "Ground Floor");
        supermarket.addFloorArea("Frozen Foods", "Ground Floor");

        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addAisle("Dairy", "Cheese Aisle", 8.0, 4.0, "Refrigerated");
        supermarket.addAisle("Bakery", "Bread Aisle", 12.0, 6.0, "Unrefrigerated");
        supermarket.addAisle("Frozen Foods", "Ice Cream Aisle", 6.0, 3.0, "Frozen");

        supermarket.addShelf("Milk Aisle", 1);
        supermarket.addShelf("Milk Aisle", 2);
        supermarket.addShelf("Cheese Aisle", 1);
        supermarket.addShelf("Bread Aisle", 1);
        supermarket.addShelf("Ice Cream Aisle", 1);


        supermarket.addGoodItem("Milk Aisle", 1,
                new GoodItem("Fresh Milk", 1000, 2.99, 25, "Refrigerated","" ));
        supermarket.addGoodItem("Milk Aisle", 1,
                new GoodItem("Yogurt", 500, 1.49, 30, "Refrigerated", ""));
        supermarket.addGoodItem("Milk Aisle", 2,
                new GoodItem("Butter", 250, 3.49, 20, "Refrigerated", ""));
        supermarket.addGoodItem("Cheese Aisle", 1,
                new GoodItem("Cheddar Cheese", 200, 4.99, 15, "Refrigerated", ""));
        supermarket.addGoodItem("Bread Aisle", 1,
                new GoodItem("Whole Wheat Bread", 400, 1.79, 20, "Unrefrigerated", ""));
        supermarket.addGoodItem("Bread Aisle", 1,
                new GoodItem("Croissants", 100, 0.99, 25, "Unrefrigerated", ""));
        supermarket.addGoodItem("Ice Cream Aisle", 1,
                new GoodItem("Vanilla Ice Cream", 500, 4.49, 12, "Frozen", ""));
    }
}