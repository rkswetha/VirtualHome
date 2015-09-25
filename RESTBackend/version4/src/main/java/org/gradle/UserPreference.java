package org.gradle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserPreference{
	
	
//	private Integer identity;
	private int user_id;
	private String email;
	private String gender;
	private String ethnicity;
	//@NotEmpty
	//String options;
	private String family;
	private String profession;
	private Boolean gardening;
	private Boolean interiorDesign;
	private Boolean cooking;
	private Boolean painting;
	private Boolean reading;
	private Boolean music;
	
	
	public UserPreference()
	{
		
	}
	public UserPreference(int user_id, String sex, String family, String profession, Boolean gardening, Boolean interiorDesign, Boolean cooking, Boolean music, Boolean painting, Boolean reading) 
	{
		super();
		this .user_id = user_id;
//		this.identity = identity;
		this.gender = sex;
		this.family = family;
		this.profession = profession;
		this.gardening = gardening;
		this.interiorDesign = interiorDesign;
		this.cooking = cooking;
		this.painting = painting;
		this.reading = reading;
		this.music = music;
//		this.options = options;
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
		return gender;
	}
	public void setSex(String sex) {
		this.gender = sex;
	}
	
	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	
//	public String getOptions() {
//		return options=null==options?" ":options;
//	}
//	public void setOptions(String options) {
//		this.options = options;
//	}
	
	public Boolean getMusic() {
		return music;
	}
	public void setMusic(Boolean music) {
		this.music = music;
	}
	
	public Boolean getCooking() {
		return cooking;
	}
	public void setCooking(Boolean cooking) {
		this.cooking = cooking;
	}
	
	public Boolean getPainting() {
		return painting;
	}
	public void setPainting(Boolean paint) {
		this.painting = paint;
	}

	public Boolean getReading() {
		return reading;
	}
	public void setReading(Boolean reading) {
		this.reading = reading;
	}
	
	public Boolean getGardening() {
		return gardening;
	}
	public void setGardening(Boolean gardening) {
		this.gardening = gardening;
	}
	
	public Boolean getInteriorDesign() {
		return interiorDesign;
	}
	public void setInteriorDesign(Boolean intdes) {
		this.interiorDesign = intdes;
	}

	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}

	public String getProfession() {
		return profession;
	}
	public void setProfession(String prof) {
		this.profession = prof;
	}


}
