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

public class UserDAO {

	 private final MongoCollection<Document> userCollection;
     
     public UserDAO(final MongoDatabase blogDatabase) {
             userCollection = blogDatabase.getCollection("userdetails");
             
             //modCollection.drop();
     }
     
     public void save(User user){

         Document document=new Document("_id",user.getUser_id());
         document.append("name", user.getName());
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
         user.setName(userDoc.getString("name"));
         user.setPassword(userDoc.getString("password"));
         user.setEmail(userDoc.getString("email"));
         user.setCreated_at(userDoc.getString("created_at"));
         return user;
     }

     public void updateUser(User user, int id) {
    	 System.out.println("The user details are :" + user.email + user.password + id);
    	 FindIterable<Document> find = userCollection.find(new Document("_id",id));
         Document document = find.first();
         //Document document=new Document("_id",id);
//         System.out.println(document.toString());
         document.append("email", user.getEmail());
         document.append("password", user.getPassword());
//         System.out.println(document.toString());
         Document setDocument=new Document("$set",document);
//         System.out.println(setDocument.toString());
         userCollection.updateOne(new Document("_id",id), setDocument);
     }
     
//     public List<User> getUsers(){
////      
//    	 
//         return users;
// }

     
     public void deleteUser(int userId){
         userCollection.deleteOne(new Document("_id",userId));
     }

}