import java.io.*;
import java.util.*;

/*
  Manufacturer used to store products and map a listing to a product based on the title of the listing.
*/

public class Manufacturer{
  //Store all the products of the manufacturer inside the arraylist products.
  private ArrayList <Product> products;
  
  /*
    Mapping a family name string to all the models of the family. The models of a specific family is stored inside a 
    hashmap model_map. The model_map maps the model name string to a product id int.
  */
  private HashMap<String,HashMap> family_models_map;
  
  //next available product id.
  private int next_id;
  
  // name of Manufacturer
  private String name;
  
/*
  Constructor creates all the data members
*/
  public  Manufacturer(String name){
    this.name = name;
    family_models_map = new HashMap <String,HashMap>();
    products = new ArrayList <Product>();
  }

/*
  Adding a new product to the Manufacturer. It puts the new Product inside the products arraylist and
  sign a product id to the product. Then it finds the family the product belongs to, add the new model 
  to the family by creating a new entry that maps the model name to its product id.
*/
  public void addProduct(Product prod){ 
    if(!products.contains(prod)){
      products.add(prod);
    }
    
    String family = unifyString(prod.get_family()).replaceAll(" ", "");
    String model =  unifyString(prod.get_model());
            
    if(!family_models_map.containsKey(family)){
      HashMap<String,Integer> model_map  = new HashMap();
      family_models_map.put(family,model_map);
    }

    HashMap models_id_map = family_models_map.get(family);
    models_id_map.put(model,new Integer(next_id)); 
    next_id++;   
  }
  
// return the products arraylist
  public ArrayList<Product> get_products(){
    return products;
  }

//matching a listing to a product and adding the listing to it
  public boolean matchProduct(Listing lst){
    Product prod = findProduct(lst.get_title());
    if(prod!=null){
      prod.addListing(lst);
      return true;
    }
    return false;
  }
  
/*
  Find the product based on the title of the listing. It first scan all the families of the Manufacturer to 
  find families match the title. Then it scans all the models of these families to find the prodcut the title 
  matches.
*/

  public Product findProduct(String title){
    ArrayList <String> families = searchFamilies(title);
    int productId = -1;
    for(String family:families){
      productId = searchModels(family,title);
      if(productId>=0) break;
    }
    return (productId<0||productId>=next_id)? null:products.get(productId);
  }
  
// find all the families the listing's title matches
  public ArrayList <String> searchFamilies(String title){
    Iterator<Map.Entry<String,HashMap>> it = family_models_map.entrySet().iterator();
    ArrayList <String> families = new ArrayList<String>();
    while (it.hasNext()) {
        Map.Entry<String,HashMap> pairs = it.next();
        String family = pairs.getKey();
        if(matchWord(family,title)){
          families.add(family);
        }        
    }
    return families;
  }

//Scan all the models of a family to find the product id of the product that matches the listing's title. 
  public int searchModels(String family, String title){
    HashMap model_map = family_models_map.get(family);
    if (model_map==null) return -1;
    
    Iterator<Map.Entry<String,Integer>> it = model_map.entrySet().iterator();
    while(it.hasNext()){
        Map.Entry<String,Integer> entry = it.next();
        String model = entry.getKey();
        if(matchWord(model,title)){
          return entry.getValue();
        }        
    }
    return -1;
  }

//check if a pattern of words is inside a string. 
  public boolean matchWord(String pattern, String str){
    //unify the pattern and string
    pattern = unifyString(pattern);
    str = unifyString(str);
    
    if(str.equals("NULL")) return false;
    if(pattern.equals("NULL")) return true;
    
    //splits the pattern into an array of words.
    String[] pattern_words = pattern.split(" ");
    //in case the pattern string as a single word.
    String pattern_word = pattern.replaceAll(" ","");
    
    //spilting the string into array of words.
    String[] str_words = str.split(" ");
    int index = 0;
    
    //Scaning the string word by word, check if the words pattern appears.
    for(String str_word:str_words){
      if(index>=pattern_words.length||str_word.equals(pattern_word)) return true;
      if(str_word.equals(pattern_words[index])) {index++;}
      else index = 0;
    }
    return false;
  }
  
/*
  unifying strings to same format.
*/
  public String unifyString(String str){
    if (str==null) str = "null";
    str = str.toUpperCase();
    str = str.replaceAll("-", "");
    str = str.replaceAll("_", "");
    str = str.replaceAll("CANADA", "");
    str = str.replaceAll("USA", "");
    return str;
  }

/*
  debug functions to print the manufacturer
*/
  public void printAllFamilies(){
    System.out.println("------------"+name+"--------------");
    Iterator<Map.Entry<String,HashMap>> it = family_models_map.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<String,HashMap> pairs = it.next();
        String family = pairs.getKey();
        System.out.println(family);
        printAllModels(family);             
    }
    printAllModels("NULL"); 
    return;
  }
  
  public void printAllModels(String family){
    HashMap model_map = family_models_map.get(family);
     if (model_map==null) return;
     Iterator<Map.Entry<String,Integer>> it = model_map.entrySet().iterator();
     while(it.hasNext()){
         Map.Entry<String,Integer> entry = it.next();
         String model = entry.getKey();
         System.out.println("--"+model);  
     }
     return;
  }
}