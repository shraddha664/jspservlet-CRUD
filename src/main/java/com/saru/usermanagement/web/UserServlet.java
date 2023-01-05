package com.saru.usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.saru.usermanagement.dao.UserDao;
import com.saru.usermanagement.model.User;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       private UserDao userDao;
    
    public UserServlet() {
    this.userDao=new UserDao();
        
    }

	
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getServletPath();
		
		switch (action) {
		case "/new":
			showNewForm(request,response);
			break;
		case "/insert":
			try {
				insertUser(request,response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "/delete":
			try {
				deleteUser(request,response);
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
			break;
		case "/edit":
			showEditForm(request,response);
			break;
		case "/update":
			try {
				updateUser(request,response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
//			handle list
			listUser(request,response);
			break;
		}
		
	}

private void listUser(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
	List<User> listUser=userDao.selectAllUser();
	request.setAttribute("listUser", listUser);
	RequestDispatcher dispatcher=request.getRequestDispatcher("user-list.jsp");
	dispatcher.forward(request, response);
}

private void updateUser(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
	int id=Integer.parseInt(request.getParameter("id"));
	String name=request.getParameter("name");
	String email=request.getParameter("email");
	String country=request.getParameter("country");
	
	User user=new User(id,name,email,country);
	userDao.updateUser(user);
	response.sendRedirect("list");
}

private void showEditForm(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
	int id=Integer.parseInt(request.getParameter("id"));
	User existingUser=userDao.selectUser(id);
	RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
	request.setAttribute("user", existingUser);
	dispatcher.forward(request, response);
}

private void showNewForm(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException {
	RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
	dispatcher.forward(request,response);
}

private void insertUser(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
	String name=request.getParameter("name");
	String email=request.getParameter("email");
	String country=request.getParameter("country");
	User newUser=new User(name,email,country);
	userDao.insertUser(newUser);
	response.sendRedirect("list");
}

private void deleteUser(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
	int id=Integer.parseInt(request.getParameter("id"));
	userDao.deleteUser(id);
	response.sendRedirect("list");
}
}
