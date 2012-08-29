import java.io.*;
import java.util.*;

public class Product{
  
  class ProductListings{
    private String product_name;
    private ArrayList<Listing> listings;
    public ProductListings(){
      listings = new ArrayList<Listing>();
    }
    
    public void addListing(Listing lst){
       listings.add(lst);
    }
    
    public void setName(String name){
      product_name = name;
    }
  }
  
  private String product_name;
  private String manufacturer;
  private String family;
  private String model;
  private ProductListings product_listings;
  int list_count;
  
  public  Product(){
    product_listings = new ProductListings();
    list_count = 0;
  }
  
  public String get_name(){return product_name;}
  public String get_manufacturer(){return manufacturer;}
  public String get_family(){return family;}
  public String get_model(){return model;}
  public ProductListings get_listings(){
    product_listings.setName(product_name);
    return product_listings;
  }
   
  public void addListing(Listing lst){
    product_listings.addListing(lst);
    list_count++;
  }
}