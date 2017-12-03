import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@javax.servlet.annotation.WebServlet({"/Login"})
public class login extends javax.servlet.http.HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public login() {}
  
  public String getServletInfo()
  {
    return "Servlet connects to MySQL database and displays result of a SELECT";
  }
  

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws java.io.IOException, javax.servlet.ServletException
  {
    doGet(request, response);
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException
  {
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
    
    response.setContentType("text/html");
    

    PrintWriter out = response.getWriter();
    
    javax.servlet.http.HttpSession session = request.getSession();
    String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
    
    boolean valid = VerifyUtils.verify(gRecaptchaResponse);
    
    java.util.LinkedHashMap<String, Integer> Cart = new java.util.LinkedHashMap();
    session.setAttribute("Cart", Cart);
    


    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      
      java.sql.Connection dbcon = java.sql.DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
      
      java.sql.Statement statement = dbcon.createStatement();
      String username = request.getParameter("uname");
      String password = request.getParameter("pword");
      String query = "SELECT COUNT(*) from customers WHERE email = \"" + username + "\" AND password = \"" + password + "\"";
      

      ResultSet result = statement.executeQuery(query);
      int e = 0;
      while (result.next()) {
        e = result.getInt(1);
      }
      if (e == 0)
      {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        
        request.setAttribute("errormsg", "Wrong Username or Password");
        
        rd.forward(request, response);
      }
      else
      {
        if (!valid)
        {
          RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
          request.setAttribute("errormsg", "Wrong Username or Password/Invalid ReCaptcha");
          rd.forward(request, response);
        }
        response.sendRedirect("mainPage");
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