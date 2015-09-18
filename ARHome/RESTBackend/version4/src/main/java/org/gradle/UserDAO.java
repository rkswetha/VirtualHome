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
         //document.append("name", user.getName());
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
         //user.setName(userDoc.getString("name"));
         user.setPassword(userDoc.getString("password"));
         user.setEmail(userDoc.getString("email"));
         user.setCreated_at(userDoc.getString("created_at"));
         return user;
     }

     public Integer authUser(User user){
         FindIterable<Document> find = userCollection.find(new Document("email",user.getEmail()));
         Document userDoc = find.first();
         if(userDoc.isEmpty())
         {
        	 System.out.println("Invalid email");
        	 return -1;
         }
         if(user.password.equals(userDoc.getString("password")))
         {
        
//       	     FindIterable<Document> userPref = userPrefCollection.find(new Document("user_id",user.getUser_id()));
//             Document userPrefDoc = userPref.first();
//             UserPreference userPrefObj=new UserPreference();
//             userPrefObj.setUser_id(userPrefDoc.getInteger("_id"));
//             userPrefObj.setUser_id		(userDoc.getInteger("user_id"));
//             userPrefObj.setSex			(userDoc.getString("gender"));
//             userPrefObj.setFamily		(userDoc.getString("family"));
//             userPrefObj.setProfession		(userDoc.getString("profession"));
//             userPrefObj.setGardening 		(userDoc.getBoolean("gardening", true));
//             userPrefObj.setInteriorDesign	(userDoc.getBoolean("interiorDesign", true));
//             userPrefObj.setCooking 		(userDoc.getBoolean("cooking", true));
//             userPrefObj.setPainting 		(userDoc.getBoolean("painting", true));
//             userPrefObj.setReading 		(userDoc.getBoolean("reading", true));
//             userPrefObj.setMusic 		(userDoc.getBoolean("music", true));
//       		return userPrefObj;
             return userDoc.getInteger("_id");
         }
         System.out.println("Invalid Password");
         return -1;
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
