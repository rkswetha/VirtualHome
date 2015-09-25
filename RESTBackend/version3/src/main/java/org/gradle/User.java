package org.gradle;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class User{

int user_id;

@NotEmpty
@Email(message="wrong email format")
String email;

@NotEmpty
@Length(min = 1,message="password too short")
String password;

String name;

String created_at;
String updated_at;

public User(){}

public User(int user_id, String email, String password, String name, String created_at, String updated_at)
{
super();
System.out.println("User Creation triggered"); 
this.user_id = user_id;
this.email = email;
this.password = password;
this.name = name;
this.created_at = created_at;
this.updated_at = updated_at;
}

public int getUser_id(){return user_id;}
public String getEmail(){return email;}
public String getPassword(){return password;}
public String getName(){return name;}
public String getCreated_at(){return created_at;}
public String getUpdated_at(){return updated_at;}

public void setUser_id(int user_id){this.user_id = user_id;}
public void setEmail(String email){this.email = email;}
public void setPassword(String password){this.password = password;}
public void setName(String name){this.name = name;}
public void setCreated_at(String created_at){this.created_at = created_at;}
public void setUpdated_at(String updated_at){this.updated_at = updated_at;}
//public String toString(User user){}
}
