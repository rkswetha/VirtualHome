package org.gradle;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

public class ProductDetail {
	
	int product_id;

	String product_category;
	String product_name;
	String product_description;
	String product_prices;
	String product_url;

	public ProductDetail(){}

	public ProductDetail(int product_id, String product_name, String product_category, String product_desc, String product_prices, String product_url)
	{
		super();
		System.out.println("product Creation triggered"); 
		this.product_id = product_id;
		this.product_name = product_name;
		this.product_category = product_category;
		this.product_description = product_desc;
		this.product_prices = product_prices;
		this.product_url = product_url;
	}

	public int getProduct_id(){return product_id;}
	public String getProductDesc(){return product_description;}
	public String getProductName(){return product_name;}
	public String getProductCategory(){return product_category;}
	public String getProductPrices(){return product_prices;}
	public String getProductUrl(){return product_url;}

	public void setProduct_id(int product_id){this.product_id = product_id;}
	public void setProductDesc(String desc){this.product_description = desc;}
	public void setProductName(String name){this.product_name = name;}
	public void setProductCategory(String category){this.product_category = category;}
	public void setProductPrices(String prices){this.product_prices = prices;}
	public void setProductUrl(String url){this.product_url = url;}

}
