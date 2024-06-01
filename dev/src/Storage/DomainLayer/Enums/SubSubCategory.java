package Storage.DomainLayer.Enums;

public enum SubSubCategory {
    Small,
    Medium,
    Large;

    public static boolean contains(String size) {

        for (SubSubCategory c : SubSubCategory.values()) {
            if (c.name().equals(size)) {
                return true;
            }
        }

        return false;
    }
}
