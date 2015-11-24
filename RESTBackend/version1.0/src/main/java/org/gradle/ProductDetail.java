package org.gradle;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class ProductDetail {
	
	int product_id;

	String category;
	String name;
	String description;
	String prices;
	String url;

	public ProductDetail(){}

	public ProductDetail(int product_id, String name, String category, String description, String prices, String url)
	{
		super();
		System.out.println("product Creation triggered"); 
		this.product_id = product_id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.prices = prices;
		this.url = url;
	}

	public int 	getProduct_id(){return product_id;}
	public String 	getDescription(){return description;}
	public String 	getName(){return name;}
	public String 	getCategory(){return category;}
	public String 	getPrices(){return prices;}
	public String 	getUrl(){return url;}

	public void 	setProduct_id(int product_id){this.product_id = product_id;}
	public void 	setDescription(String description){this.description = description;}
	public void 	setName(String name){this.name = name;}
	public void 	setCategory(String category){this.category = category;}
	public void 	setPrices(String prices){this.prices = prices;}
	public void 	setUrl(String url){this.url = url;}

}
