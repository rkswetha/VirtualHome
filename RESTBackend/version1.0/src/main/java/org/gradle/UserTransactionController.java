package org.gradle;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;


import org.gradle.UserPreference;


@RestController
@RequestMapping("/api/v8")
public class UserTransactionController
{
	
	private AtomicLong along;
    private UserTransactionDAO userTranDAO;

    public UserTransactionController() {
    	final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://karan:karan345@ds047792.mongolab.com:47792/cmpe295"));
        final MongoDatabase pollDatabase = mongoClient.getDatabase("cmpe295");
        userTranDAO=new UserTransactionDAO(pollDatabase);
	MongoCollection<Document> userTransactionCollection = pollDatabase.getCollection("usertransactions");
            along = new AtomicLong(123456+userTransactionCollection.count());

    }
	
	//Add Transactions
	@RequestMapping(value="/usertransactions", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public ResponseEntity<UserTransaction> addUserTransactions(@Valid @RequestBody UserTransaction userTrans)
	{
		userTrans.setUser_id(userTrans.getUser_id());
		System.out.println("inside contolller:"+userTrans.getUser_id());
        //userPref.setCreated_at(new Date().toString());
		userTranDAO.save(userTrans);
        return new ResponseEntity<UserTransaction>(userTrans,HttpStatus.CREATED);
	}

	//list bank
	/*@RequestMapping(value="/usertransactions/{id}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<String> viewUserTransaction(@PathVariable int id){
		return new ResponseEntity<UserTransaction>(userTranDAO.getUserTransaction(id),HttpStatus.OK);
	}*/
	
	
	@RequestMapping(value="/usertransactions/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleteUserTransactions(@NotBlank @PathVariable int id){
		userTranDAO.deleteUserTran(id);
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/usertransactions/{id}", method = RequestMethod.PUT, produces="application/json")
    @ResponseBody
    public ResponseEntity<String> updateUserTrans(@NotBlank @PathVariable int id,@Valid @RequestBody UserTransaction userTrans){
		
                    return new ResponseEntity<String>(userTranDAO.updateUserTransaction(userTrans, id),HttpStatus.OK);
    }

}

