package org.gradle;

import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;

public class UserTransactionDAO {

	private final MongoCollection<Document> userTranCollection;
	private final MongoCollection<Document> productCollection;
    
    public UserTransactionDAO(final MongoDatabase blogDatabase) {
            userTranCollection = blogDatabase.getCollection("usertransactions");
            productCollection = blogDatabase.getCollection("productdetails");
            
            //userTranCollection();
    }
    
    //New creation of id in the database
    public void save(UserTransaction userTran){

        Document document=new Document("user_id",userTran.getUser_id());
        System.out.println("inside save:"+userTran.getUser_id());
        System.out.println("inside save:"+userTran.getProduct_id());
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(userTran.getProduct_id());
        document.append("product_id",a);
        //document.append("product_id",userTran.getProduct_id());
        userTranCollection.insertOne(document);
    } 
    
    public String createJsonArray(ArrayList<Integer> productList){
    	  	
    	for(Integer product: productList)
    	System.out.println("Product in list"+product);
    	
    	final JSONArray jsonarray = new JSONArray();
		JSONObject mainObj = new JSONObject();
		mainObj.put("total_results_count",productList.size());
		
		//call dB and get corresponding product information for  a  given product id	
		 String url = "";
		
		for(Integer product:productList)
		{
			//DB access:
				System.out.println(" Mogo called for id" + product);
				//Checking if the product exists in DB
				 
	         FindIterable<Document> find = productCollection.find(new Document("_id",product));
	         Document prodDoc = find.first();     
	         url = prodDoc.getString("url");     
	         System.out.println(url);
	         
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("url", url);
			jsonarray.put(jsonobj);
		}
		
	
		
		mainObj.put("results", jsonarray);
		System.out.println("Results segment appended condition");
		System.out.println("JSON data created from MongoDB");
		System.out.println(mainObj.toString());

		return mainObj.toString();
    	
    }

    public String updateUserTransaction(UserTransaction userTran, int id) {
    	
    	System.out.println("inside the update: the id: "+id);
    	long count = userTranCollection.count(new Document("user_id",id));
    	String JsonString="";
    	if(count==0)
    	{
    		//No user entry
    		Document document=new Document("user_id",userTran.getUser_id());
            ArrayList<Integer> a = new ArrayList<Integer>();
            a.add(userTran.getProduct_id());
            document.append("product_id",a);
            //document.append("product_id",userTran.getProduct_id());
            userTranCollection.insertOne(document);
            ArrayList<Integer> productList = new ArrayList<Integer>();
            JsonString = createJsonArray(productList);
            return JsonString;
    	}
    	else
    	{
    		 	FindIterable<Document> find = userTranCollection.find(new Document("user_id",id));
    	        Document userTranDoc = find.first();
    	        ArrayList<Integer> productList = (ArrayList<Integer>)userTranDoc.get("product_id");
    	           
    	        System.out.println(productList.size());
    	        //Check the array length to be sent (if >3 send last 3 chosen images)
    	        if(productList.size()>3)
    	        {
    	        	ArrayList<Integer> productListTemp = new ArrayList<Integer>();
    	        	for(int i =0;i< 3;i++)
    	        	{
    	        		int index =(productList.size()-i-1);
    	        		System.out.println(index);
    	        		productListTemp.add(productList.get(index));
    	        		
    	        	}
    	        	
    	        	productList=productListTemp;
    	        }
    	        
    	        JsonString= createJsonArray(productList); 
    	}
    	
    	//Finally update the latest image
    	userTranCollection.updateOne(new Document("user_id",id),new Document("$addToSet",new Document("product_id",userTran.getProduct_id())));
        
    	return JsonString;
        
    }
    
    
    public void deleteUserTran(int userId){
        userTranCollection.deleteOne(new Document("user_id",userId));
    }
}
