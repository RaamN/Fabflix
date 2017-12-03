import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletResponse;

public class StarPage extends javax.servlet.http.HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public StarPage() {}
  
  public String getServletInfo()
  {
    return "Servlet connects to MySQL database and displays result of a SELECT";
  }
  


  public void doGet(javax.servlet.http.HttpServletRequest request, HttpServletResponse response)
    throws java.io.IOException, javax.servlet.ServletException
  {
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
    
    response.setContentType("text/html");
    

    PrintWriter out = response.getWriter();
    
    out.println("<HTML><HEAD><TITLE>Fabflix: Found Results</TITLE>");
    out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\"><link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");
    

    out.println("<BODY style=\"background-color: lightgrey;\"><h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"mainPage\">Fabflix</a></h2><h2 id=\"cart\" style=\"float:right;display: inline-block;z-index: 1;padding:1em;\"><a href=\"ShoppingCart\">My Cart</a></h2>");
    

    out.println("<h3 style=\"text-align:center;\">Star Page</h3>");
    





    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      
      Connection dbcon = java.sql.DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
      
      Statement statement = dbcon.createStatement();
      Statement starStatement = dbcon.createStatement();
      
      String id = request.getParameter("id");
      

      String query = "SELECT id, first_name, last_name, dob, photo_url from stars WHERE id = " + id;
      

      ResultSet rs = statement.executeQuery(query);
      

      out.println("<TABLE border align=\"center\">");
      



      out.println("<tr><td>Image</td><td>ID</td><td>Name</td><td>Date of Birth</td><td>Movies</td></tr>");
      






      while (rs.next())
      {
        String s_ID = rs.getString("id");
        String fn = rs.getString("first_name");
        String ln = rs.getString("last_name");
        String dob = rs.getString("dob");
        String photo = rs.getString("photo_url");
        String table = "<tr><td><img src=" + 
          photo + " style= height:200px;/></td>" + 
          "<td>" + s_ID + "</td>" + 
          "<td>" + fn + " " + ln + "</td>" + 
          "<td>" + dob + "</td><td>";
        
        String movieQuery = "Select M.id, M.title from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
        

        movieQuery = movieQuery + "AND S.id = " + s_ID;
        

        ResultSet rsMovie = starStatement.executeQuery(movieQuery);
        
        while (rsMovie.next()) {
          String title = rsMovie.getString("title");
          String movieid = rsMovie.getString("id");
          table = table + "<p><a href=MoviePage?id=" + movieid + ">" + title + "</a></p>";
        }
        table = table + "</td></tr>";
        out.println(table);
      }
      
      out.println("</TABLE>");
      

      rs.close();
      statement.close();
      dbcon.close();
    }
    catch (SQLException ex) {
      while (ex != null) {
        System.out.println("SQL Exception:  " + ex.getMessage());
        ex = ex.getNextException();


      }
      


    }
    catch (Exception ex)
    {


      return;
    }
    out.close();
  }
}