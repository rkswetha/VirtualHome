package org.gradle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.ClusterMembership;
import weka.filters.unsupervised.attribute.Remove;


public class ProductRecommendationDAO {

	private final MongoCollection<Document> productCollection;
    
    public ProductRecommendationDAO(final MongoDatabase blogDatabase) {
    	productCollection = blogDatabase.getCollection("productdetails");            
    }
    
    
    public String datamine(ProductRecommendation prodDetails){
    	System.out.println("The sent product id:"+prodDetails.getProductID());
    	String result=getRecomProducts(prodDetails);
    	return result;
    	
        
    }
    
    public String getRecomProducts(ProductRecommendation prodDetails){
    	
    	ArrayList<Long>items = new ArrayList<Long>();
    	
    	//Datamining
    	String dataset = "dataset-all.arff";
		try {
			DataSource dataSource = new DataSource(dataset);
			Instances data = dataSource.getDataSet();
			SimpleKMeans model = new SimpleKMeans();
			
			model.setNumClusters(10);
			 AddCluster filtro= new AddCluster(); 
		       filtro.setClusterer(model); // 
		       filtro.setInputFormat(data); 
		       
		     //Here in train - we need to get attributes starting with index 1
		       filtro.setIgnoredAttributeIndices("1,11,12,13");
		       
		       
		       Filter.useFilter(data,filtro); 
		       Instances newInstances = Filter.useFilter(data, filtro); 

		       for (int i=0; i<newInstances.numInstances();i++ ) 
		       { 
		         Instance row = newInstances.instance(i); 
		         int attrCount = row.numAttributes(); 
		        // System.out.println(attrCount);
		         Attribute att = row.attribute(attrCount-1);
		         
		         Instance rowOld = data.instance(i); 
		        
		         

		         System.out.println("Row "+i+" "+"Userid " 
		        		 +row.value(row.attribute(0))+" "+"Cluster " 
		        		 +row.stringValue(attrCount-1)); 
		                                                        
		       }
		       
		      System.out.println(model);
		 
		    //Obtain test data	    
		     Instances testData = createTestDataset(prodDetails);
		     //Evaluate cluster
		     ClusterEvaluation clsEval = new ClusterEvaluation();
		     
		     //Another method of creating test file:
		 /*    try{
		     File file = new File("test-new.arff");
		     file.createNewFile();
		     
		     if(!file.exists())
		     {
		    	 file.createNewFile();
		     }
		     FileWriter fw = new FileWriter(file.getAbsoluteFile());
		     BufferedWriter bw = new BufferedWriter(fw);
		     bw.write(testData.toString());
		     bw.close();
		     }catch(IOException e)
		     {
		    	 e.printStackTrace();
		     }
			
			//Evaluate the cluster
			ClusterEvaluation clsEval = new ClusterEvaluation();
			String datasetTest = "test-new.arff";
			DataSource dataSource1 = new DataSource(datasetTest);*/
		     
		     
			
			DataSource dataSource1 = new DataSource(testData);
			Instances data1 = dataSource1.getDataSet();
			
			Remove atr = new Remove();
			//Here in test - we need to get attributes starting with index 0
			int[] attributeToIgnore = {0,10,11,12};
			atr.setAttributeIndicesArray(attributeToIgnore);
			atr.setInputFormat(data1);
			
			 Instances filteredInstances = Filter.useFilter(data1, atr); 
			 
			//Second method of evaluation
			/*clsEval.setClusterer(model);
			clsEval.evaluateClusterer(filteredInstances);	
			System.out.println("***********Filtered instances*************");
			System.out.println(filteredInstances);
		
			System.out.println("RESULTS: "+clsEval.clusterResultsToString());
			System.out.println("************");
			double[] results=clsEval.getClusterAssignments();
			
			for(double result:results)
			{
				System.out.println(result);
			}
			*/
			
			int firstClus=model.clusterInstance(filteredInstances.firstInstance());
			System.out.println("Assigned Cluster: "+firstClus);
			
			
			//call to the getItemList
			String clusterId ="cluster"+(firstClus+1);
			 items=getItemList(newInstances, clusterId, prodDetails.getProductID());
			
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
   	

		final JSONArray jsonarray = new JSONArray();
		JSONObject mainObj = new JSONObject();
		
		//call dB and get corresponding product information for  a  given product id	
		 String url = "";
		
		for(Long image:items)
		{
			//DB access:
				System.out.println(" getUser called for id" + image);
	         FindIterable<Document> find = productCollection.find(new Document("_id",image));
	         Document prodDoc = find.first();     
	         url = prodDoc.getString("url");      
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
    
    
    
    
    public static Instances createTestDataset(ProductRecommendation prodDetails){
    	
    	//Getting the input params
	      Integer userID= prodDetails.getUser_id();
	      String sex= prodDetails.getSex();
	      String family= prodDetails.getFamily();
	      String proff= prodDetails.getProfession();
	      Boolean gardening= prodDetails.getGardening();
	      Boolean painting= prodDetails.getPainting();
	      Boolean intD= prodDetails.getInteriorDesign();
	      Boolean cooking= prodDetails.getCooking();
	      Boolean reading= prodDetails.getReading();
	      Boolean music= prodDetails.getMusic();
	      
	      
	      //Print the data
	      System.out.println("INPUT from getter in DAO "+userID+" "+sex+" "+family+" "+proff+" "+gardening+" "+painting+" "+intD+" "+cooking+" "+reading+" "+music);
    	
    	
    	
    	
    	FastVector      atts;
	    FastVector      attVals;
	    FastVector      attFamilyVals;
	    FastVector      attProffVals;
	    FastVector 		attGardeningValues;
	    FastVector 		attPaintingValues;
	    FastVector 		attIDValues;
	    FastVector 		attCookingValues;
	    FastVector 		attReadingValues;
	    FastVector 		attMusicValues;
	    Instances       data;
	    double[]        vals;

	    // 1. set up attributes
	    atts = new FastVector();
	    // - numeric
	    atts.addElement(new Attribute("userid"));
	    
	    // - nominal
	    attVals = new FastVector();
	    attVals.addElement("female");
	    attVals.addElement("male");
	    atts.addElement(new Attribute("gender", attVals));
	    
	    
	    //Family
	    attFamilyVals = new FastVector();
	    attFamilyVals.addElement("single");
	    attFamilyVals.addElement("familywithboykids");
	    attFamilyVals.addElement("family");
	    attFamilyVals.addElement("familywithgirlkids");
	    atts.addElement(new Attribute("family", attFamilyVals));
	    
	    
	    
	    //Profession
	    attProffVals = new FastVector();
	    attProffVals.addElement("itfield");
	    attProffVals.addElement("business");
	    attProffVals.addElement("educational");
	    attProffVals.addElement("homemaker");
	    atts.addElement(new Attribute("profession", attProffVals));
	    
	    
	    //Gardening
	    attGardeningValues = new FastVector();
	    attGardeningValues.addElement("FALSE");
	    attGardeningValues.addElement("TRUE");
	    atts.addElement(new Attribute("gardening", attGardeningValues));
	    
	    
	    //Painting
	    attPaintingValues = new FastVector();
	    attPaintingValues.addElement("TRUE");
	    attPaintingValues.addElement("FALSE");
	    atts.addElement(new Attribute("painting", attPaintingValues));
	    
	    
	    //InteriorDesign
	    attIDValues = new FastVector();
	    attIDValues.addElement("TRUE");
	    attIDValues.addElement("FALSE");
	    atts.addElement(new Attribute("interiordesign", attIDValues));
	    
	    //Cooking
	    attCookingValues = new FastVector();
	    attCookingValues.addElement("FALSE");
	    attCookingValues.addElement("TRUE");
	    atts.addElement(new Attribute("cooking", attCookingValues));
	  
	    //Reading
	    attReadingValues = new FastVector();
	    attReadingValues.addElement("TRUE");
	    attReadingValues.addElement("FALSE");
	    atts.addElement(new Attribute("reading", attReadingValues));
	    
	    //Music
	    attMusicValues = new FastVector();
	    attMusicValues.addElement("TRUE");
	    attMusicValues.addElement("FALSE");
	    atts.addElement(new Attribute("music", attMusicValues));
	    
	    
	    //Numeric items
	    atts.addElement(new Attribute("item1"));
	    atts.addElement(new Attribute("item2"));
	    atts.addElement(new Attribute("item3"));
	    
	    
	   //Adding the intance variable
	    
	    // 2. create Instances object
	    data = new Instances("ProductPrediction", atts, 0);

	    // 3. fill with data
	    // first instance
	    //Numeric data fields
	    vals = new double[data.numAttributes()];
	    System.out.println(data.numAttributes());
	    vals[0] =userID;
	    vals[1]=attVals.indexOf(sex);
	    vals[2]=attFamilyVals.indexOf(family);
	    vals[3]=attProffVals.indexOf(proff);
	    vals[4]=attGardeningValues.indexOf(gardening.toString().toUpperCase());
	    vals[5]=attPaintingValues.indexOf(painting.toString().toUpperCase());
	    vals[6]=attIDValues.indexOf(intD.toString().toUpperCase());
	    vals[7]=attCookingValues.indexOf(cooking.toString().toUpperCase());
	    vals[8]=attReadingValues.indexOf(reading.toString().toUpperCase());
	    vals[9]=attMusicValues.indexOf(music.toString().toUpperCase());
	    vals[10]=123485;
	    vals[11]=123507;
	    vals[12]=123516;
	    
	    System.out.println("Values: "+vals[4]+vals[5]+vals[6]);
	    
   
	    data.add(new Instance(1.0, vals));
	    System.out.println(data);
	    
	    return data;
    	
    	
    }
    
    public static ArrayList<Long> getItemList(Instances data, String clusterID, long chosenProductID)
	{
		ArrayList<Long> items= new ArrayList<Long>();
		ArrayList<Long> usersInList= new ArrayList<Long>();
		Set<Long> totalItems = new HashSet<Long>();
		
		//For now
		
		
		
		//geting the users of the given clusters
		for (int i=0; i<data.numInstances();i++ ) 
	       { 
	         Instance row = data.instance(i); 
	         int attrCount = row.numAttributes(); 
	         
	         //getting the userid list
	         //Clusterid attribute
	         String currClustID= row.stringValue(attrCount-1);
	         if(currClustID.equals(clusterID))
	         {
	        	 usersInList.add((long)row.value(row.attribute(0)));
	        	//Adding the images into hashset for now
	        	
	        	 totalItems.add((long)row.value(row.attribute(10)));
	        	 totalItems.add((long)row.value(row.attribute(11)));
	        	 totalItems.add((long)row.value(row.attribute(12)));
	        	 
	        	/* System.out.println("user id: "+row.value(row.attribute(0)));
	        	 System.out.println("images: "+row.value(row.attribute(10)));
	        	 System.out.println("images: "+row.value(row.attribute(11)));
	        	 System.out.println("images: "+row.value(row.attribute(12)));*/
	        	 
	         }       
	       }
		
		
		//Print the userIDs:
		for(Long id:usersInList)
		{
			System.out.println("User id: "+id);			
		}
		
		//Print the total images set:
		System.out.println("The images are");
		for(Long image:totalItems)
		{
			System.out.println(image);			
		}
		
		//Pick up a random number in the set size range
		Random rand=new Random();
		Long[] list = (Long[]) totalItems.toArray(new Long[totalItems.size()]);
		
		for(int i=0;i<3;i++)
		{
		int n = rand.nextInt(totalItems.size()-1)+1;
		if(list[n]==chosenProductID)
		{
			n=n+1;
		}
		items.add(list[n]);		
		
	}
		
		System.out.println("Final chosen items");
		for(Long image:items)
		{
			System.out.println(image);
		}
		
		return items;
	}
    
    
    
    
}
