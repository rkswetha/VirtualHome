package org.gradle;

import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

@RestController
@RequestMapping("/api/v8")
public class UserDetailsController {
	
	private AtomicLong along;
    private UserDetailsDAO userDetailsDAO;

    public UserDetailsController() {
    	final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://karan:karan345@ds047792.mongolab.com:47792/cmpe295"));
        final MongoDatabase pollDatabase = mongoClient.getDatabase("cmpe295");
        userDetailsDAO=new UserDetailsDAO(pollDatabase);

    }
	
	//list bank
	@RequestMapping(value="/userdetails/{id}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<UserDetails> viewUserDetails(@PathVariable int id){
		return new ResponseEntity<UserDetails>(userDetailsDAO.getUserDetails(id),HttpStatus.OK);
	}

	@RequestMapping(value="/login", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public ResponseEntity<UserDetails> authUsers(@Valid @RequestBody UserDetails user){

                UserDetails userStatus =  userDetailsDAO.authUser(user);
                if(userStatus == null)
		{
		   System.out.println(" userDetailsDAO.authUser() returned null");
		   return new ResponseEntity<UserDetails>(userStatus,HttpStatus.BAD_REQUEST);
		}
		else
		{  
		  System.out.println(" userDetailsDAO.authUser() returned NOT null");
		  return new ResponseEntity<UserDetails>(userStatus,HttpStatus.OK);
		}
	}
	
}
