package org.gradle;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserDetails {

	
	private int user_id;
	
	String email;
	String password;


	private String gender;
	private String ethnicity;
	//@NotEmpty
	private String family;
	private String profession;
	private Boolean gardening;
	private Boolean interiorDesign;
	private Boolean cooking;
	private Boolean painting;
	private Boolean reading;
	private Boolean music;

	public UserDetails(){}

	public UserDetails(int user_id, String sex, String family, String profession, Boolean gardening, Boolean interiorDesign, Boolean cooking, Boolean music, Boolean painting, Boolean reading)
	{
		super();
		this .user_id = user_id;
		this.gender = sex;
		this.family = family;
		this.profession = profession;
		this.gardening = gardening;
		this.interiorDesign = interiorDesign;
		this.cooking = cooking;
		this.painting = painting;
		this.reading = reading;
		this.music = music;
	}

	public String getEmail(){return email;}
	public String getPassword(){return password;}
	
	public void setEmail(String email){this.email = email;}
	public void setPassword(String password){this.password = password;}
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(int id) {
		this.user_id = id;
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
