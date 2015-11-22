package org.gradle;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;


public class ProductRecommendation{
	
	private int user_id;
	private String gender;
	private String family;
	private String profession;
	private Boolean gardening;
	private Boolean interiorDesign;
	private Boolean cooking;
	private Boolean painting;
	private Boolean reading;
	private Boolean music;
	private long productID;
	
	
	public ProductRecommendation()
	{
		
	}
	public ProductRecommendation(int user_id, String sex, String family, String profession, Boolean gardening, Boolean interiorDesign, Boolean cooking,  Boolean painting, Boolean reading,Boolean music, long productID ) 
	{
		
		super();
		System.out.println("inside the constr"+" "+gardening+interiorDesign);
		this.user_id = user_id;
		this.gender = sex;
		this.family = family;
		this.profession = profession;
		this.gardening = gardening;
		this.interiorDesign = interiorDesign;
		this.cooking = cooking;
		this.painting = painting;
		this.reading = reading;
		this.music = music;
		this.productID=productID;
	}
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(int id) {
		this.user_id = id;
	}
	//
	public Long getProductID() {
		return productID;
	}
	public void setProductID(long id) {
		this.productID = id;
	}
	
	public String getSex() {
		return gender;
	}
	public void setSex(String sex) {
		this.gender = sex;
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
