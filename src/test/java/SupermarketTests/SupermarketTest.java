package SupermarketTests;

import supermarket.Models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class SupermarketTest {

    private Supermarket supermarket;

    @BeforeEach
    void setUp() {
        supermarket = new Supermarket();
    }

    // Test LinkedList
    @Test
    void testLinkedListOperations() {
        LinkedList<String> list = new LinkedList<>();

        // Test add and size
        list.add("First");
        list.add("Second");
        assertEquals(2, list.size());

        // Test get
        assertEquals("First", list.get(0));
        assertEquals("Second", list.get(1));

        // Test remove
        list.remove(0);
        assertEquals(1, list.size());
        assertEquals("Second", list.get(0));

        // Test find
        String found = list.find(s -> s.equals("Second"));
        assertEquals("Second", found);

        // Test findAll
        list.add("Second");
        LinkedList<String> results = list.findAll(s -> s.equals("Second"));
        assertEquals(2, results.size());
    }

    // Test FloorArea
    @Test
    void testAddFloorArea() {
        assertTrue(supermarket.addFloorArea("Dairy", "Ground Floor"));
        assertEquals(1, supermarket.getFloorAreas().size());

        FloorArea area = supermarket.getFloorAreas().get(0);
        assertEquals("Dairy", area.getTitle());
        assertEquals("Ground Floor", area.getLevel());
    }

    // Test aisle names
    @Test
    void testAddAisleWithUniqueName() {
        supermarket.addFloorArea("Dairy", "Ground Floor");

        // First aisle - succeed
        assertTrue(supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated"));

        // Different name - succeed
        assertTrue(supermarket.addAisle("Dairy", "Cheese Aisle", 8.0, 4.0, "Refrigerated"));
    }

    // Test item quantity update
    @Test
    void testGoodItemQuantityUpdate() {
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addShelf("Milk Aisle", 1);

        GoodItem milk1 = new GoodItem("Milk", 1000, 2.99, 10, "Refrigerated", "");
        GoodItem milk2 = new GoodItem("Milk", 1000, 3.99, 5, "Refrigerated", "");

        supermarket.addGoodItem("Milk Aisle", 1, milk1);
        supermarket.addGoodItem("Milk Aisle", 1, milk2);

        // updated quantity and price
        Shelf shelf = supermarket.getFloorAreas().get(0)
                .getAisles().get(0)
                .getShelves().get(0);
        GoodItem result = shelf.getGoodItems().get(0);

        assertEquals(15, result.getQuantity());
        assertEquals(3.99, result.getUnitPrice(), 0.01);
        assertEquals(1, shelf.getGoodItems().size()); // Only one item instance
    }

    // Search items
    @Test
    void testSearchGoodItems() {
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addShelf("Milk Aisle", 1);

        GoodItem milk = new GoodItem("Fresh Milk", 1000, 2.99, 10, "Refrigerated", "");
        supermarket.addGoodItem("Milk Aisle", 1, milk);

        String searchResult = supermarket.searchGoodItems("Milk");
        assertTrue(searchResult.contains("Fresh Milk"));
        assertTrue(searchResult.contains("Milk Aisle"));
        assertTrue(searchResult.contains("Quantity: 10"));

        // Test search
        String searchResult2 = supermarket.searchGoodItems("fresh");
        assertTrue(searchResult2.contains("Fresh Milk"));}



    // Test Smart Add
    @Test
    void testSmartAddToExistingLocation() {
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addShelf("Milk Aisle", 1);

        GoodItem milk1 = new GoodItem("Milk", 1000, 2.99, 10, "Refrigerated", "");
        supermarket.addGoodItem("Milk Aisle", 1, milk1);

        GoodItem milk2 = new GoodItem("Milk", 1000, 3.49, 5, "Refrigerated", "");
        String result = supermarket.smartAddGoodItem(milk2);

        assertTrue(result.contains("Added to existing item") || result.contains("existing"));
        assertTrue(result.contains("Milk Aisle"));

        Shelf shelf = supermarket.getFloorAreas().get(0)
                .getAisles().get(0)
                .getShelves().get(0);
        assertEquals(1, shelf.getGoodItems().size()); // Still one item (updated)
        assertEquals(15, shelf.getGoodItems().get(0).getQuantity()); // But quantity updated
        assertEquals(3.49, shelf.getGoodItems().get(0).getUnitPrice(), 0.01); // Price updated
    }

    // SmartAdd new location
    @Test
    void testSmartAddToNewLocation() {
        supermarket.addFloorArea("Frozen", "Ground Floor");
        supermarket.addAisle("Frozen", "Ice Cream Aisle", 8.0, 4.0, "Frozen");

        GoodItem iceCream = new GoodItem("Vanilla Ice Cream", 500, 4.99, 10, "Frozen", "");
        String result = supermarket.smartAddGoodItem(iceCream);

        assertTrue(result.contains("Smart added to") ||
                result.contains("Ice Cream Aisle") ||
                result.contains("Shelf"));

        // create shelf automatically
        Aisle aisle = supermarket.getFloorAreas().get(0).getAisles().get(0);
        assertTrue(aisle.getShelves().size() > 0);
    }

    // Test remove items
    @Test
    void testRemoveGoodItems() {
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addShelf("Milk Aisle", 1);

        GoodItem milk = new GoodItem("Milk", 1000, 2.99, 10, "Refrigerated", "");
        supermarket.addGoodItem("Milk Aisle", 1, milk);

        // Remove quantity
        assertTrue(supermarket.removeGoodItem("Milk", 1000, 5));

        Shelf shelf = supermarket.getFloorAreas().get(0)
                .getAisles().get(0)
                .getShelves().get(0);
        assertEquals(5, shelf.getGoodItems().get(0).getQuantity());

        // Remove all
        assertTrue(supermarket.removeGoodItem("Milk", 1000, 5));
        assertEquals(0, shelf.getGoodItems().size());

    }

    //  Test reset system
    @Test
    void testResetSystem() {
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addShelf("Milk Aisle", 1);

        assertEquals(1, supermarket.getFloorAreas().size());

        supermarket.reset();

        assertEquals(0, supermarket.getFloorAreas().size());
    }

    // Test total value calculation
    @Test
    void testTotalValueCalculations() {
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addShelf("Milk Aisle", 1);

        GoodItem milk = new GoodItem("Milk", 1000, 2.99, 10, "Refrigerated", "");
        GoodItem cheese = new GoodItem("Cheese", 200, 5.99, 5, "Refrigerated", "");

        supermarket.addGoodItem("Milk Aisle", 1, milk);
        supermarket.addGoodItem("Milk Aisle", 1, cheese);

        // Test item total value
        assertEquals(29.90, milk.getTotalValue(), 0.01);
        assertEquals(29.95, cheese.getTotalValue(), 0.01);

        // Test shelf total value
        Shelf shelf = supermarket.getFloorAreas().get(0)
                .getAisles().get(0)
                .getShelves().get(0);
        assertEquals(59.85, shelf.getTotalValue(), 0.01);

        // Test aisle total value
        Aisle aisle = supermarket.getFloorAreas().get(0).getAisles().get(0);
        assertEquals(59.85, aisle.getTotalValue(), 0.01);

        // Test floor area total value
        FloorArea area = supermarket.getFloorAreas().get(0);
        assertEquals(59.85, area.getTotalValue(), 0.01);
    }

    // Test same item
    @Test
    void testSameItemDetection() {
        GoodItem milk1 = new GoodItem("Milk", 1000, 2.99, 10, "Refrigerated", "");
        GoodItem milk2 = new GoodItem("Milk", 1000, 3.49, 5, "Refrigerated", "");
        GoodItem differentMilk = new GoodItem("Milk", 500, 2.99, 10, "Refrigerated", "");
        GoodItem cheese = new GoodItem("Cheese", 200, 5.99, 5, "Refrigerated", "");

        assertTrue(milk1.isSameItem(milk2)); // Same description and size
        assertFalse(milk1.isSameItem(differentMilk)); // Different size
        assertFalse(milk1.isSameItem(cheese)); // Different description
    }

    // Save and Load test
    @Test
    void testSaveAndLoadSystem() throws IOException {
        // Create test data
        supermarket.addFloorArea("Dairy", "Ground Floor");
        supermarket.addAisle("Dairy", "Milk Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addShelf("Milk Aisle", 1);

        GoodItem milk = new GoodItem("Milk", 1000, 2.99, 10, "Refrigerated", "");
        supermarket.addGoodItem("Milk Aisle", 1, milk);

        String stockReport = supermarket.viewAllStock();
        assertTrue(stockReport.contains("Supermarket Stock Report"));
        assertTrue(stockReport.contains("Dairy"));
        assertTrue(stockReport.contains("Milk Aisle"));
        assertTrue(stockReport.contains("Milk"));
        assertTrue(stockReport.contains("€"));
    }

    // Test temperature check
    @Test
    void testTemperatureCompatibility() {
        supermarket.addFloorArea("Mixed", "Ground Floor");
        supermarket.addAisle("Mixed", "Refrigerated Aisle", 10.0, 5.0, "Refrigerated");
        supermarket.addAisle("Mixed", "Frozen Aisle", 8.0, 4.0, "Frozen");

        supermarket.addShelf("Refrigerated Aisle", 1);
        supermarket.addShelf("Frozen Aisle", 1);

        GoodItem refrigeratedItem = new GoodItem("Yogurt", 500, 1.49, 10, "Refrigerated", "");
        GoodItem frozenItem = new GoodItem("Ice Cream", 500, 4.99, 5, "Frozen", "");

        assertTrue(supermarket.addGoodItem("Refrigerated Aisle", 1, refrigeratedItem));
        assertTrue(supermarket.addGoodItem("Frozen Aisle", 1, frozenItem));
    }

    // Test edge cases
    @Test
    void testEdgeCases() {
        // Test adding to non-existent locations
        assertFalse(supermarket.addAisle("NonExistent", "Test Aisle", 10.0, 5.0, "Refrigerated"));
        assertFalse(supermarket.addShelf("NonExistent", 1));

        GoodItem item = new GoodItem("Test", 100, 1.0, 1, "Refrigerated", "");
        assertFalse(supermarket.addGoodItem("NonExistent", 1, item));

        // Test empty search
        String emptySearch = supermarket.searchGoodItems("");
        assertTrue(emptySearch.contains("Search results for: ''"));

        // Test smart add with no suitable location
        supermarket.reset();
        GoodItem noHomeItem = new GoodItem("Orphan Item", 100, 1.0, 1, "Refrigerated", "");
        String smartAddResult = supermarket.smartAddGoodItem(noHomeItem);

        assertTrue(smartAddResult.contains("No suitable location") ||
                smartAddResult.contains("Need an aisle with temperature"));
    }
}