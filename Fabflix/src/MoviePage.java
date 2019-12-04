import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletResponse;

public class MoviePage extends javax.servlet.http.HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public MoviePage() {}
  
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
    out.println("<h3 style=\"text-align:center;\">Movie Page</h3>");
    
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      
      Connection dbcon = java.sql.DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
      
      Statement statement = dbcon.createStatement();
      Statement starStatement = dbcon.createStatement();
      Statement genreStatement = dbcon.createStatement();
      
      String id = request.getParameter("id");
      
      String query = "SELECT DISTINCT id, year, director, banner_url, title, trailer_url from movies WHERE id = " + id;
      
      ResultSet rs = statement.executeQuery(query);
      
      out.println("<TABLE border align=\"center\">");
      
      out.println("<tr><td>Image</td><td>ID</td><td>Title</td><td>Year</td><td>Director</td><td>Stars</td><td>Genres</td><td>Trailer</td></tr>");
      
      while (rs.next())
      {
        String m_IG = rs.getString("banner_url");
        String m_ID = rs.getString("id");
        String m_TI = rs.getString("title");
        String m_YR = rs.getString("year");
        String m_DR = rs.getString("director");
        String m_TR = rs.getString("trailer_url");
        String table = "<tr><td><img src=" + 
          m_IG + " style= height:200px;/></td>" + 
          "<td>" + m_ID + "</td>" + 
          "<td>" + m_TI + "</td>" + 
          "<td>" + m_YR + "</td>" + 
          "<td>" + m_DR + "</td><td>";
        
        String starQuery = "Select S.first_name, S.last_name, S.id from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
        String genreQuery = "Select G.name, G.id from genres G, genres_in_movies GM, movies M WHERE M.id = GM.movie_id and G.id = GM.genre_id ";
        

        starQuery = starQuery + "AND M.id = " + m_ID;
        
        genreQuery = genreQuery + "AND M.id = " + m_ID;
        
        ResultSet rsStar = starStatement.executeQuery(starQuery);
        ResultSet gStar = genreStatement.executeQuery(genreQuery);
        
        while (rsStar.next()) {
          String starFName = rsStar.getString("first_name");
          String starLName = rsStar.getString("last_name");
          String starid = rsStar.getString("id");
          table = table + "<p><a href=StarPage?id=" + starid + ">" + starFName + " " + starLName + "</a></p>";
        }
        table = table + "</td><td>";
        while (gStar.next()) {
          String genre = gStar.getString("name");
          String gid = gStar.getString("G.id");
          table = table + "<p><a href=Browse?genre=" + gid + "&page=1>" + genre + "</a></p>";
        }
        table = table + "</td><td><a href=" + m_TR + ">" + m_TR + "</a>";
        table = table + "</td></tr>";
        out.println(table);
      }
      
      out.println("</TABLE>");
   
      out.println("<a href=\"ShoppingCart?id=" + id + "&quantity=1\">" + "Add to Cart</a>");
      
      rs.close();
      statement.close();
      dbcon.close();
    }
    catch (java.sql.SQLException ex) {
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
