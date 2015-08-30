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
            
            //modCollection.drop();
    }
    
    public void save(UserPreference userPref){

        Document document=new Document("_id",userPref.getIdentity());
        document.append("ethnicity", userPref.getEthnicity());
        document.append("email", userPref.getEmail());
        document.append("sex", userPref.getSex());
        document.append("options", userPref.getOptions());
        userPrefCollection.insertOne(document);
    }
    
    public UserPreference getUserPreference(int id){
        FindIterable<Document> find = userPrefCollection.find(new Document("_id",id));
        Document userDoc = find.first();
        ArrayList<String> options = new ArrayList<String>();
        UserPreference userPref=new UserPreference();
        userPref.setIdentity(userDoc.getInteger("_id"));
        userPref.setEthnicity(userDoc.getString("ethnicity"));
        userPref.setSex(userDoc.getString("sex"));
        userPref.setEmail(userDoc.getString("email"));
        //userPref.setOptions(userDoc.get("options", options));
        return userPref;
    }

    public void updateUserPreference(UserPreference userPref) {
        Document document=new Document("email", userPref.getEmail());
        document.append("options", userPref.getOptions());
        document.append("sex", userPref.getSex());
        document.append("ethnicity", userPref.getEthnicity());
        Document setDocument=new Document("$set",document);
        userPrefCollection.updateOne(new Document("_id",userPref.getIdentity()), setDocument);
    }
    
//    public List<User> getUsers(){
////     
//   	 
//        return users;
//}

    
    public void deleteUserPref(int userId){
        userPrefCollection.deleteOne(new Document("_id",userId));
    }
}
