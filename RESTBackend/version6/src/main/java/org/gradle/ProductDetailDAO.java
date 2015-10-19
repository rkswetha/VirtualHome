package org.gradle;

import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ProductDetailDAO {

	private final MongoCollection<Document> prodCollection;
//	 private final MongoCollection<Document> userPrefCollection;
    
    public ProductDetailDAO(final MongoDatabase blogDatabase) {
            prodCollection = blogDatabase.getCollection("productdetails");
//            userPrefCollection = blogDatabase.getCollection("preferences");
            prodCollection.drop();
    }
    
    public void save(ProductDetail prod){

        Document document=new Document("_id",prod.getProduct_id());
        document.append("name", prod.getProductName());
        document.append("category", prod.getProductCategory());
        document.append("description", prod.getProductDesc());
        document.append("price", prod.getProductPrices());
        document.append("URL", prod.getProductUrl());
        prodCollection.insertOne(document);
    }
    
    public String getProductByCategory(String category){
        FindIterable<Document> find = prodCollection.find(new Document("category",category));
    	if(find.first() == null)
    		return null;
//        Document userDoc = find.first();
//        User user=new User();
//        user.setUser_id(userDoc.getInteger("_id"));
//        user.setPassword(userDoc.getString("password"));
//        user.setEmail(userDoc.getString("email"));
//        user.setCreated_at(userDoc.getString("created_at"));
    	System.out.println("The product details are :" + find.toString());
        return find.toString();
    }

    
    public void updateProduct(ProductDetail prod, int id) {
//    	System.out.println("The user details are :" + prod.email + user.password + id);
    	FindIterable<Document> find = prodCollection.find(new Document("_id",id));
    	Document document = find.first();
    	document.append("name", prod.getProductName());
    	document.append("prices", prod.getProductPrices());
    	document.append("description", prod.getProductDesc());
    	document.append("URL", prod.getProductUrl());
    	Document setDocument=new Document("$set",document);
    	prodCollection.updateOne(new Document("_id",id), setDocument);
    }
    
    public void deleteProduct(int userId){
        prodCollection.deleteOne(new Document("_id",userId));
    }
}
