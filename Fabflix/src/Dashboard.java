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





@WebServlet({"/Dashboard"})
public class Dashboard
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public Dashboard() {}
  
  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    doGet(request, response);
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
    
    response.setContentType("text/html");
    




    PrintWriter out = response.getWriter();
    

    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      
      Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
      
      Statement statement = dbcon.createStatement();
      
      String email = request.getParameter("email");
      String password = request.getParameter("password");
      
      String query = "SELECT COUNT(*) from employees WHERE email = \"" + email + "\" AND password = \"" + password + "\"";
      



      ResultSet result = statement.executeQuery(query);
      int e = 0;
      while (result.next()) {
        e = result.getInt(1);
      }
      

      if (e == 0)
      {
        RequestDispatcher rd = request.getRequestDispatcher("_dashboard.jsp");
        
        request.setAttribute("errormsg", "You are not an employee");
        
        rd.forward(request, response);
      }
      else
      {
        response.sendRedirect("dashboard");
      }
      
    }
    catch (Exception ex)
    {
      out.println("<HTML><HEAD><TITLE>MovieDB: Error</TITLE></HEAD>\n<BODY><P>SQL error in doGet: " + 
      



        ex.getMessage() + "</P></BODY></HTML>");
      return;
    }
    out.close();
  }
}