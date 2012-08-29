import java.io.*;
import java.util.*;
import com.google.gson.Gson;

/*
  The ProductMatcher class used to parse the inputfile. Based on the input Json string, it creates Products and puts them
  to related manufacturers. The ProductMatcher self stores all the manufacturers and listings. When the match function is called, 
  it scans through all the listings. For each listing, it find the related Manufacturer use the manufacture_table hashmap, 
  and ask the related Manufacturer to search and map the listing inside its products.
*/
public class ProductMatcher{
  //Mapping a string to manufacturer object. All manufacturers parsed from input file are stored in the hash map
  HashMap<String, Manufacturer> manufacture_table;
  
  //store all the listings object in a array list 
  ArrayList<Listing> listings;
  
  //the output writer
  PrintWriter outPut;

/* Constructor: initilize the data members and output writer*/
  public  ProductMatcher(String outPutFile){
    listings = new ArrayList<Listing>();
    manufacture_table = new HashMap<String, Manufacturer>();
    try{
      outPut = new PrintWriter(new FileWriter(outPutFile));
    }catch(Exception e){
      System.err.println("Error: " + e.getMessage());
    }
  }

/*
  Parsing the json string from product data file uses gson library. Creates new Products and puts them inside
  related Manufacturers. Also creates new menufacturers if needed, and add them to the manufacture_table.
*/
  public void parseProduct(String productJson){
    Gson gson = new Gson();
    Product prod = gson.fromJson(productJson, Product.class);
    String man_str = unifyString(prod.get_manufacturer());
    
    if(!manufacture_table.containsKey(man_str)){
      Manufacturer new_man = new Manufacturer(man_str);
      manufacture_table.put(man_str,new_man);
    }
    
    Manufacturer man = manufacture_table.get(man_str);
    man.addProduct(prod);
  }

/*
  Parsing the list data file and created Listing object from the json string use gson. Save the Listing in an ArrayList
*/
  public void parseListing(String listingJson){
    Gson gson = new Gson();
    Listing lst = gson.fromJson(listingJson, Listing.class);
    listings.add(lst);
  }

/*
  Scanning through all the Listings, mapping each Listing to its manufacturer and ask the manufacturer to 
  check if any product of it matches the listing. If matches, the manufacturer adds the listing to the 
  product.
*/
  public void match(){
    int count = 0;
    for(Listing lst:listings){
      String man_str = unifyString(lst.get_manufacturer());      
      if(manufacture_table.containsKey(man_str)){
        Manufacturer man = manufacture_table.get(man_str);
        if(man.matchProduct(lst)) count++;
      }
    }
  }

/*
  Scanning through all manufacturers. For each manufacturer, print all the results of its products in json.
*/
  public void printResult(){
    Iterator it = manufacture_table.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pairs = (Map.Entry)it.next();
      Manufacturer man = (Manufacturer)pairs.getValue();
      ArrayList<Product>products= man.get_products();
      printProducts(products);
    }
    outPut.close();
  }

/*
  Printing helper, print each product result from an array of products.
*/
  public void printProducts(ArrayList<Product> products){
      for(Product prod:products){
        Product.ProductListings result = prod.get_listings();
        Gson gson = new Gson();
        String json = gson.toJson(result);  
        outPut.println(json);
      }
  }

/*
  unifying strings to same format.
*/
  public String unifyString(String str){
     if (str==null) str="null";
     str = str.toUpperCase();
     str = str.replaceAll("-", "");
     str = str.replaceAll("_", "");
     str = str.replaceAll(" ", "");
     str = str.replaceAll("CANADA", "");
     str = str.replaceAll("USA", "");
     return str;
  }
}