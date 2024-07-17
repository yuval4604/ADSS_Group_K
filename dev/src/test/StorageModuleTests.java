package src.test;


import src.Storage.DataAccessLayer.Repository;
import src.Storage.DomainLayer.DomainManager;
import src.Storage.DomainLayer.Product;
import org.junit.*;

import java.sql.Connection;
import java.sql.Savepoint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StorageModuleTests {
    private static Product milk;
    private static Product beef;
    private static Product soap;
    private static DomainManager manager;
    private static Savepoint sv;
    private static Connection conn;
    @BeforeClass
    public static void setUp() throws Exception {
        manager = new DomainManager(new Repository("tests.db"));
        manager.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
         manager.deleteAll();
    }

    @Before
    public void initTest() throws Exception {
        milk = new Product(100,"Tnuva-milk 500ml", "Dairy", "Milk", "Small",6,8.5,0,0,"Tnuva","14",15);
        beef = new Product(101,"Beef-Steak 500gr", "Meat", "Beef", "Large",50,74.95,0,0,"Joes' Meats","10",20);
        soap = new Product(102,"Hand-Soap 200ml", "Cleaning", "Soap", "Medium",10,15,0,0,"Soapy","7",13);
        manager.addCategory("Dairy");
        manager.addCategory("Meat");
        manager.addCategory("Cleaning");
        manager.addSubCategory("Milk");
        manager.addSubCategory("Beef");
        manager.addSubCategory("Soap");
        manager.addSize("Small");
        manager.addSize("Large");
        manager.addSize("Medium");
        manager.addProduct(milk);
        manager.addProduct(beef);
        manager.addProduct(soap);
    }



    @Test
    public void AddProductToBatchTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2025-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2025-08-01"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2025-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2025-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2025-12-12"),102, 10, 0);

        Assert.assertEquals(50, manager.getProduct(100).getStorageQuantity());
        Assert.assertEquals(3, manager.getProduct(100).getStoreQuantity());

        Assert.assertEquals(34, manager.getProduct(101).getStorageQuantity());
        Assert.assertEquals(5, manager.getProduct(101).getStoreQuantity());

        Assert.assertEquals(0, manager.getProduct(102).getStorageQuantity());
        Assert.assertEquals(10, manager.getProduct(102).getStoreQuantity());
    }
    @Test
    public void DecreaseProductFromBatchTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-07-10"),100, 25, 25);
        manager.addToProduct(LocalDate.parse("2025-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2025-08-01"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2025-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2025-07-24"),101, 22, 20);
        manager.addToProduct(LocalDate.parse("2025-12-12"),102, 10, 0);

        manager.subtractFromStore(100, Map.of(LocalDate.parse("2025-07-10"), 20));
        manager.subtractFromStore(101, Map.of(LocalDate.parse("2025-07-24"), 20));
        manager.subtractFromStore(102, Map.of(LocalDate.parse("2025-12-12"), 1));

        Assert.assertEquals(50, manager.getProduct(100).getStorageQuantity());
        Assert.assertEquals(8,  manager.getProduct(100).getStoreQuantity());

        Assert.assertEquals(34, manager.getProduct(101).getStorageQuantity());
        Assert.assertEquals(5, manager.getProduct(101).getStoreQuantity());

        Assert.assertEquals(0, manager.getProduct(102).getStorageQuantity());
        Assert.assertEquals(9, manager.getProduct(102).getStoreQuantity());
    }

    @Test
    public void FailMoveBatchToStore() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-12-12"),102, 10, 0);
        try{
            manager.moveProductToStore(102, LocalDate.parse("2025-12-12"), 1);
        }
        catch (Exception e){
            Assert.assertEquals("Not enough quantity to move to store",e.getMessage());
        }
        Assert.assertEquals(0, manager.getProduct(102).getStorageQuantity());
        Assert.assertEquals(10, manager.getProduct(102).getStoreQuantity());
    }

    @Test
    public void MoveBatchToStoreTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2025-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2025-08-01"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2025-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2025-07-24"),101, 2, 20);

        manager.moveProductToStore(100, LocalDate.parse("2025-07-10"), 14);
        manager.moveProductToStore(101, LocalDate.parse("2025-07-24"), 20);

        Assert.assertEquals(36, manager.getProduct(100).getStorageQuantity());
        Assert.assertEquals(17, manager.getProduct(100).getStoreQuantity());

        Assert.assertEquals(14, manager.getProduct(101).getStorageQuantity());
        Assert.assertEquals(25, manager.getProduct(101).getStoreQuantity());

        Assert.assertEquals(0, manager.getProduct(102).getStorageQuantity());
        Assert.assertEquals(0, manager.getProduct(102).getStoreQuantity());
    }

    @Test
    public void CheckMinimalQuantityTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2025-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2025-12-12"),102, 10, 0);

        Assert.assertEquals("Tnuva-milk 500ml\nHand-Soap 200ml",manager.alertOnMinimalQuantity());
    }

    @Test
    public void getProductsByCategoriesTest() throws Exception {
        Product otherMilk = new Product(103,"Tara-milk 500ml", "Dairy", "Milk", "Small",5,7.5,0,0,"Tnuva","12",6);
        manager.addProduct(otherMilk);
        List<String> stuff = new ArrayList<>();
        stuff.add("Meat");
        stuff.add("Dairy,Milk,Small");

        List<Product> res = new ArrayList<>();
        res.add(milk);
        res.add(beef);
        res.add(otherMilk);

        Assert.assertEquals(res.toString(),manager.getProductsByCategories(stuff).toString());
    }

    @Test
    public void FailApplyDiscountTest() throws Exception {
        try{
            manager.setDiscount(102,1.5);
        }
        catch (Exception e){
            Assert.assertEquals("Discount is out of bounds", e.getMessage());
        }
    }

    @Test
    public void ApplyDiscountTest() throws Exception {
        manager.setDiscount(100,0.3);
        manager.setDiscount(101,1);

        Assert.assertEquals(5.95, manager.getProduct(100).getDiscountedPrice(),0.99);

        Assert.assertEquals(0, manager.getProduct(101).getDiscountedPrice(),0.99);
    }

    @Test
    public void MoveToDamagedTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2025-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2025-07-15"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2025-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2025-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2025-12-12"),102, 10, 10);

        manager.updateDamageForProduct(100, new int[]{2}, new int[]{15}, new LocalDate[]{LocalDate.parse("2025-07-07")});
        manager.updateDamageForProduct(101, new int[]{3}, new int[]{2}, new LocalDate[]{LocalDate.parse("2025-07-20")});
        manager.updateDamageForProduct(102, new int[]{0}, new int[]{10}, new LocalDate[]{LocalDate.parse("2025-12-12")});

        Assert.assertEquals(35, manager.getProduct(100).getStorageQuantity());
        Assert.assertEquals(1, manager.getProduct(100).getStoreQuantity());
        Assert.assertEquals(17, manager.getProduct(100).getDamageQuantity());

        Assert.assertEquals(32, manager.getProduct(101).getStorageQuantity());
        Assert.assertEquals(2, manager.getProduct(101).getStoreQuantity());
        Assert.assertEquals(5, manager.getProduct(101).getDamageQuantity());

        Assert.assertEquals(0, manager.getProduct(102).getStorageQuantity());
        Assert.assertEquals(10, manager.getProduct(102).getStoreQuantity());
        Assert.assertEquals(10, manager.getProduct(102).getDamageQuantity());
    }

    @Test
    public void ProductReportToStringTest() throws Exception {
        Assert.assertEquals("Catalog number: " + 100 + "\n" +
                "Name: " + "Tnuva-milk 500ml" + "\n" +
                "Category: " + "Dairy" + "\n" +
                "Sub category: " + "Milk" + "\n" +
                "Size: " + "Small" + "\n" +
                "Storage quantity: " + 0 + "\n" +
                "Store quantity: " + 0 + "\n" +
                "Manufacturer: " + "Tnuva" + "\n" +
                "Discount: " + 0.0 * 100 + "% " + "\n" +
                "Supplier discount: " + 0.0 * 100 + "% " + "\n" +
                "Aisle: " + 14 + "\n" +
                "Minimal quantity: " + 15 + "\n", manager.getProduct(100).productReportToString());
    }

    @Test
    public void DamagedAndExpiredReportToStringTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2025-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2025-08-01"),100, 0, 10);

        manager.updateDamageForProduct(100, new int[]{2}, new int[]{15}, new LocalDate[]{LocalDate.parse("2025-07-07")});
        Assert.assertEquals("Catalog number: " + 100 + "\n" +
                "Name: " + "Tnuva-milk 500ml" + "\n" +
                "Category: " + "Dairy" + "\n" +
                "Sub category: " + "Milk" + "\n" +
                "Size: " + "Small" + "\n" +
                "Aisle: " + 14 + "\n" +
                "Expired quantity: " + 0 + "\n" +
                "Damaged quantity: " + 17  + "\n", manager.getProduct(100).damagedReportToString());
    }

    @Test
    public void ToStringTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2025-08-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2025-08-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2025-08-01"),100, 0, 10);
        manager.setDiscount(100,0.2);

        Assert.assertEquals("Catalog number: " + 100 + "\n" +
                "Name: " + "Tnuva-milk 500ml" + "\n" +
                "Category: " + "Dairy" + "\n" +
                "Sub category: " + "Milk" + "\n" +
                "Size: " + "Small" + "\n" +
                "Storage quantity: " + 50 + "\n" +
                "Store quantity: " + 3 + "\n" +
                "Manufacturer: " + "Tnuva" + "\n" +
                "Discount: " + 0.2 * 100 + "% " + "\n" +
                "Supplier discount: " + 0.0 * 100 + "% " + "\n" +
                "Aisle: " + 14 + "\n" +
                "Minimal quantity: " + 15 + "\n", manager.getProduct(100).productReportToString());
    }

    @Test
    public void FailGetProduct() throws Exception {
        Assert.assertEquals(null, manager.getProduct(103));
    }

    @Test
    public void FailAddProductAlreadyExists() throws Exception {
        Product otherMilk = new Product(100,"Tara-milk 500ml", "Dairy", "Milk", "Small",5,7.5,0,0,"Tnuva","12",6);
        try{
            manager.addProduct(otherMilk);
        }
        catch (Exception e){
            Assert.assertEquals("Product with this catalog number already exists", e.getMessage());
        }
    }

    @Test
    public void FailRemoveProduct() throws Exception {
        try{
            manager.removeProduct(103);
        }
        catch (Exception e){
            Assert.assertEquals("Product with this catalog number does not exist", e.getMessage());
        }
    }

    @Test
    public void FailAddProductInvalidCategory() throws Exception {
        try{
            manager.addProduct(new Product(104,"Tara-milk 500ml", "Dairy", "Cheese", "Small",5,7.5,0,0,"Tnuva","12",6));
        }
        catch (Exception e){
            Assert.assertEquals("Invalid category", e.getMessage());
        }
    }

    @Test
    public void FailUpdateDiscount() throws Exception {
        try{
            manager.setDiscount(103,0.5);
        }
        catch (Exception e){
            Assert.assertEquals("Product does not exist", e.getMessage());
        }
    }

    @Test
    public void FailUpdateProduct() throws Exception {
        try{
            manager.updateDamageForProduct(103, new int[]{0}, new int[]{0}, new LocalDate[]{LocalDate.parse("2025-07-07")});
        }
        catch (Exception e){
            Assert.assertEquals("Product does not exist", e.getMessage());
        }
    }

    @Test
    public void FailGetProductsByCategories() throws Exception {
        List<String> stuff = new ArrayList<>();
        stuff.add("Meat");
        stuff.add("Dairy,Cheese,Small");

        try{
            manager.getProductsByCategories(stuff);
        }
        catch (Exception e){
            Assert.assertEquals("Invalid category", e.getMessage());
        }
    }

    @Test
    public void FailAddProductNull() {
        try {
            manager.addProduct(null);
        } catch (Exception e) {
            Assert.assertEquals("Product is null", e.getMessage());
        }
    }
}
