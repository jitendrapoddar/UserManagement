package com.cij.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cij.dao.UserDao;
import com.cij.model.User;
import com.google.gson.Gson;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userDAO;

	public void init() {
		userDAO = new UserDao();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		Gson gson = new Gson();// convert object to json string and json string to object
		try {
			String s;
			StringBuilder sb = new StringBuilder();
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}
			User user = (User) gson.fromJson(sb.toString(), User.class);
			User insertedUser = userDAO.insertUser(user);
			String json = gson.toJson(insertedUser);
			PrintWriter out = response.getWriter();
			out.println(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		try {
			String id = request.getParameter("id");
			if(id != null) {
				User user = userDAO.selectUser(Integer.parseInt(id));
				if(user !=null) {
					String json = gson.toJson(user);
					out.println(json);
				} else {
					out.println("User : "+id+" Not Found");
				}
			} else {
				List<User> users = userDAO.selectAllUsers();
				String json = gson.toJson(users);
				out.println(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			String id = request.getParameter("id");
			if(id != null) {
				boolean isDeleted = userDAO.deleteUser(Integer.parseInt(id));
				if(isDeleted) {
					out.println("Deleted User ID : "+id);
				} else {
					out.println("User ID : "+id+" Not Found");
				}
			} else {
				out.println("Please provide User Id to delete");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		try {
			String s;
			StringBuilder sb = new StringBuilder();
			while ((s = request.getReader().readLine()) != null) {
				sb.append(s);
			}
			User user = (User) gson.fromJson(sb.toString(), User.class);
			User actualUser = userDAO.selectUser(user.getId());
			if(user.getEmail() != null) {
				actualUser.setEmail(user.getEmail());
			}
			if(user.getName() != null) {
				actualUser.setName(user.getName());
			}
			if(user.getCountry() != null) {
				actualUser.setCountry(user.getCountry());
			}
			
			boolean isUpdated = userDAO.updateUser(actualUser);
			if (isUpdated) {
				String json = gson.toJson(userDAO.selectUser(user.getId()));
				out.println(json);
			} else {
				out.println("Not Updated");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
