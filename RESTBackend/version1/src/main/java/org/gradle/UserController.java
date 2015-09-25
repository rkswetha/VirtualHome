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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class UserController {
	private String ig_gen;
	Random randomGenerator = new Random();
	private int id_no=randomGenerator.nextInt(1000);
	Map<String, User> userData = new HashMap<String, User>();

	//Add User
	@RequestMapping(value="/api/v1/users", method=RequestMethod.POST)
	public User update(@Valid @RequestBody User user){
		System.out.println("Inside Add user");
		id_no= id_no+17;
		String user_id= "u-"+Integer.toString(id_no);	
		Date date = new Date();
		Format f= new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String created_at= f.format(date);
		String updated_at= f.format(date);
		User new_user=new User(user_id, user.getEmail(), user.getPassword(), user.getName(), created_at, updated_at);
		userData.put(user_id,new_user);
		return new_user;
	}

	//Get User details
	@RequestMapping(value="/api/v1/users/{user_id}",method=RequestMethod.GET)
	public User view(@PathVariable("user_id") String user_id){
		System.out.println(user_id);
		return userData.get(user_id);
	}

	//Update User Details
	@RequestMapping(value="/api/v1/users/{user_id}",method=RequestMethod.PUT)
	public User edit(@Valid @RequestBody User user,@PathVariable("user_id") String user_id){
		System.out.println(user_id);
		User user_online= userData.get(user_id);
		user_online.setEmail(user.getEmail());
		user_online.setPassword(user.getPassword());
		Date date = new Date();
		Format f= new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String updated_at= f.format(date);
		user_online.setUpdated_at(updated_at);
		userData.put(user_id, user_online);
		return userData.get(user_id);
	}
}
