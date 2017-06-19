

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
/**
 * Servlet implementation class AppSearch
 */
@WebServlet("/AppSearch")
public class AppSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String loginUser = "mytestuser";
        String loginPasswd = "mypassword";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
        response.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = response.getWriter();
        try{

            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            
            Statement statement = dbcon.createStatement();
            
            String title = request.getParameter("title");
            String [] a = title.split("\\s");
            
            PreparedStatement ps = null;

            
            String query = "SELECT DISTINCT title from movies where match(title) against(\'";// LIKE \"%" + title + "%\"";
            //out.println(query);
            for (int i = 0; i < a.length; i++)
            {
            	query+= " +" + a[i];
            }
            query+="*\' IN BOOLEAN MODE)";
           
            ps = dbcon.prepareStatement(query);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
            	String m_TI = rs.getString("title");
            	out.println(m_TI);
            }
            
            rs.close();
            statement.close();
            dbcon.close();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
