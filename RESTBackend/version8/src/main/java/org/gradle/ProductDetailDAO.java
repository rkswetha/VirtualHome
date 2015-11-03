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

import java.awt.Color;  
import java.awt.Graphics2D;  
import java.awt.Image;  
import java.awt.Toolkit;  
import java.awt.image.BufferedImage;  
import java.awt.image.FilteredImageSource;  
import java.awt.image.ImageFilter;  
import java.awt.image.ImageProducer;  
import java.awt.image.RGBImageFilter;  
import java.io.File;  
import javax.imageio.ImageIO; 
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import com.cloudinary.*;
import java.util.Map;
import java.util.Arrays;
import com.cloudinary.utils.ObjectUtils;


public class ProductDetailDAO {

	private final MongoCollection<Document> prodCollection;

	public ProductDetailDAO(final MongoDatabase blogDatabase) {
		prodCollection = blogDatabase.getCollection("productdetails");
		//prodCollection.drop();
	}
	
	private static BufferedImage imageToBufferedImage(final Image image)  
	{  
	      final BufferedImage bufferedImage =  
	         new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);  
	      final Graphics2D g2 = bufferedImage.createGraphics();  
	      g2.drawImage(image, 0, 0, null);  
	      g2.dispose();  
	      return bufferedImage;  
	}
	
	public static Image makeColorTransparent(final BufferedImage im, final Color color)  
	{  
	      final ImageFilter filter = new RGBImageFilter()  
	      {  

	    	 Color min =new Color(170, 170, 170);
	         int mini = min.getRGB() | 0xFFAAAAAA;
	         
	         public final int filterRGB(final int x, final int y, final int rgb)  
	         {  
	           
	        	 if (((rgb | 0xFF000000) >= mini))
	             {  
	                // Mark the alpha bits as zero - transparent  
	                return 0x00FFFFFF & rgb;  
	             }  
	             else  
	             {  
	                // nothing to do  
	                return rgb;  
	             } 
	         }  
	      };  
	  
	      final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);  
	      return Toolkit.getDefaultToolkit().createImage(ip);  
	}  
	
    /*
	public static String createTransparentImage(String inputImageUrl, String prodName)   
	{  
			
	   try{ 
	     final String tempImgFileName =  "test_altcopy.jpg";  
		  
	      System.out.println("Copying file " + inputImageUrl + " to " + tempImgFileName);  
	      
	      URL in = new URL(inputImageUrl);
	      final BufferedImage source = ImageIO.read(in);  
	  
	      final int color = source.getRGB(0, 0);  
	     
	      final Image imageWithTransparency = makeColorTransparent(source, new Color(color));  
	      final BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);  
	  
	      final File out = new File(tempImgFileName);  
	      
	      ImageIO.write(transparentImage, "PNG", out);
	  	
	      // Upload the transparent image to Cloudinary and return its location Url
	  	  return uploadImageToCloudinary(tempImgFileName, prodName);
	      
		}catch(IOException e){
	    	  e.printStackTrace();
	    }
		return null;
	}  */
    
    
    private static Image TransformColorToTransparency(BufferedImage image, Color c1, Color c2)
    {
        // Primitive test, just an example
        final int r1 = c1.getRed();
        final int g1 = c1.getGreen();
        final int b1 = c1.getBlue();
        final int r2 = c2.getRed();
        final int g2 = c2.getGreen();
        final int b2 = c2.getBlue();
        ImageFilter filter = new RGBImageFilter()
        {
            public final int filterRGB(int x, int y, int rgb)
            {
                int r = (rgb & 0xFF0000) >> 16;
                int g = (rgb & 0xFF00) >> 8;
                int b = rgb & 0xFF;
                if (r >= r1 && r <= r2 &&
                    g >= g1 && g <= g2 &&
                    b >= b1 && b <= b2)
                {
                    // Set fully transparent but keep color
                    return rgb & 0xFFFFFF;
                }
                return rgb;
            }
        };
        
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
    private static BufferedImage ImageToBufferedImage(Image image, int width, int height)
    {
        BufferedImage dest = new BufferedImage(
                                               width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return dest;
    }
    
    
     public static String createTransparentImage(String inputImageUrl, String prodName)
     {
     
     try{
     final String tempImgFileName =  "test_altcopy.jpg";
     
     System.out.println("Copying file " + inputImageUrl + " to " + tempImgFileName);
     
     URL in = new URL(inputImageUrl);
     final BufferedImage source = ImageIO.read(in);
     
     Image transpImg2 = TransformColorToTransparency(source, new Color(180, 180, 180), new Color(255, 255, 255));
     
     final BufferedImage transparentImage = ImageToBufferedImage(transpImg2, source.getWidth(), source.getHeight());
         
     final File out = new File(tempImgFileName);
     
     ImageIO.write(transparentImage, "PNG", out);
     
     // Upload the transparent image to Cloudinary and return its location Url
     return uploadImageToCloudinary(tempImgFileName, prodName);
     
     }catch(IOException e){
     e.printStackTrace();
	    }
     return null;
     }
    
	
	// Method to create transparent image using Cloudinary edit options. (Not used for now)
	public static void createTransparentImageFromCloudinary(String inputFileName)
	{
		try{
		final String outputFileName =  "test_altcopy.jpg";
		
		URL in = new URL(inputFileName);
	    final BufferedImage source = ImageIO.read(in);
	     
	    final File out = new File(outputFileName);  
	    ImageIO.write(source, "JPG", out);
		
	    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", "cmpe295b",
				  "api_key", "451228766912636",
				  "api_secret", "utBUDOlhMyTFpHy0qDoudgmnggg"));
		
		Map result = cloudinary.uploader().upload(new File(outputFileName), ObjectUtils.asMap(
				  "public_id", "sample_id",
				  "transformation", new Transformation().crop("limit").width(300).height(300).effect("make_transparent")
				  ));
		String url = cloudinary.url().format("png")
				  .transformation(new Transformation().width(500).height(500).crop("fit"))
				  .generate("sample5");
		
		System.out.println("URL received from cloudinary"+url);
		
		
		}catch(IOException e){
	    	  e.printStackTrace();
	    }
		
	}
	
	
	public static String uploadImageToCloudinary(String outputFileName, String prodName)
	{
		System.out.println("uploadImageToCloudinary - called");
		
		try{
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", "cmpe295b",
				  "api_key", "451228766912636",
				  "api_secret", "utBUDOlhMyTFpHy0qDoudgmnggg"));
		
		
		//File toUpload = new File(outputFileName);
		//Map uploadResult = cloudinary.uploader().upload(toUpload);
		/*
		Map result = cloudinary.uploader().upload(new File(outputFileName), ObjectUtils.asMap(
				  "public_id", "sample_id",
				  "transformation", new Transformation().crop("limit").width(40).height(40),
				  "eager", Arrays.asList(
				    new Transformation().width(200).height(200)
				      .crop("thumb").gravity("face").radius(20)
				      .effect("sepia"),
				    new Transformation().width(100).height(150)
				      .crop("fit").fetchFormat("jpg")
				  ),
				  "tags", "special, for_homepage"));
		*/
		
		
		Map result = cloudinary.uploader().upload(new File(outputFileName), ObjectUtils.asMap(
				  "public_id", prodName,
				  "transformation", new Transformation().crop("limit").width(500).height(500)
				  ));
		
		String url = cloudinary.url().format("PNG")
				  .transformation(new Transformation().width(500).height(500).crop("fit"))
				  .generate(prodName);
		
		System.out.println("URL received from cloudinary " +url);
		
		return url;
		
		
		}catch(IOException e){
	    	  e.printStackTrace();
	    }
		return null;
	}
	   
	public boolean save(ProductDetail prod){

		if(prod.getName() == null || prod.getCategory() == null || 
				prod.getDescription() == null || prod.getPrices()==null ||prod.getUrl() == null)  
		  return false;
		
		String prodName = prod.getName().replaceAll("\\s", "");
		System.out.println("Product URL to be added: "+prod.getUrl() +"and name:" + prodName);
		
		String newUrl = createTransparentImage(prod.getUrl(), prodName);
		
		System.out.println("New Product URL to be added: "+newUrl);
		
		System.out.println("Saving to product info to Database");
		prod.setUrl(newUrl);
		prod.setName(prodName);
		Document document=new Document("_id",	prod.getProduct_id());
		document.append("name", prodName);
		document.append("category", prod.getCategory());
		document.append("description", 	prod.getDescription());
		document.append("price", prod.getPrices());
		document.append("url", 	newUrl);
		
		prodCollection.insertOne(document);
		
		return true;
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
