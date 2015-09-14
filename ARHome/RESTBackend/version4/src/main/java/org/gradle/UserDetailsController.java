package org.gradle;

import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class UserDetailsController {
	
	private AtomicLong along;
    private UserDetailsDAO userDetailsDAO;

    public UserDetailsController() {
    	final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://karan:karan345@ds047792.mongolab.com:47792/cmpe295"));
        final MongoDatabase pollDatabase = mongoClient.getDatabase("cmpe295");
    userDetailsDAO=new UserDetailsDAO(pollDatabase);
            along = new AtomicLong(123456);

    }
	
	//list bank
	@RequestMapping(value="/api/v1/usersdetails/{id}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<UserDetails> viewUsers(@PathVariable int id){
		return new ResponseEntity<UserDetails>(userDetailsDAO.getUserDetails(id),HttpStatus.OK);
	}
	
	

}
