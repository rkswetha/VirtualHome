package org.gradle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserPreference{
	
	
	private Integer identity;
	private String email;
	@NotEmpty
	private String sex;
	private String ethnicity;
	@NotEmpty
	private List<String> options;
	
	public UserPreference()
	{
		
	}
	public UserPreference(int identity, String email,String sex, String ethnicity, List<String> options) 
	{
		super();
		this.identity = identity;
		this.email = email;
		this.sex = sex;
		this.ethnicity = ethnicity;
		this.options = options;
	}
	
	public Integer getIdentity() {
		return identity;
	}
	public void setIdentity(Integer id) {
		this.identity = id;
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
	
	public List<String> getOptions() {
		return options=null==options?new ArrayList<String>():options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}

	

}
