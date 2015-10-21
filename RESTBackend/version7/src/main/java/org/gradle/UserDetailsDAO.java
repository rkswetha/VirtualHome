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
    
    public UserDetails getUserDetails(int id){

        UserDetails userDet=new UserDetails();

        return userDet;
    }

    public UserDetails authUser(UserDetails user){

    	System.out.println("Inside auth user");

        long count = userCollection.count(new Document("email",user.getEmail()));
	if(count == 0)
       	{
	   System.out.println("Invalid email");
 	   return null;
        }
	else
        {
           FindIterable<Document> find = userCollection.find(new Document("email",user.getEmail()));
           Document userDoc = find.first();
           System.out.println(userDoc.toString());
	   if(user.password.equals(userDoc.getString("password")))
           {
             count = userPrefCollection.count(new Document("user_id",userDoc.getInteger("_id"))); 
 	     if(count == 0)
             {
	       System.out.println(" User hasnt configured preference previously:" + user.email + user.password);
	       System.out.println(" Reply with default preference" + user.email + user.password);
               //return null; // TODO: Need to set all preference parameters as true 
	       
               UserDetails userDet=new UserDetails();
               userDet.setUser_id(userDoc.getInteger("_id"));
               userDet.setSex("Male");
               userDet.setFamily("Single");
               userDet.setProfession("IT field");
               userDet.setGardening(true);
               userDet.setInteriorDesign(true);
               userDet.setCooking(true);
               userDet.setPainting(true);
               userDet.setReading(true);
               userDet.setMusic(true);
               return userDet;
             }
	     else 
	     {
               System.out.println(" Retreiving preference for given user:" + user.email + user.password);
      	       FindIterable<Document> userPref = userPrefCollection.find(new Document("user_id",userDoc.getInteger("_id")));
               Document userPrefDoc = userPref.first();
      
               System.out.println(userPrefDoc.toString());
               UserDetails userDet=new UserDetails();
               userDet.setUser_id(userPrefDoc.getInteger("user_id"));
               userDet.setSex(userPrefDoc.getString("gender"));
               userDet.setFamily(userPrefDoc.getString("family"));
               userDet.setProfession(userPrefDoc.getString("profession"));
               userDet.setGardening(userPrefDoc.getBoolean("gardening", true));
               userDet.setInteriorDesign(userPrefDoc.getBoolean("interiorDesign", true));
               userDet.setCooking(userPrefDoc.getBoolean("cooking", true));
               userDet.setPainting(userPrefDoc.getBoolean("painting", true));
               userDet.setReading(userPrefDoc.getBoolean("reading", true));
               userDet.setMusic(userPrefDoc.getBoolean("music", true));
               return userDet;
             }
	  }
	  else
	  {
            System.out.println("Invalid Password");
            return null;
	  }
	}
    }
}
