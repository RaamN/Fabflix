import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;

@javax.servlet.annotation.WebServlet({"/ShoppingCart"})
public class ShoppingCart extends javax.servlet.http.HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public ShoppingCart() {}
  
  protected void doGet(HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException
  {
    javax.servlet.http.HttpSession session = request.getSession();
    

    LinkedHashMap<String, Integer> Cart = (LinkedHashMap)session.getAttribute("Cart");
    

    if (Cart == null) {
      Cart = new LinkedHashMap();
      session.setAttribute("Cart", Cart);
    }
    
    String id = request.getParameter("id");
    String quantity = request.getParameter("quantity");
    Integer number = null;
    String delete = request.getParameter("delete");
    String newQuantity = request.getParameter("newQuantity");
    
    if (quantity != null) {
      number = Integer.valueOf(Integer.parseInt(quantity));
    }
    


    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
    
    response.setContentType("text/html");
    

    PrintWriter out = response.getWriter();
    
    String title = "Shopping Cart";
    String docType = 
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n";
    
    out.println(docType + 
      "<HTML>\n" + 
      "<HEAD><TITLE>" + title + "</TITLE>" + 
      "<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">" + 
      "<link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");
    out.println("<BODY style=\"background-color: lightgrey;\"><h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"mainPage\">Fabflix</a></h2><h2 id=\"cart\" style=\"float:right;display: inline-block;z-index: 1;padding:1em;\"><a href=\"ShoppingCart\">My Cart</a></h2>");
    

    out.println("<h2 align=\"center\">Shopping Cart</h2>");
    

    int total_movies = 0;
    synchronized (Cart)
    {
      out.println("<input type = \"hidden\" name = \"newQuantity\" value = \"" + newQuantity + "\">");
      out.println("<input type = \"hidden\" name = \"id\" value = \"" + id + "\">");
      
      String uri = request.getScheme() + "://" + 
      
        request.getServerName() + (
        
        (("http".equals(request.getScheme())) && (request.getServerPort() == 80)) || (("https".equals(request.getScheme())) && (request.getServerPort() == 443)) ? "" : new StringBuilder(":").append(request.getServerPort()).toString()) + 
        
        request.getRequestURI() + (
        
        request.getQueryString() != null ? "?" + request.getQueryString() : "");
      
      System.out.println(uri);
      
      if ((id != null) && (quantity != null)) {
        if (Cart.containsKey(id)) {
          Cart.put(id, Integer.valueOf(((Integer)Cart.get(id)).intValue() + number.intValue()));
        }
        else {
          Cart.put(id, number);
        }
      }
      if ((id != null) && (newQuantity != null))
      {
        if (Integer.parseInt(newQuantity) <= 0)
        {
          Cart.remove(id);
        } else {
          Cart.put(id, Integer.valueOf(Integer.parseInt(newQuantity)));
        }
      }
      if ((id != null) && (delete != null)) {
        Cart.remove(id);
      }
      

      if (Cart.size() == 0)
      {
        out.println("<I>No items</I>");

      }
      else
      {
        out.println("<table border align=\"center\">");
        out.println("<tr><td>Movie</td><td>Price</td><td>Quantity</td></tr>");
        



        try
        {
          Class.forName("com.mysql.jdbc.Driver").newInstance();
          java.sql.Connection dbcon = java.sql.DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
          java.sql.Statement statement = dbcon.createStatement();
          



          java.util.Set set = Cart.entrySet();
          

          java.util.Iterator i = set.iterator();
          
          while (i.hasNext())
          {
            Map.Entry<String, Integer> me = (Map.Entry)i.next();
            if (((Integer)me.getValue()).intValue() > 0) {
              String query = "SELECT title FROM movies WHERE id = " + (String)me.getKey();
              
              java.sql.ResultSet rs = statement.executeQuery(query);
              
              while (rs.next()) {
                String movie_title = rs.getString("title");
                out.println("<tr><td><a href=MoviePage?id=" + (String)me.getKey() + ">" + movie_title + "</a></td>" + 
                  "<td>$100.00</td>" + 
                  "<form action = \"ShoppingCart\" METHOD = \"GET\">" + 
                  "<td><input type=\"number\" value=" + me.getValue() + " name = \"newQuantity\"></td>" + 
                  "<input type = \"hidden\" name = \"id\" value = \"" + (String)me.getKey() + "\">" + 
                  
                  "<td><button type = \"submit\">" + 
                  "Update" + 
                  "</button></td>" + 
                  "</form>" + 
                  
                  "<td>" + 
                  "<a href=\"ShoppingCart?id=" + (String)me.getKey() + "&delete=delete\"><button>Delete</button></a>" + 
                  "</td>" + 
                  
                  "</tr>");
              }
              
              total_movies += ((Integer)me.getValue()).intValue();
            }
            
          }
        }
        catch (SQLException ex)
        {
          Cart = new LinkedHashMap();
          session.setAttribute("Cart", Cart);
          while (ex != null) {
            System.out.println("SQL Exception:  " + ex.getMessage());
            ex = ex.getNextException();


          }
          


        }
        catch (Exception ex)
        {


          return;
        }
        
        out.println("</table>");
        
        out.println("<h4 align=\"center\">Total: $" + total_movies * 100 + ".00");
        

        out.println("<a href=\"checkout.jsp\"><button align=\"center\">Checkout</button></a>");
      }
    }
    
    out.println("</BODY></HTML>");
    out.close();
  }
}