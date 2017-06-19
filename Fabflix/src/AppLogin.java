import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AppLogin
 */
@WebServlet("/AppLogin")
public class AppLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

        PrintWriter out = response.getWriter();
    	String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
        response.setStatus(HttpServletResponse.SC_OK);
        try{
        	
        	Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            Statement statement = dbcon.createStatement();
	      	  String username = request.getParameter("uname");
	      	  String password = request.getParameter("pword");
	      	  String query = "SELECT COUNT(*) from customers WHERE email = \"" + username + "\" AND password = \"" + password + "\"";
	      	  
	      	  ResultSet result;
	      	  result = statement.executeQuery(query);
	      	  int e = 0;
	      	  while(result.next()){
	      		  e = result.getInt(1);
	      	  }
	      	  if(e == 0){
	      		 //response.sendRedirect("index.jsp?errorMessage=Invalid");
	  	      	//RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
	
	  	      	//request.setAttribute("errormsg", "Wrong Username or Password");
	      		out.println("0");
	      		//rd.forward(request, response);  

	      	  }
	      	  else{
//	      		 session.setAttribute("", "");
	      		out.println("1");
 	      		 //response.sendRedirect("mainPage");	
	      	  }
	      	response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e)
        {
        	
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
