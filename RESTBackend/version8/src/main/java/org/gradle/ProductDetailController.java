package org.gradle;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.Format;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;
import java.lang.Math;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v8")

public class ProductDetailController {

	private ProductDetailDAO prodDAO;
	private AtomicLong along;
	
	public ProductDetailController() {
    	final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://karan:karan345@ds047792.mongolab.com:47792/cmpe295"));
        final MongoDatabase pollDatabase = mongoClient.getDatabase("cmpe295");
        prodDAO=new ProductDetailDAO(pollDatabase);
        along = new AtomicLong(123456);
    }
	
	@RequestMapping(value="/productadd", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public ResponseEntity<ProductDetail> addProductDetails(@Valid @RequestBody ProductDetail prod)
	{
		prod.setProduct_id((int)along.incrementAndGet());
   	     	//userPref.setCreated_at(new Date().toString());
        	prodDAO.save(prod);
        	return new ResponseEntity<ProductDetail>(prod,HttpStatus.CREATED);
	}

	@RequestMapping(value="/products/{id}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<String> viewProducts(@PathVariable String id){
		return new ResponseEntity<String>(prodDAO.getProductByCategory(id),HttpStatus.OK);
	}
	
	@RequestMapping(value="/products/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleteUsers(@NotBlank @PathVariable int id){
        	prodDAO.deleteProduct(id);
        	return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT, produces="application/json")
    @ResponseBody
    public ResponseEntity<ProductDetail> updateProduct(@NotBlank @PathVariable int id,@Valid @RequestBody ProductDetail prod ){
               prodDAO.updateProduct(prod, id);
               return new ResponseEntity<ProductDetail>(HttpStatus.OK);
    }
}
