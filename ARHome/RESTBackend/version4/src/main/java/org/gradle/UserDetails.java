package org.gradle;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserDetails {

	
	User user;
	UserPreference userPref;

	public UserDetails(){}

	public UserDetails(int user_id, User user, UserPreference userPref)
	{
	super();
	System.out.println("User details Creation triggered"); 
	this.user = user;
	this.userPref = userPref;
	}

	public User getUserObj(){return user;}
	public UserPreference getUserPref(){return userPref;}

	public void setUserObj(User user){this.user = user;}
	public void setUserPref(UserPreference userPref){this.userPref = userPref;}
}
