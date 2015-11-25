package org.gradle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserTransaction{
	
	
	private int user_id;
	private int product_id;
	
	public UserTransaction()
	{
		
	}
	public UserTransaction(int user_id, int product_id) 
	{
		super();
		System.out.println("inside cons");
		this.user_id = user_id;
		this.product_id = product_id;
	}
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(int id) {
		this.user_id = id;
	}
	
	public int getProduct_id(){return product_id;}
	public void setProduct_id(int product_id){this.product_id = product_id;}

}
