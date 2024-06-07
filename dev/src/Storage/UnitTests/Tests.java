package Storage.UnitTests;


import Storage.DomainLayer.DomainManager;
import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Tests {
    private Product milk;
    private Product beef;
    private Product soap;
    private DomainManager manager;

    @Before
    public void initTest() throws Exception {
        milk = new Product(100,"Tnuva-milk 500ml", Category.Dairy, SubCategory.Milk, SubSubCategory.Small,6,8.5,0,0,"Tnuva","14",15);
        beef = new Product(101,"Beef-Steak 500gr", Category.Meat, SubCategory.Beef, SubSubCategory.Large,50,74.95,0,0,"Joes' Meats","10",20);
        soap = new Product(102,"Hand-Soap 200ml", Category.Cleaning, SubCategory.Soap, SubSubCategory.Medium,10,15,0,0,"Soapy","7",13);
        manager = new DomainManager(new DomainFacade());
        manager.addProduct(milk);
        manager.addProduct(beef);
        manager.addProduct(soap);
    }

    @Test
    public void AddProductToBatchTest(){
        milk.addByExpirationDate(0,25, LocalDate.parse("2024-07-10"));
        milk.addByExpirationDate(3,15, LocalDate.parse("2024-07-07"));
        milk.addByExpirationDate(0,10, LocalDate.parse("2024-07-01"));
        beef.addByExpirationDate(3,14, LocalDate.parse("2024-06-20"));
        beef.addByExpirationDate(2,20, LocalDate.parse("2024-06-24"));
        soap.addByExpirationDate(10,0, LocalDate.parse("2024-12-12"));

        Assert.assertEquals(50,milk.getStorageQuantity());
        Assert.assertEquals(3,milk.getStoreQuantity());

        Assert.assertEquals(34,beef.getStorageQuantity());
        Assert.assertEquals(5,beef.getStoreQuantity());

        Assert.assertEquals(0,soap.getStorageQuantity());
        Assert.assertEquals(10,soap.getStoreQuantity());
    }
    @Test
    public void DecreaseProductFromBatchTest(){
        milk.addByExpirationDate(0,25, LocalDate.parse("2024-07-10"));
        milk.addByExpirationDate(3,15, LocalDate.parse("2024-07-07"));
        milk.addByExpirationDate(0,10, LocalDate.parse("2024-07-01"));
        beef.addByExpirationDate(3,14, LocalDate.parse("2024-06-20"));
        beef.addByExpirationDate(2,20, LocalDate.parse("2024-06-24"));
        soap.addByExpirationDate(10,0, LocalDate.parse("2024-12-12"));

        for(int i = 0; i < 20; i++){
            milk.removeOne(true,LocalDate.parse("2024-07-10"));
        }
        for(int i = 0; i < 20; i++){
            beef.removeOne(true,LocalDate.parse("2024-06-24"));
        }
        beef.removeOne(false,LocalDate.parse("2024-06-20"));
        for(int i = 0; i < 3; i++){
            soap.removeOne(false,LocalDate.parse("2024-12-12"));
        }

        Assert.assertEquals(30,milk.getStorageQuantity());
        Assert.assertEquals(3,milk.getStoreQuantity());

        Assert.assertEquals(14,beef.getStorageQuantity());
        Assert.assertEquals(4,beef.getStoreQuantity());

        Assert.assertEquals(0,soap.getStorageQuantity());
        Assert.assertEquals(7,soap.getStoreQuantity());
    }

    @Test
    public void MoveBatchToStoreTest(){
        milk.addByExpirationDate(0,25, LocalDate.parse("2024-07-10"));
        milk.addByExpirationDate(3,15, LocalDate.parse("2024-07-07"));
        milk.addByExpirationDate(0,10, LocalDate.parse("2024-07-01"));
        beef.addByExpirationDate(3,14, LocalDate.parse("2024-06-20"));
        beef.addByExpirationDate(2,20, LocalDate.parse("2024-06-24"));
        soap.addByExpirationDate(10,0, LocalDate.parse("2024-12-12"));

       milk.moveProductToStore(LocalDate.parse("2024-07-10"), 14);
       beef.moveProductToStore(LocalDate.parse("2024-06-24") ,20);
       try{
            soap.moveProductToStore(LocalDate.parse("2024-12-12") ,1);
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
        beef.addByExpirationDate(3,14, LocalDate.parse("2024-06-20"));
        beef.addByExpirationDate(2,20, LocalDate.parse("2024-06-24"));
        soap.addByExpirationDate(10,0, LocalDate.parse("2024-12-12"));

        Assert.assertEquals("Tnuva-milk 500ml\nHand-Soap 200ml",manager.alertOnMinimalQuantity());
    }

    @Test
    public void getProductsByCategoriesTest() throws Exception {
        Product otherMilk = new Product(103,"Tara-milk 500ml", Category.Dairy, SubCategory.Milk, SubSubCategory.Small,5,7.5,0,0,"Tnuva","12",6);
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
    public void ApplyDiscountTest(){
        milk.setDiscount(0.3);
        soap.setDiscount(1);

        Assert.assertEquals(5.95,milk.getDiscountedPrice(),0.99);
        try{
            beef.setDiscount(1.5);
        }
        catch (Exception e){
            Assert.assertEquals("Discount is out of bounds", e.getMessage());
        }
        Assert.assertEquals(0,soap.getDiscountedPrice(),0.99);
    }

    @Test
    public void MoveToDamagedTest(){
        milk.addByExpirationDate(0,25, LocalDate.parse("2024-07-10"));
        milk.addByExpirationDate(3,15, LocalDate.parse("2024-07-07"));
        milk.addByExpirationDate(0,10, LocalDate.parse("2024-07-01"));
        beef.addByExpirationDate(3,14, LocalDate.parse("2024-06-20"));
        beef.addByExpirationDate(2,20, LocalDate.parse("2024-06-24"));
        soap.addByExpirationDate(10,0, LocalDate.parse("2024-12-12"));

        milk.moveToDamage(new int[]{2}, new int[]{15}, new LocalDate[]{LocalDate.parse("2024-07-07")});
        beef.moveToDamage(new int[]{3}, new int[]{2}, new LocalDate[]{LocalDate.parse("2024-06-20")});
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
