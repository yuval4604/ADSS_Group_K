package Storage.UnitTests;


import Storage.DomainLayer.Facades.DomainFacade;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import Storage.DomainLayer.Enums.*;
import Storage.DomainLayer.*;

import java.time.LocalDate;
import java.util.HashMap;

public class Tests {
    private Product milk;
    private Product beef;
    private Product soap;

    @Before
    public void initTest() {
        milk = new Product(100,"Tnuva-milk 500ml", Category.Dairy, SubCategory.Milk, SubSubCategory.Small,6,8.5,0,0,"Tnuva","14",15);
        beef = new Product(101,"Beef-Steak 500gr", Category.Meat, SubCategory.Beef, SubSubCategory.Large,50,74.95,0,0,"Joes' Meats","10",20);
        soap = new Product(102,"Hand-Soap 200ml", Category.Cleaning, SubCategory.Soap, SubSubCategory.Medium,10,15,0,0,"Soapy","7",13);
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

       milk.moveProductToStore(14);
       beef.moveProductToStore(34);
       try{
            soap.moveProductToStore(1);
       }
       catch (Exception e){
           Assert.assertEquals("Not enough quantity to move to store",e.getMessage());
       }

        Assert.assertEquals(36,milk.getStorageQuantity());
        Assert.assertEquals(17,milk.getStoreQuantity());

        Assert.assertEquals(0,beef.getStorageQuantity());
        Assert.assertEquals(39,beef.getStoreQuantity());

        Assert.assertEquals(0,soap.getStorageQuantity());
        Assert.assertEquals(10,soap.getStoreQuantity());
    }

    @Test
    public void CheckExpirationTest(){
        milk.addByExpirationDate(1,0, LocalDate.parse("2024-06-01"));
        milk.addByExpirationDate(1,0, LocalDate.parse("2024-07-01"));
        beef.addByExpirationDate(0,3, LocalDate.parse("2024-06-01"));
        beef.addByExpirationDate(1,1, LocalDate.parse("2024-07-01"));
        soap.addByExpirationDate(1,0, LocalDate.parse("2024-12-12"));

        Assert.assertEquals(1,milk.expiredCount());
        Assert.assertEquals(3,beef.expiredCount());
        Assert.assertEquals(0,soap.expiredCount());
    }

    @Test
    public void CheckMinimalQuantityTest() throws Exception {
        DomainManager manager = new DomainManager(new DomainFacade());
        manager.addProduct(milk);
        manager.addProduct(beef);
        manager.addProduct(soap);
        beef.addByExpirationDate(3,14, LocalDate.parse("2024-06-20"));
        beef.addByExpirationDate(2,20, LocalDate.parse("2024-06-24"));
        soap.addByExpirationDate(10,0, LocalDate.parse("2024-12-12"));

        Assert.assertEquals("Tnuva-milk 500ml\nHand-Soap 200ml",manager.alertOnMinimalQuantity());
    }
}
