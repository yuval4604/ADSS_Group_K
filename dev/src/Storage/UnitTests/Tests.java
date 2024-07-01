package Storage.UnitTests;


import Storage.DataAccessLayer.Repository;
import Storage.DomainLayer.DomainManager;
import Storage.DomainLayer.Product;
import org.junit.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Tests {
    private static Product milk;
    private static Product beef;
    private static Product soap;
    private static DomainManager manager;

    @Before
    public void initTest() throws Exception {
        milk = new Product(100,"Tnuva-milk 500ml", "Dairy", "Milk", "Small",6,8.5,0,0,"Tnuva","14",15);
        beef = new Product(101,"Beef-Steak 500gr", "Meat", "Beef", "Large",50,74.95,0,0,"Joes' Meats","10",20);
        soap = new Product(102,"Hand-Soap 200ml", "Cleaning", "Soap", "Medium",10,15,0,0,"Soapy","7",13);
        manager = new DomainManager(new Repository());
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

    @After
    public void tearDown() {
        manager.getRepo().getDao().rollback();
    }


    @Test
    public void AddProductToBatchTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2024-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2024-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2024-07-01"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2024-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2024-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2024-12-12"),102, 10, 0);

        Assert.assertEquals(50,milk.getStorageQuantity());
        Assert.assertEquals(3,milk.getStoreQuantity());

        Assert.assertEquals(34,beef.getStorageQuantity());
        Assert.assertEquals(5,beef.getStoreQuantity());

        Assert.assertEquals(0,soap.getStorageQuantity());
        Assert.assertEquals(10,soap.getStoreQuantity());
    }
    @Test
    public void DecreaseProductFromBatchTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2024-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2024-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2024-07-01"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2024-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2024-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2024-12-12"),102, 10, 0);

        manager.subtractFromStore(100, Map.of(LocalDate.parse("2024-07-10"), 20));
        manager.subtractFromStore(101, Map.of(LocalDate.parse("2024-07-24"), 20));
        manager.subtractFromStore(102, Map.of(LocalDate.parse("2024-12-12"), 1));

        manager.subtractFromStore(100, Map.of(LocalDate.parse("2024-12-12"), 3));

        Assert.assertEquals(30,milk.getStorageQuantity());
        Assert.assertEquals(3,milk.getStoreQuantity());

        Assert.assertEquals(14,beef.getStorageQuantity());
        Assert.assertEquals(4,beef.getStoreQuantity());

        Assert.assertEquals(0,soap.getStorageQuantity());
        Assert.assertEquals(7,soap.getStoreQuantity());
    }

    @Test
    public void MoveBatchToStoreTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2024-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2024-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2024-07-01"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2024-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2024-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2024-12-12"),102, 10, 0);

        manager.moveProductToStore(100, LocalDate.parse("2024-07-10"), 14);
        manager.moveProductToStore(101, LocalDate.parse("2024-07-24"), 20);
       try{
           manager.moveProductToStore(102, LocalDate.parse("2024-12-12"), 1);
       }
       catch (Exception e){
           Assert.assertEquals("Not enough quantity to move to store",e.getMessage());
       }

        Assert.assertEquals(36,milk.getStorageQuantity());
        Assert.assertEquals(17,milk.getStoreQuantity());

        Assert.assertEquals(14,beef.getStorageQuantity());
        Assert.assertEquals(25,beef.getStoreQuantity());

        Assert.assertEquals(0,soap.getStorageQuantity());
        Assert.assertEquals(10,soap.getStoreQuantity());
    }

    @Test
    public void CheckMinimalQuantityTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2024-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2024-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2024-12-12"),102, 10, 0);

        Assert.assertEquals("Tnuva-milk 500ml\nHand-Soap 200ml",manager.alertOnMinimalQuantity());
    }

    @Test
    public void getProductsByCategoriesTest() throws Exception {
        Product otherMilk = new Product(103,"Tara-milk 500ml", "Dairy", "Milk", "Small",5,7.5,0,0,"Tnuva","12",6);
        manager.addProduct(otherMilk);
        List<String> stuff = new LinkedList<>();
        stuff.add("Meat");
        stuff.add("Dairy,Milk,Small");

        List<Product> res = new LinkedList<>();
        res.add(beef);
        res.add(milk);
        res.add(otherMilk);

        Assert.assertEquals(res,manager.getProductsByCategories(stuff));
    }

    @Test
    public void ApplyDiscountTest() throws SQLException {
        manager.setDiscount(101,0.3);
        manager.setDiscount(102,1);

        Assert.assertEquals(5.95,milk.getDiscountedPrice(),0.99);
        try{
            manager.setDiscount(101,1.5);
        }
        catch (Exception e){
            Assert.assertEquals("Discount is out of bounds", e.getMessage());
        }
        Assert.assertEquals(0,soap.getDiscountedPrice(),0.99);
    }

    @Test
    public void MoveToDamagedTest() throws Exception {
        manager.addToProduct(LocalDate.parse("2024-07-10"),100, 0, 25);
        manager.addToProduct(LocalDate.parse("2024-07-07"),100, 3, 15);
        manager.addToProduct(LocalDate.parse("2024-07-01"),100, 0, 10);
        manager.addToProduct(LocalDate.parse("2024-07-20"),101, 3, 14);
        manager.addToProduct(LocalDate.parse("2024-07-24"),101, 2, 20);
        manager.addToProduct(LocalDate.parse("2024-12-12"),102, 10, 0);

        manager.moveToDamage(100, new int[]{2}, new int[]{15}, new LocalDate[]{LocalDate.parse("2024-07-07")});
        milk.moveToDamage(new int[]{2}, new int[]{15}, new LocalDate[]{LocalDate.parse("2024-07-07")});
        beef.moveToDamage(new int[]{3}, new int[]{2}, new LocalDate[]{LocalDate.parse("2024-07-20")});
        soap.moveToDamage(new int[]{0},new int[]{0}, new LocalDate[]{LocalDate.parse("2024-12-12")});

        Assert.assertEquals(35,milk.getStorageQuantity());
        Assert.assertEquals(1,milk.getStoreQuantity());
        Assert.assertEquals(17,milk.getDamageQuantity());

        Assert.assertEquals(32,beef.getStorageQuantity());
        Assert.assertEquals(2,beef.getStoreQuantity());
        Assert.assertEquals(5,beef.getDamageQuantity());

        Assert.assertEquals(0,soap.getStorageQuantity());
        Assert.assertEquals(10,soap.getStoreQuantity());
        Assert.assertEquals(0,soap.getDamageQuantity());
    }

    @Test
    public void ProductReportToStringTest(){
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
                "Minimal quantity: " + 15 + "\n",milk.productReportToString());
    }

    @Test
    public void DamagedAndExpiredReportToStringTest(){
        milk.addByExpirationDate(0,25, LocalDate.parse("2024-07-10"));
        milk.addByExpirationDate(3,15, LocalDate.parse("2024-07-07"));
        milk.addByExpirationDate(0,10, LocalDate.parse("2024-07-01"));

        milk.moveToDamage(new int[]{2}, new int[]{15}, new LocalDate[]{LocalDate.parse("2024-07-07")});
        Assert.assertEquals("Catalog number: " + 100 + "\n" +
                "Name: " + "Tnuva-milk 500ml" + "\n" +
                "Category: " + "Dairy" + "\n" +
                "Sub category: " + "Milk" + "\n" +
                "Size: " + "Small" + "\n" +
                "Aisle: " + 14 + "\n" +
                "Expired quantity: " + 0 + "\n" +
                "Damaged quantity: " + 17  + "\n",milk.damagedReportToString());
    }

    @Test
    public void ToStringTest(){
        milk.addByExpirationDate(0,25, LocalDate.parse("2024-07-10"));
        milk.addByExpirationDate(3,15, LocalDate.parse("2024-07-07"));
        milk.addByExpirationDate(0,10, LocalDate.parse("2024-07-01"));
        milk.setDiscount(0.2);

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
                "Minimal quantity: " + 15 + "\n",milk.productReportToString());
    }
}
