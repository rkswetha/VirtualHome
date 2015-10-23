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
import com.mongodb.Block;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailDAO {

	private final MongoCollection<Document> prodCollection;

	public ProductDetailDAO(final MongoDatabase blogDatabase) {
		prodCollection = blogDatabase.getCollection("productdetails");
		prodCollection.drop();
	}

	public void save(ProductDetail prod){

		Document document=new Document("_id",	prod.getProduct_id());
		document.append("name", 		prod.getName());
		document.append("category", 		prod.getCategory());
		document.append("description", 		prod.getDescription());
		document.append("price", 		prod.getPrices());
		document.append("url", 			prod.getUrl());
		prodCollection.insertOne(document);
	}

	public String getProductByCategory(String category){

		final JSONArray jsonarray = new JSONArray();
		FindIterable<Document> find = prodCollection.find(new Document("category",category));
		JSONObject mainObj = new JSONObject();
		mainObj.put("total_results_count", jsonarray.length());
		mainObj.put("code", "OK");
		mainObj.put("offset", "0");
		if(!((category.equals("sofa")) || (category.equals("chairs")) || (category.equals("diningset")) || (category.equals("table")) || (category.equals("bedroom")) || (category.equals("organization")) || (category.equals("intdesign")) || (category.equals("misc")) || (category.equals("datamining"))))
		{
			System.out.println(mainObj.toString());
			System.out.println("No correct category selected condition category = "+category);
			return mainObj.toString();
		}
		if(find.first() == null)
		{
			System.out.println(mainObj.toString());
			System.out.println("result set empty condition");
			return mainObj.toString();
		}
			
		find.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {
				System.out.println(doc);
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("name", doc.getString("name"));
				jsonobj.put("category", doc.getString("category"));
				jsonobj.put("description", doc.getString("description"));
				jsonobj.put("price", doc.getString("price"));
				jsonobj.put("url", doc.getString("url"));
				jsonarray.put(jsonobj);
			}
		});
		
		mainObj.put("results", jsonarray);
		System.out.println("Results segment appended condition");
		System.out.println("JSON data created from MongoDB");
		System.out.println(mainObj.toString());

		return mainObj.toString();
	}


	public void updateProduct(ProductDetail prod, int id) {
		FindIterable<Document> find = prodCollection.find(new Document("_id",id));
		Document document = find.first();
		document.append("name", prod.getName());
		document.append("prices", prod.getPrices());
		document.append("description", prod.getDescription());
		document.append("url", prod.getUrl());
		Document setDocument=new Document("$set",document);
		prodCollection.updateOne(new Document("_id",id), setDocument);
	}

	public void deleteProduct(int userId){
		prodCollection.deleteOne(new Document("_id",userId));
	}
}
