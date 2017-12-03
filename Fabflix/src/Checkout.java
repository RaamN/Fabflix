import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;





@WebServlet({"/Checkout"})
public class Checkout
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public Checkout() {}
  
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
    
    HttpSession session = request.getSession();
    

    LinkedHashMap<String, Integer> Cart = (LinkedHashMap)session.getAttribute("Cart");
    


    PrintWriter out = response.getWriter();
    


    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      
      Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
      
      Statement statement = dbcon.createStatement();
      
      String first_name = request.getParameter("fname");
      String last_name = request.getParameter("lname");
      String id = request.getParameter("ccid");
      String expiration = request.getParameter("exp");
      String query = "SELECT COUNT(*) from creditcards WHERE first_name = \"" + first_name + "\" AND last_name = \"" + last_name + "\" AND id = " + id + " AND expiration = \"" + expiration + "\"";
      



      ResultSet result = statement.executeQuery(query);
      int e = 0;
      while (result.next()) {
        e = result.getInt(1);
      }
      


      if (e == 0)
      {
        RequestDispatcher rd = request.getRequestDispatcher("checkout.jsp");
        
        request.setAttribute("errormsg", "Invalid credentials");
        
        rd.forward(request, response);
      }
      else
      {
        synchronized (Cart)
        {


          Set set = Cart.entrySet();
          

          Iterator i = set.iterator();
          
          while (i.hasNext())
          {

            Map.Entry<String, Integer> me = (Map.Entry)i.next();
            
            Statement find = dbcon.createStatement();
            String customer = "SELECT id FROM customers WHERE cc_id = " + id;
            ResultSet rs = find.executeQuery(customer);
            
            int x = 0;
            
            while (rs.next()) {
              x = rs.getInt(1);
            }
            
            Statement s = dbcon.createStatement();
            
            String insert = "INSERT INTO sales(customer_id, movie_id, sale_date) VALUES(" + x + "," + (String)me.getKey() + ",CURDATE())";
            

            int i = s.executeUpdate(insert);
          }
        }
        


        Cart = new LinkedHashMap();
        session.setAttribute("Cart", Cart);
        
        response.sendRedirect("confirmation.html");
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