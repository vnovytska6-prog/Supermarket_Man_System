package supermarket.Models;

public class Shelf {
    private int shelfNumber;
    private LinkedList<GoodItem> goodItems;

    public Shelf(int shelfNumber) {
        this.shelfNumber = shelfNumber;
        this.goodItems = new LinkedList<>();
    }

    public int getShelfNumber() { return shelfNumber; }
    public LinkedList<GoodItem> getGoodItems() { return goodItems; }

    // adding item on the shelf
    public void addGoodItem(GoodItem newItem) {
        GoodItem existing = goodItems.find(new LinkedList.Condition<GoodItem>() {
            @Override
            public boolean test(GoodItem item) {
                return item.isSameItem(newItem);
            }
        });
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
            // update price
            existing.setUnitPrice(newItem.getUnitPrice());
            if (newItem.getPhotoUrl() != null && !newItem.getPhotoUrl().isEmpty()) {
                existing.setPhotoUrl(newItem.getPhotoUrl());
            }
        } else {
            goodItems.add(newItem);
        }
    }

    // delete item
    public boolean removeGoodItem(GoodItem target, int quantityToRemove) {
        GoodItem existing = goodItems.find(new LinkedList.Condition<GoodItem>() {
            @Override
            public boolean test(GoodItem item) {
                return item.isSameItem(target);
            }
        });
        if (existing != null) {
            if (quantityToRemove >= existing.getQuantity()) {
                // deleting
                int index = findIndex(existing);
                if (index != -1) {
                    goodItems.remove(index);
                    return true;
                }
            } else {
                // change quantity
                existing.setQuantity(existing.getQuantity() - quantityToRemove);
                return true;
            }
        }
        return false;
    }

    private int findIndex(GoodItem item) {
        for (int i = 0; i < goodItems.size(); i++) {
            if (goodItems.get(i).isSameItem(item)) {
                return i;
            }
        }
        return -1;
    }

    public double getTotalValue() {
        double total = 0;
        for (int i = 0; i < goodItems.size(); i++) {
            total += goodItems.get(i).getTotalValue();
        }
        return total;
    }

    public int getTotalItemsCount() {
        int total = 0;
        for (int i = 0; i < goodItems.size(); i++) {
            total += goodItems.get(i).getQuantity();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Shelf %d: %d items = €%.2f",
                shelfNumber, getTotalItemsCount(), getTotalValue());
    }
}
