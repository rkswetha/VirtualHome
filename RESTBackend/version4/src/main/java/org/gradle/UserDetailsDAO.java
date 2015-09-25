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

public class UserDetailsDAO {
	private final MongoCollection<Document> userCollection;
	private final MongoCollection<Document> userPrefCollection;
    
    public UserDetailsDAO(final MongoDatabase blogDatabase) {
            userCollection = blogDatabase.getCollection("userdetails");
            userPrefCollection = blogDatabase.getCollection("preferences");
            //modCollection.drop();
    }
    
//    public void save(User user){
//
//        Document document=new Document("_id",user.getUser_id());
//        document.append("name", user.getName());
//        document.append("email", user.getEmail());
//        document.append("password", user.getPassword());
//        document.append("created_at", user.getCreated_at());
//        userCollection.insertOne(document);
//    }
    
    public UserDetails getUserDetails(int id){
        FindIterable<Document> find = userCollection.find(new Document("_id",id));
        FindIterable<Document> findPref = userPrefCollection.find(new Document("user_id",id));
        Document userDoc = find.first();
        Document userPrefDoc = findPref.first();
        System.out.println(userDoc.toString());
        System.out.println(userPrefDoc.toString());
        UserDetails userDet=new UserDetails();
//        UserPreference userPref = new UserPreference();
//        userDet.user.setUser_id(userDoc.getInteger("_id"));
//        userDet.user.setName(userDoc.getString("name"));
//        userDet.user.setPassword(userDoc.getString("password"));
//        userDet.user.setEmail(userDoc.getString("email"));
//        userDet.user.setCreated_at(userDoc.getString("created_at"));
//        userDet.userPref.setEthnicity(userPrefDoc.getString("ethnicity"));
//        userDet.userPref.setSex(userPrefDoc.getString("sex"));
//        userDet.userPref.setOptions(userPrefDoc.getString("options"));
        
        return userDet;
    }

//    public void updateUser(User user, int id) {
//   	 System.out.println("The user details are :" + user.email + user.password + id);
//   	 FindIterable<Document> find = userCollection.find(new Document("_id",id));
//        Document document = find.first();
//        //Document document=new Document("_id",id);
////        System.out.println(document.toString());
//        document.append("email", user.getEmail());
//        document.append("password", user.getPassword());
////        System.out.println(document.toString());
//        Document setDocument=new Document("$set",document);
////        System.out.println(setDocument.toString());
//        userCollection.updateOne(new Document("_id",id), setDocument);
//    }
//    
////    public List<User> getUsers(){
//////     
////   	 
////        return users;
////}
//
//    
//    public void deleteUser(int userId){
//        userCollection.deleteOne(new Document("_id",userId));
//    }

}
