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
public class UserPreferenceController
{
	
	private AtomicLong along;
    private UserPreferenceDAO userPrefDAO;

    public UserPreferenceController() {
    	final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://karan:karan345@ds047792.mongolab.com:47792/cmpe295"));
        final MongoDatabase pollDatabase = mongoClient.getDatabase("cmpe295");
    	userPrefDAO=new UserPreferenceDAO(pollDatabase);
	MongoCollection<Document> userPrefCollection = pollDatabase.getCollection("preferences");
            along = new AtomicLong(123456+userPrefCollection.count());

    }
	
	//Add Preference
	@RequestMapping(value="/userpreferences", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public ResponseEntity<UserPreference> addUserPreferences(@Valid @RequestBody UserPreference userPref)
	{
		userPref.setUser_id(userPref.getUser_id());
        //userPref.setCreated_at(new Date().toString());
        userPrefDAO.save(userPref);
        return new ResponseEntity<UserPreference>(userPref,HttpStatus.CREATED);
	}

	//list bank
	@RequestMapping(value="/userpreferences/{id}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<UserPreference> viewUserPreferences(@PathVariable int id){
		return new ResponseEntity<UserPreference>(userPrefDAO.getUserPreference(id),HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/userpreferences/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleteUserPreferences(@NotBlank @PathVariable int id){
        userPrefDAO.deleteUserPref(id);
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/userpreferences/{id}", method = RequestMethod.PUT, produces="application/json")
    @ResponseBody
    public ResponseEntity<Object> updateUserPref(@NotBlank @PathVariable int id,@Valid @RequestBody UserPreference userPref){
                    userPrefDAO.updateUserPreference(userPref, id);
                    return new ResponseEntity<Object>(HttpStatus.OK);
    }

}

