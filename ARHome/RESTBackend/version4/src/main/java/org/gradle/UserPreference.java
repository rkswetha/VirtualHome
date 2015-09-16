package org.gradle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserPreference{
	
	
//	private Integer identity;
	private int user_id;
	private String email;
	@NotEmpty
	private String sex;
	private String ethnicity;
	@NotEmpty
	String options;
	
	public UserPreference()
	{
		
	}
	public UserPreference(int identity, int user_id, String email,String sex, String ethnicity, String options) 
	{
		super();
		this .user_id = user_id;
//		this.identity = identity;
		this.email = email;
		this.sex = sex;
		this.ethnicity = ethnicity;
		this.options = options;
	}
	
//	public Integer getIdentity() {
//		return identity;
//	}
//	public void setIdentity(Integer id) {
//		this.identity = id;
//	}
//	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(int id) {
		this.user_id = id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	
	public String getOptions() {
		return options=null==options?" ":options;
	}
	public void setOptions(String options) {
		this.options = options;
	}

}
