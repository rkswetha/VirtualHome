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

import org.gradle.*;

public class UserDAO {

	 private final MongoCollection<Document> userCollection;
	 private final MongoCollection<Document> userPrefCollection;
     
     public UserDAO(final MongoDatabase blogDatabase) {
             userCollection = blogDatabase.getCollection("userdetails");
             userPrefCollection = blogDatabase.getCollection("preferences");
             userCollection.drop();
     }
     
     public void save(User user){

         Document document=new Document("_id",user.getUser_id());
         document.append("email", user.getEmail());
         document.append("password", user.getPassword());
         document.append("created_at", user.getCreated_at());
         userCollection.insertOne(document);
     }
     
     public User getUser(int id){
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
