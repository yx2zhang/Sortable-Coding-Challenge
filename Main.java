import java.io.*;
import java.util.*;

class Main{   
  public static void main(String[] args){
    
    //create a ProductMatcher object to match the listings to product. The constructor take a string as the name of output file
     ProductMatcher pMatcher = new ProductMatcher("data/output.txt");
     if(args.length < 2)
     {
         System.out.println("Proper Usage is: java Main productsInput listingsIuput");
         System.exit(0);
     }
     
     //parse the json strings from the input file use ProductMatcher pMatcher. 
     parseInput(args[0],pMatcher,0);
     parseInput(args[1],pMatcher,1);
     
     //match listing objects to product objects
     pMatcher.match();
     
     //print the result to output.txt
     pMatcher.printResult();
   }
   
  /*
    ParseInput file is used to read the inputFile line by line. For each line, it calls the ProductMatcher calss
    to parse the lines string as json. The ProductMatcher will create Product or Listing object based on the json
    string. inputFile is the name of input text file, pMatcher is the reference of a ProductMatcher, option = 1 for
    product parse and option = 1 fpr listing parse.
  */
   
  public static void parseInput(String inputFile, ProductMatcher pMatcher,int option){
     try{
        if(inputFile==null){return;}
        FileInputStream fstream = new FileInputStream(inputFile);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        
        //Read File Line By Line
        while ((strLine = br.readLine()) != null){
          //parse the json string and create related objects such as Products, Manufacturer and Listing. 
          if(option== 0)pMatcher.parseProduct(strLine);
          if(option== 1)pMatcher.parseListing(strLine);          
        }
        
        in.close();
        }catch (Exception e){
          System.err.println("Error: " + e.getMessage());
        }
  }
}
