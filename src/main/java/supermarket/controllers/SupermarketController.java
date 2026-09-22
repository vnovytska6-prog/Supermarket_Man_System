package supermarket.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.control.ScrollPane;
import supermarket.Models.*;
import supermarket.SupermarketAPI;

public class SupermarketController {

    private SupermarketAPI api;

    @FXML
    private TreeView<String> supermarketTree;
    @FXML
    private TextArea displayArea;
    @FXML
    private TextField floorAreaTitle;
    @FXML
    private TextField floorAreaLevel;
    @FXML
    private TextField aisleName;
    @FXML
    private TextField aisleLength;
    @FXML
    private TextField aisleWidth;
    @FXML
    private ComboBox<String> aisleTemperature;
    @FXML
    private TextField shelfNumber;
    @FXML
    private TextField itemDescription;
    @FXML
    private TextField itemSize;
    @FXML
    private TextField itemPrice;
    @FXML
    private TextField itemQuantity;
    @FXML
    private ComboBox<String> itemTemperature;
    @FXML
    private TextField itemPhotoUrl;
    @FXML
    private TextField searchField;
    @FXML
    private TextField removeItemDesc;
    @FXML
    private TextField removeItemSize;
    @FXML
    private TextField removeQuantity;
    @FXML
    private Label statusLabel;
    @FXML
    private VBox formsContainer;
    @FXML
    private VBox floorAreaForm, aisleForm, shelfForm, itemForm, searchForm, removeForm, mapContainer;
    @FXML
    private ComboBox<String> aisleAreaCombo, shelfAreaCombo, shelfAisleCombo, itemAreaCombo, itemAisleCombo, itemShelfCombo;
    @FXML
    private VBox storeMap;

    // Form Management
    private void showOnlyForm(VBox formToShow) {
        floorAreaForm.setVisible(false);
        aisleForm.setVisible(false);
        shelfForm.setVisible(false);
        itemForm.setVisible(false);
        searchForm.setVisible(false);
        removeForm.setVisible(false);
        mapContainer.setVisible(false);

        if (formToShow != null) {
            formToShow.setVisible(true);
            formToShow.setManaged(true);
        }
    }

    @FXML
    private void showStoreMap() {
        showOnlyForm(mapContainer);
        updateStoreMap();
        displayArea.setText("Store Map View\n\nNavigate through supermarket structure");
    }

    @FXML
    private void showSearchForm() {
        showOnlyForm(searchForm);
        displayArea.setText("Search Items\n\nSearch from below to find item by name");
    }

    @FXML
    private void showRemoveForm() {
        showOnlyForm(removeForm);
        displayArea.setText("Remove Items\n\nUse the remove to delete items");
    }

    @FXML
    private void showFloorAreaForm() {
        showOnlyForm(floorAreaForm);
        displayArea.setText("Add Floor Area\n\nAdd a new floor area");
    }

    @FXML
    private void showAisleForm() {
        showOnlyForm(aisleForm);
        displayArea.setText("Add Aisle\n\nAdd a new aisle to a floor area");
    }

    @FXML
    private void showShelfForm() {
        showOnlyForm(shelfForm);
        displayArea.setText("Add Shelf\n\nAdd a new shelf to an aisle");
    }

    @FXML
    private void showItemForm() {
        showOnlyForm(itemForm);
        displayArea.setText("Add Item\n\nAdd a new item to a shelf");
    }

    // INITIALISATION PROCESS
    @FXML
    public void initialize() {
        api = new SupermarketAPI();

        aisleTemperature.getItems().addAll("Unrefrigerated", "Refrigerated", "Frozen");
        itemTemperature.getItems().addAll("Unrefrigerated", "Refrigerated", "Frozen");

        displayArea.setText("Welcome to Supermarket Management System!\n\n" +
                "Use the navigation panel to manage your supermarket:\n\n" +
                "• 🏬 Add Floor Areas\n" +
                "• 🛒 Add Aisles to floor areas\n" +
                "• 📚 Add Shelves to aisles\n" +
                "• 📦 Add Items to shelves\n" +
                "• 🔍 Search for items by name\n" +
                "• 🗑️ Remove items\n" +
                "• 🗺️ View Store Map\n" +
                "• 📊 View All Stock\n\n" +
                "Start managing your supermarket!");
    }

    private void updateEverything() {
        updateComboboxes();
        updateSupermarketTree();
        updateStoreMap();
    }

    // COMBOBOX UPDATES
    private void updateComboboxes() {
        Supermarket supermarket = api.getSupermarket();

        if (aisleAreaCombo != null) {
            aisleAreaCombo.getItems().clear();
            for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
                String areaTitle = supermarket.getFloorAreas().get(i).getTitle();
                aisleAreaCombo.getItems().add(areaTitle);
            }
        }

        if (shelfAreaCombo != null) {
            shelfAreaCombo.getItems().clear();
            for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
                String areaTitle = supermarket.getFloorAreas().get(i).getTitle();
                shelfAreaCombo.getItems().add(areaTitle);
            }
        }

        if (itemAreaCombo != null) {
            itemAreaCombo.getItems().clear();
            for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
                String areaTitle = supermarket.getFloorAreas().get(i).getTitle();
                itemAreaCombo.getItems().add(areaTitle);
            }
        }

        if (shelfAisleCombo != null) {
            shelfAisleCombo.getItems().clear();
            for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
                FloorArea area = supermarket.getFloorAreas().get(i);
                for (int j = 0; j < area.getAisles().size(); j++) {
                    String aisleName = area.getAisles().get(j).getAisleName();
                    shelfAisleCombo.getItems().add(aisleName);
                }
            }
        }

        if (itemAisleCombo != null) {
            itemAisleCombo.getItems().clear();
            for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
                FloorArea area = supermarket.getFloorAreas().get(i);
                for (int j = 0; j < area.getAisles().size(); j++) {
                    String aisleName = area.getAisles().get(j).getAisleName();
                    itemAisleCombo.getItems().add(aisleName);
                }
            }
        }

        if (itemShelfCombo != null) {
            itemShelfCombo.getItems().clear();
            for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
                FloorArea area = supermarket.getFloorAreas().get(i);
                for (int j = 0; j < area.getAisles().size(); j++) {
                    Aisle aisle = area.getAisles().get(j);
                    for (int k = 0; k < aisle.getShelves().size(); k++) {
                        int shelfNum = aisle.getShelves().get(k).getShelfNumber();
                        itemShelfCombo.getItems().add("Shelf " + shelfNum);
                    }
                }
            }
        }
    }

    // STORE MAP
    private void updateStoreMap() {
        if (storeMap == null) return;
        storeMap.getChildren().clear();
        Supermarket supermarket = api.getSupermarket();

        for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
            FloorArea area = supermarket.getFloorAreas().get(i);
            VBox areaBox = new VBox(10);
            areaBox.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #34495e; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 15px;");

            Label areaLabel = new Label("🏬 " + area.getTitle() + " (Level: " + area.getLevel() + ")");
            areaLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            HBox aislesBox = new HBox(15);

            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                VBox aisleBox = new VBox(8);
                aisleBox.setStyle("-fx-background-color: white; -fx-border-color: #7f8c8d; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px;");
                aisleBox.setPrefWidth(180);

                Label aisleLabel = new Label("🛒 " + aisle.getAisleName());
                aisleLabel.setStyle("-fx-font-weight: bold;");

                Label tempLabel = new Label(aisle.getTemperature());
                String tempColor = "";
                switch (aisle.getTemperature().toLowerCase()) {
                    case "unrefrigerated":
                        tempColor = "#27ae60";
                        break;
                    case "refrigerated":
                        tempColor = "#3498db";
                        break;
                    case "frozen":
                        tempColor = "#9b59b6";
                        break;
                }
                tempLabel.setStyle("-fx-background-color: " + tempColor + "; -fx-text-fill: white; -fx-padding: 2px 6px; -fx-border-radius: 3px; -fx-background-radius: 3px; -fx-font-size: 10px;");

                Label dimLabel = new Label(aisle.getLength() + "m × " + aisle.getWidth() + "m");
                dimLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");
                VBox shelvesBox = new VBox(3);


                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    HBox shelfBox = new HBox(5);
                    shelfBox.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-border-radius: 3px; -fx-background-radius: 3px; -fx-padding: 5px;");

                    Label shelfLabel = new Label("📚 Shelf " + shelf.getShelfNumber());
                    Label itemsLabel = new Label("(" + shelf.getGoodItems().size() + " items)");
                    itemsLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-size: 10px;");

                    shelfBox.getChildren().addAll(shelfLabel, itemsLabel);
                    shelvesBox.getChildren().add(shelfBox);
                }

                aisleBox.getChildren().addAll(aisleLabel, tempLabel, dimLabel, shelvesBox);
                aislesBox.getChildren().add(aisleBox);
            }

            areaBox.getChildren().addAll(areaLabel, aislesBox);
            storeMap.getChildren().add(areaBox);
        }

        if (supermarket.getFloorAreas().size() == 0) {
            Label noDataLabel = new Label("No floor areas added yet. Start by adding a floor area!");
            noDataLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            storeMap.getChildren().add(noDataLabel);
        }
        makeMapInteractive();
    }

    // Interactive map
    private void makeMapInteractive() {
        if (storeMap == null) return;

        for (javafx.scene.Node node : storeMap.getChildren()) {
            if (node instanceof VBox) {
                VBox areaBox = (VBox) node;

                // pointing will be light blue color
                areaBox.setOnMouseEntered(e -> {
                    areaBox.setStyle("-fx-background-color: #e8f4fc; " + "-fx-border-color: #3498db; " +
                            "-fx-cursor: hand;");
                });

                // return previous color
                areaBox.setOnMouseExited(e -> {
                    areaBox.setStyle("-fx-background-color: #ecf0f1; " + "-fx-border-color: #34495e;");
                });

                // when click will show area name
                areaBox.setOnMouseClicked(e -> {
                    Label label = (Label) areaBox.getChildren().get(0);
                    String name = label.getText();
                    displayArea.setText("Selected: " + name);
                });
            }
        }
    }

    // ACTIONS
    @FXML
    private void setupSampleData() {
        api.createSampleData();
        updateEverything();
        displayArea.setText("Sample data created successfully!\n\n" +
                api.getSupermarket().viewAllStock());
    }

    @FXML
    private void addFloorArea() {
        String title = floorAreaTitle.getText();
        String level = floorAreaLevel.getText();

        if (!title.isEmpty() && !level.isEmpty()) {
            if (api.getSupermarket().addFloorArea(title, level)) {
                displayArea.setText("Floor area added: " + title + " on " + level);
                floorAreaTitle.clear();
                floorAreaLevel.clear();
                updateEverything();
            }
        } else {
            displayArea.setText("Please enter both title and level");
        }
    }

    @FXML
    private void addAisle() {
        String areaTitle = aisleAreaCombo != null ? aisleAreaCombo.getValue() : "Dairy";
        String name = aisleName.getText();

        try {
            double length = Double.parseDouble(aisleLength.getText());
            double width = Double.parseDouble(aisleWidth.getText());
            String temp = aisleTemperature.getValue();

            if (name != null && !name.isEmpty() && temp != null && areaTitle != null) {
                if (api.getSupermarket().addAisle(areaTitle, name, length, width, temp)) {
                    displayArea.setText("Aisle added: " + name + " to " + areaTitle);
                    clearAisleFields();
                    updateEverything();
                } else {
                    displayArea.setText("Error: Aisle name must be unique or floor area not found");
                }
            } else {
                displayArea.setText("Please fill all fields");
            }
        } catch (NumberFormatException e) {
            displayArea.setText("Please enter valid numbers for dimensions");
        }
    }

    // Remove aisle
    @FXML private TextField removeAisleField;

    @FXML
    private void removeAisle() {
        String aisleName = removeAisleField.getText();

        if (!aisleName.isEmpty()) {
            if (api.getSupermarket().removeAisle(aisleName)) {
                displayArea.setText("Aisle '" + aisleName + "' removed successfully!");
                removeAisleField.clear();
                updateEverything();
            } else {
                displayArea.setText("Aisle '" + aisleName + "' not found");
            }
        } else {
            displayArea.setText("Please enter an aisle name to remove");
        }
    }

    @FXML
    private void addShelf() {
        String areaTitle = shelfAreaCombo != null ? shelfAreaCombo.getValue() : "Dairy";
        String aisleName = shelfAisleCombo != null ? shelfAisleCombo.getValue() : "Milk Aisle";

        try {
            int number = Integer.parseInt(shelfNumber.getText());
            if (api.getSupermarket().addShelf(aisleName, number)) {
                displayArea.setText("Shelf " + number + " added to " + aisleName + " in " + areaTitle);
                shelfNumber.clear();
                updateEverything();
            } else {
                displayArea.setText("Error: Aisle not found or shelf number already exists");
            }
        } catch (NumberFormatException e) {
            displayArea.setText("Please enter a valid shelf number");
        }
    }

    @FXML
    private void addGoodItem() {
        String areaTitle = itemAreaCombo != null ? itemAreaCombo.getValue() : "Dairy";
        String aisleName = itemAisleCombo != null ? itemAisleCombo.getValue() : "Milk Aisle";
        String shelfText = itemShelfCombo != null ? itemShelfCombo.getValue() : "Shelf 1";

        try {
            int shelfNumber = Integer.parseInt(shelfText.replace("Shelf ", ""));
            String desc = itemDescription.getText();
            double size = Double.parseDouble(itemSize.getText());
            double price = Double.parseDouble(itemPrice.getText());
            int quantity = Integer.parseInt(itemQuantity.getText());
            String temp = itemTemperature.getValue();

            if (!desc.isEmpty() && temp != null) {
                GoodItem item = new GoodItem(desc, size, price, quantity, temp, "");
                if (api.getSupermarket().addGoodItem(aisleName, shelfNumber, item)) {
                    displayArea.setText("Item added: " + desc + " to " + areaTitle + " - " + aisleName + " - Shelf " + shelfNumber);
                    clearItemFields();
                    updateEverything();
                } else {
                    displayArea.setText("Error: Could not add item - check aisle and shelf exist");
                }
            } else {
                displayArea.setText("Please fill description and temperature");
            }
        } catch (NumberFormatException e) {
            displayArea.setText("Please check your input values (size, price, quantity should be numbers)");
        }
    }

    @FXML
    private void viewAllStock() {
        displayArea.setText(api.getSupermarket().viewAllStock());
    }

    @FXML
    private void searchItems() {
        String term = searchField.getText();
        if (!term.isEmpty()) {
            displayArea.setText(api.getSupermarket().searchGoodItems(term));
            searchField.clear();
        } else {
            displayArea.setText("Please enter a search term");
        }
    }

    @FXML
    private void smartAddItem() {
        try {
            String desc = itemDescription.getText();
            double size = Double.parseDouble(itemSize.getText());
            double price = Double.parseDouble(itemPrice.getText());
            int quantity = Integer.parseInt(itemQuantity.getText());
            String temp = itemTemperature.getValue();
            String photoUrl = itemPhotoUrl.getText();

            if (!desc.isEmpty() && temp != null) {
                GoodItem item = new GoodItem(desc, size, price, quantity, temp, photoUrl);
                String result = api.getSupermarket().smartAddGoodItem(item);
                displayArea.setText("🧠 Smart Add Result: " + result);
                clearItemFields();
                updateEverything();
            } else {
                displayArea.setText("Please fill description and temperature");
            }
        } catch (NumberFormatException e) {
            displayArea.setText("Please check your input values");
        }
    }

    @FXML
    private void removeItem() {
        try {
            String desc = removeItemDesc.getText();
            double size = Double.parseDouble(removeItemSize.getText());
            int quantity = Integer.parseInt(removeQuantity.getText());

            if (!desc.isEmpty()) {
                if (api.getSupermarket().removeGoodItem(desc, size, quantity)) {
                    displayArea.setText("Item removed: " + desc);
                    removeItemDesc.clear();
                    removeItemSize.clear();
                    removeQuantity.clear();
                    updateEverything();
                } else {
                    displayArea.setText("Item not found or insufficient quantity");
                }
            } else {
                displayArea.setText("Please enter item description");
            }
        } catch (NumberFormatException e) {
            displayArea.setText("Please enter valid size and quantity numbers");
        }
    }

    @FXML
    private void saveData() {
        try {
            api.saveToFile("supermarket_data.txt");
            displayArea.setText("💾 Data saved successfully!");
        } catch (Exception e) {
            displayArea.setText("Error saving data: " + e.getMessage());
        }
    }

    @FXML
    private void loadData() {
        try {
            api.loadFromFile("supermarket_data.txt");
            displayArea.setText("📂 Data loaded successfully!\n\n" +
                    api.getSupermarket().viewAllStock());
            updateEverything();
        } catch (Exception e) {
            displayArea.setText("Error loading data: " + e.getMessage());
        }
    }

    @FXML
    private void resetSystem() {
        api.getSupermarket().reset();
        displayArea.setText("🔄 System reset successfully!");
        updateEverything();
    }

    //HELPER METHODS
    private void clearAisleFields() {
        aisleName.clear();
        aisleLength.clear();
        aisleWidth.clear();
        aisleTemperature.setValue(null);
    }

    private void clearItemFields() {
        itemDescription.clear();
        itemSize.clear();
        itemPrice.clear();
        itemQuantity.clear();
        itemTemperature.setValue(null);
        itemPhotoUrl.clear();
    }

    // TREE VIEW
    private void updateSupermarketTree() {
        TreeItem<String> root = new TreeItem<>("Supermarket");
        root.setExpanded(true);
        Supermarket supermarket = api.getSupermarket();

        for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
            FloorArea area = supermarket.getFloorAreas().get(i);
            TreeItem<String> areaNode = new TreeItem<>(area.getTitle() + " (" + area.getLevel() + ")");
            areaNode.setExpanded(true);

            for (int j = 0; j < area.getAisles().size(); j++) {
                Aisle aisle = area.getAisles().get(j);
                TreeItem<String> aisleNode = new TreeItem<>(
                        aisle.getAisleName() + " - " + aisle.getTemperature() +
                                " [" + aisle.getLength() + "x" + aisle.getWidth() + "m]"
                );
                aisleNode.setExpanded(true);

                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    TreeItem<String> shelfNode = new TreeItem<>("Shelf " + shelf.getShelfNumber() +
                            " (" + shelf.getGoodItems().size() + " items)");
                    shelfNode.setExpanded(true);

                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem item = shelf.getGoodItems().get(l);
                        TreeItem<String> itemNode = new TreeItem<>(
                                item.getDescription() + " (" + item.getSize() + "g) - " +
                                        item.getQuantity() + " x €" + item.getUnitPrice() + " = €" +
                                        String.format("%.2f", item.getTotalValue())
                        );
                        shelfNode.getChildren().add(itemNode);
                    }
                    aisleNode.getChildren().add(shelfNode);
                }
                areaNode.getChildren().add(aisleNode);
            }
            root.getChildren().add(areaNode);
        }

        supermarketTree.setRoot(root);
    }
}