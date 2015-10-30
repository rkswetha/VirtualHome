package org.gradle;

import java.util.Arrays;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UserPreferenceDAO {

	private final MongoCollection<Document> userPrefCollection;
    
    public UserPreferenceDAO(final MongoDatabase blogDatabase) {
            userPrefCollection = blogDatabase.getCollection("preferences");
            
            //userPrefCollection.drop();
    }
    
    
    public void save(UserPreference userPref){

        Document document=new Document("user_id",userPref.getUser_id());
        document.append("gender", 		userPref.getSex());
        document.append("family", 		userPref.getFamily());
        document.append("profession", 		userPref.getProfession());
        document.append("gardening", 		userPref.getGardening());
        document.append("interiorDesign", 	userPref.getInteriorDesign());
        document.append("cooking", 		userPref.getCooking());
        document.append("painting", 		userPref.getPainting());
        document.append("reading", 		userPref.getReading());
        document.append("music", 		userPref.getMusic());
        userPrefCollection.insertOne(document);
    }
    
    public UserPreference getUserPreference(int id){
        FindIterable<Document> find = userPrefCollection.find(new Document("user_id",id));
        Document userDoc = find.first();
        UserPreference userPref=new UserPreference();
        userPref.setUser_id		(userDoc.getInteger("user_id"));
        userPref.setSex			(userDoc.getString("gender"));
        userPref.setFamily		(userDoc.getString("family"));
        userPref.setProfession		(userDoc.getString("profession"));
        userPref.setGardening 		(userDoc.getBoolean("gardening", true));
        userPref.setInteriorDesign	(userDoc.getBoolean("interiorDesign", true));
        userPref.setCooking 		(userDoc.getBoolean("cooking", true));
        userPref.setPainting 		(userDoc.getBoolean("painting", true));
        userPref.setReading 		(userDoc.getBoolean("reading", true));
        userPref.setMusic 		(userDoc.getBoolean("music", true));
        return userPref;
    }

    public void updateUserPreference(UserPreference userPref, int id) {
    	FindIterable<Document> find = userPrefCollection.find(new Document("user_id",id));
        Document document = find.first();
        document.append("user_id", 		userPref.getUser_id());
        document.append("gender", 		userPref.getSex());
        document.append("family", 		userPref.getFamily());
        document.append("profession", 		userPref.getProfession());
        document.append("gardening", 		userPref.getGardening());
        document.append("interiorDesign", 	userPref.getInteriorDesign());
        document.append("cooking", 		userPref.getCooking());
        document.append("painting", 		userPref.getPainting());
        document.append("reading", 		userPref.getReading());

        Document setDocument=new Document("$set",document);
        userPrefCollection.updateOne(new Document("_id",id), setDocument);
    }
    
    
    public void deleteUserPref(int userId){
        userPrefCollection.deleteOne(new Document("user_id",userId));
    }
}
