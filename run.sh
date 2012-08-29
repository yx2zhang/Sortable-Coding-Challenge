#!/bin/bash
javac -cp :google-gson-2.2.2/gson-2.2.2.jar Main.java ProductMatcher.java Product.java Listing.java
java -cp :google-gson-2.2.2/gson-2.2.2.jar Main data/products.txt data/listings.txt