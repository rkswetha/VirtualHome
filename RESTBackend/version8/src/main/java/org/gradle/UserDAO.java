package org.gradle;

import java.util.ArrayList;
import java.util.Arrays; 
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.Block;
import org.gradle.*;

public class UserDAO {

	 private final MongoCollection<Document> userCollection;
	 private final MongoCollection<Document> userPrefCollection;
     
     public UserDAO(final MongoDatabase blogDatabase) {
             userCollection = blogDatabase.getCollection("userdetails");
             userPrefCollection = blogDatabase.getCollection("preferences");
             userCollection.drop();
     }
    
     public boolean save(User user){

         System.out.println("Input user registration entry :" + user.email + user.password);
         long count = userCollection.count(new Document("email",user.email));
         System.out.println("Duplicate user registration entry :" +count);
     
	 if(count == 0)
         {
           System.out.println("Not Duplicate user registration entry :" + user.email + user.password);
           Document document=new Document("_id",user.getUser_id());
           document.append("email", user.getEmail());
           document.append("password", user.getPassword());
           document.append("created_at", user.getCreated_at());
           userCollection.insertOne(document);
           return true;
        }
        else
           return false;
     }
 
     public User getUser(int id){
     
         System.out.println(" getUser called for id" + id);
         FindIterable<Document> find = userCollection.find(new Document("_id",id));
         Document userDoc = find.first();
         User user=new User();
         user.setUser_id(userDoc.getInteger("_id"));
         user.setPassword(userDoc.getString("password"));
         user.setEmail(userDoc.getString("email"));
         user.setCreated_at(userDoc.getString("created_at"));
         return user;
     }

     
     public void updateUser(User user, int id) {
    	 System.out.println("The user details are :" + user.email + user.password + id);
    	 FindIterable<Document> find = userCollection.find(new Document("_id",id));
         Document document = find.first();
         document.append("email", user.getEmail());
         document.append("password", user.getPassword());
         Document setDocument=new Document("$set",document);
         userCollection.updateOne(new Document("_id",id), setDocument);
     }
     
     public void deleteUser(int userId){
         userCollection.deleteOne(new Document("_id",userId));
     }

}
