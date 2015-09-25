package org.gradle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.gradle.UserPreference;


@RestController
@RequestMapping("/api/v1")
public class UserPreferenceController
{
	Random randomGenerator = new Random();
	private int login_id=randomGenerator.nextInt(35000);
	Map<String, List<Map<String,UserPreference>>> userPrefData = new HashMap<String, List<Map<String,UserPreference>>>();

	//Add Preference
	@RequestMapping(value="/api/v1/users/{user_id}/userpreferences", method=RequestMethod.POST)
	public UserPreference addUserPreferences(@Valid @RequestBody UserPreference userPref, @PathVariable("user_id") String user_id)
	{
		login_id= login_id+11;
		//String userPrefId= "b-"+Integer.toString(login_id);
		
		UserPreference newUserPref=new UserPreference(login_id, userPref.getEmail(), userPref.getSex(), userPref.getEthnicity(), userPref.getOptions());
		int flag=0;
		Iterator itr = userPrefData.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry pairs = (Map.Entry)itr.next();
			if(pairs.getKey().equals(user_id)){
				List<Map<String, UserPreference>> temp=(List<Map<String,UserPreference>>)userPrefData.get(user_id);
				Map<String, UserPreference> mapNewUserPref= new HashMap<String, UserPreference>();
				mapNewUserPref.put(Integer.toString(login_id), newUserPref);
				temp.add(mapNewUserPref);
				flag=1;
				break;
			}}

		if(flag==0){
			List<Map<String,UserPreference>> list= new ArrayList<Map<String,UserPreference>>();
			Map<String, UserPreference> mapNewUserPref= new HashMap<String, UserPreference>();
			mapNewUserPref.put(Integer.toString(login_id), newUserPref);
			list.add(mapNewUserPref);
			userPrefData.put(user_id,list);
		}
		return newUserPref;
	}

	//list bank
	@RequestMapping(value="/api/v1/users/{user_id}/userpreferences", method=RequestMethod.GET)
	public List<Map<String,UserPreference>> viewUserPreferences(@PathVariable("user_id") String user_id){
		System.out.println(user_id);
		return userPrefData.get(user_id);
	}
	
	//Delete a bank login
	@RequestMapping(value="/api/v1/users/{user_id}/userpreferences", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserPreference(@PathVariable("user_id") String user_id)
	{
		int flag=0;
		Iterator itr = userPrefData.entrySet().iterator();
		while (itr.hasNext()) 
		{
			Map.Entry pairs = (Map.Entry)itr.next();
			if(pairs.getKey().equals(user_id))
			{
				List<Map<String,UserPreference>> list=(List<Map<String,UserPreference>>)userPrefData.get(user_id);
				//Iterator itrlist = list.listIterator();
				list.clear();
				//while (itrlist.hasNext()) 
				//{
					//if(((Map<String,Bank>)itrlist.next()).containsKey(bank_id))
					//{
						//itrlist.remove();
					//}
				//}
				break;
			}
		}
	}
}

//public class UserController {
//
//	private AtomicLong along;
//	//private UserPreferenceDAO userPrefDAO;
//	
//	public UserController() {
//		final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://ksingh:karan345@ds047792.mongolab.com:47792/cmpe295"));
//        final MongoDatabase userPrefDatabase = mongoClient.getDatabase("cmpe295");
//        //userPrefDAO=new UserPreferenceDAO(userPrefDatabase);
//		along = new AtomicLong(123456);
//	}
//
//	@RequestMapping(value = "/prefrences", method = RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<UserPreference> createUserPreference( @RequestBody UserPreference userPref) {
//
//		userPref.setIdentity((int) along.incrementAndGet());
//		//userPref.setEmail(userPref.)
//		//userPref.setCreated_at(new Date().toString());
//		userPrefDAO.save(userPref);
//		return new ResponseEntity<UserPreference>(userPref,HttpStatus.CREATED);
//	}
//
//	@RequestMapping(value = "/preferences/{id}", method = RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<UserPreference> getUserPreference(@PathVariable int id) {
//		
//			return new ResponseEntity<UserPreference>(userPrefDAO.getUserPreference(id),HttpStatus.OK);
//		
//	}
//	
//	@RequestMapping(value = "/preferences/{id}", method = RequestMethod.PUT, produces="application/json")
//	@ResponseBody
//	public ResponseEntity<Object> updateModerator( @PathVariable int id, @RequestBody UserPreference moderator) {
//		moderator.setIdentity(id);
//		userPrefDAO.updateUserPreference(moderator);
//		return new ResponseEntity<Object>(userPrefDAO.getUserPreference(id),HttpStatus.OK) ;
//	}
//	
//}
