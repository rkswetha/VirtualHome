package org.gradle;

import javax.validation.Valid;
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
@RequestMapping("/api/v5")
public class UserController {
	private AtomicLong along;
    private UserDAO userDAO;

    public UserController() {
    	final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://karan:karan345@ds047792.mongolab.com:47792/cmpe295"));
        final MongoDatabase pollDatabase = mongoClient.getDatabase("cmpe295");
    userDAO=new UserDAO(pollDatabase);
            along = new AtomicLong(123456);

    }

	//Add Preference
	@RequestMapping(value="/users", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	public ResponseEntity<User> addUserPreferences(@Valid @RequestBody User user)
	{
		user.setUser_id((int)along.incrementAndGet());
        //userPref.setCreated_at(new Date().toString());
        userDAO.save(user);
        return new ResponseEntity<User>(user,HttpStatus.CREATED);
	}

	//list bank
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseEntity<User> viewUsers(@PathVariable int id){
		return new ResponseEntity<User>(userDAO.getUser(id),HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Object> deleteUsers(@NotBlank @PathVariable int id){
        userDAO.deleteUser(id);
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, produces="application/json")
    @ResponseBody
    public ResponseEntity<User> updateUsers(@NotBlank @PathVariable int id,@Valid @RequestBody User user ){
                    userDAO.updateUser(user, id);
                    return new ResponseEntity<User>(HttpStatus.OK);
    }


}
