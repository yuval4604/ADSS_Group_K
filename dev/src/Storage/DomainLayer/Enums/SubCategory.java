package Storage.DomainLayer.Enums;

public enum SubCategory {

    Milk,
    Cheese,
    Yogurt,
    Eggs,
    Chicken,
    Beef,
    Fish,
    Pork,
    Carrots,
    Tomatoes,
    Cucumbers,
    Apples,
    Bananas,
    Oranges,
    Grapes,
    Soap,
    Detergent,
    Shampoo,
    Conditioner,
    TV,
    Computer,
    Phone,
    Tablet,
    Shirt,
    Pants,
    Shoes,
    Hat,
    Other;

    public static boolean contains(String subCategory) {

        for (SubCategory c : SubCategory.values()) {
            if (c.name().equals(subCategory)) {
                return true;
            }
        }

        return false;
    }
}
