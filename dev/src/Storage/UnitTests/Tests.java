package Storage.UnitTests;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

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
        beef = new Product(100,"Beef-Steak 500gr", Category.Meat, SubCategory.Beef, SubSubCategory.Large,50,74.95,0,0,"Joes' Meats","10",20);
        soap = new Product(100,"Hand-Soap 200ml", Category.Cleaning, SubCategory.Soap, SubSubCategory.Medium,10,15,0,0,"Soapy","7",13);
    }

    @Test
    public void AddProductToBatchTest(){
        milk.addByExpirationDate(0,25, LocalDate.parse("2024-07-10"));
        milk.addByExpirationDate(0,15, LocalDate.parse("2024-07-07"));
        milk.addByExpirationDate(0,10, LocalDate.parse("2024-07-01"));
        beef.addByExpirationDate(3,14, LocalDate.parse("2024-06-20"));
        beef.addByExpirationDate(2,20, LocalDate.parse("2024-06-24"));
        soap.addByExpirationDate(10,0, LocalDate.parse("2024-12-12"));

        Assert.assertEquals(125,milk.getStorageQuantity());
        Assert.assertEquals(50,milk.getStoreQuantity());

        Assert.assertEquals(125,beef.getStorageQuantity());
        Assert.assertEquals(50,beef.getStoreQuantity());

        Assert.assertEquals(125,soap.getStorageQuantity());
        Assert.assertEquals(50,soap.getStoreQuantity());
    }
    @Test
    public void DecreaseProductFromBatchTest(){
        for(int i = 0; i < 20; i++){
            milk.removeOne(false,LocalDate.parse());
        }
    }
}
