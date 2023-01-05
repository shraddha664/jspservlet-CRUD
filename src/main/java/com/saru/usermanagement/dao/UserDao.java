package com.saru.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.saru.usermanagement.model.User;

public class UserDao {
	private String jdbcURL="jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbcUsername="root";
	private String jdbcPassword="spmysql";
	
	//initializing the SQL query to be fired	
private  String insertUsersSql="INSERT INTO users"+"(name,email,country)VALUES"+"(?,?,?);";
private String selectUserById="select id,name,email,country from users where id=?";
private String selectAllUsers="select * from users";
private String deleteUsersSql="delete from users where id=?";
private String updateUsersSql="update users set name=?,email=?,country=? where id=?";


protected Connection getConnection() {
	Connection connection=null;
	try {
		Class.forName("com.mysql.jdbc.Driver");
		connection=DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
	}catch(SQLException e) {
		e.printStackTrace();
	}catch(ClassNotFoundException e) {
		e.printStackTrace();
	}
	return connection;
}

//create or insert a user
public void insertUser(User user)throws SQLException {
	try(Connection connection=getConnection();
			PreparedStatement preparedStatement=connection.prepareStatement(insertUsersSql)){
			preparedStatement.setString(1,user.getName());
			preparedStatement.setString(2,user.getEmail());
			preparedStatement.setString(3,user.getCountry());
			preparedStatement.executeUpdate();

} catch (Exception e) {
	e.printStackTrace();
}

}

//update user
public boolean updateUser(User user) throws SQLException {
	boolean rowUpdate;
	try(Connection connection=getConnection();
			PreparedStatement statement=connection.prepareStatement(updateUsersSql);){
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			
			rowUpdate=statement.executeUpdate()>0;

}
		return rowUpdate;
	}
	
	
//Select user by id
public User selectUser(int id) {
	User user=null;
	try(Connection connection=getConnection();
			PreparedStatement preparedStatement=connection.prepareStatement(selectUserById);){
preparedStatement.setInt(1, id);
System.out.println(preparedStatement);
//execute the query
ResultSet rs=preparedStatement.executeQuery();
//process the result set object
while(rs.next()) {
	String name=rs.getString("name");
	String email=rs.getString("email");
	String country=rs.getString("country");
	user=new User(id,name,email,country);

}
}catch(SQLException e) {
	e.printStackTrace();
}
return user;
}

//select all users
public List<User> selectAllUser() {
List<User> users=new ArrayList<>();
	try(Connection connection=getConnection();
			PreparedStatement preparedStatement=connection.prepareStatement(selectUserById);){

System.out.println(preparedStatement);
//execute the query
ResultSet rs=preparedStatement.executeQuery(); 
//process the result set object
while(rs.next()) {
	int id=rs.getInt("id");
	String name=rs.getString("name");
	String email=rs.getString("email");
	String country=rs.getString("country"); 
	users.add(new User(id,name,email,country)); 

}
}catch(SQLException e) {
	e.printStackTrace();
}
return users;
}


//delete user by id
public boolean deleteUser(int id) throws SQLException {
	boolean rowDeleted;
	try(Connection connection=getConnection();
			PreparedStatement statement=connection.prepareStatement(deleteUsersSql);){
statement.setInt(1,id);
rowDeleted=statement.executeUpdate()>0;
}
return rowDeleted;
}
}