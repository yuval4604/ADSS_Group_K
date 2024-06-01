package Storage.DomainLayer.Enums;

public enum Category {
    Dairy,
    Meat,
    Vegetables,
    Fruits,
    Cleaning,
    Electronics,
    Clothing,
    Other;

    public static boolean contains(String category) {

        for (Category c : Category.values()) {
            if (c.name().equals(category)) {
                return true;
            }
        }

        return false;
    }
}
