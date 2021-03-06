import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class searchResults extends javax.servlet.http.HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public searchResults() {}
  
  public String getServletInfo()
  {
    return "Servlet connects to MySQL database and displays result of a SELECT";
  }
  
  public void doGet(HttpServletRequest request, HttpServletResponse response)
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
    




    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      
      Connection dbcon = java.sql.DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
      
      Statement statement = dbcon.createStatement();
      Statement statement2 = dbcon.createStatement();
      Statement starStatement = dbcon.createStatement();
      Statement genreStatement = dbcon.createStatement();
      
      String limit = request.getParameter("limit");
      String title = request.getParameter("title");
      String year = request.getParameter("year");
      String director = request.getParameter("director");
      String fname = request.getParameter("fname");
      String lname = request.getParameter("lname");
      String page = request.getParameter("page");
      String order = request.getParameter("order");
      
      String query = "SELECT DISTINCT M.id, M.year, M.director, M.banner_url, M.title from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
      String count = "SELECT COUNT(DISTINCT M.id)  from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
      





      out.println("<form action=\"searchResults\">");
      out.println("<select name=\"limit\">");
      out.println("<option value = 10> Show 10 </option>");
      out.println("<option value = 25> Show 25 </option>");
      out.println("<option value = 50> Show 50 </option>");
      out.println("<option value = 100> Show 100 </option>");
      out.println("</select><br>");
      



      if (title != null) {
        query = query + "and M.title like \"%" + title + "%\" ";
        count = count + "and M.title like \"%" + title + "%\" ";
        out.println("<input type = \"hidden\" name = \"title\" value = \"" + title + "\">");
      }
      if (year != null) {
        query = query + "and M.year like \"%" + year + "%\" ";
        count = count + "and M.year like \"%" + year + "%\" ";
        out.println("<input type = \"hidden\" name = \"year\" value = \"" + year + "\">");
      }
      if (director != null) {
        query = query + "and M.director like \"%" + director + "%\" ";
        count = count + "and M.director like \"%" + director + "%\" ";
        out.println("<input type = \"hidden\" name = \"director\" value = \"" + director + "\">");
      }
      if (fname != null) {
        query = query + "and S.first_name like \"%" + fname + "%\" ";
        count = count + "and S.first_name like \"%" + fname + "%\" ";
        out.println("<input type = \"hidden\" name = \"fname\" value = \"" + fname + "\">");
      }
      
      if (lname != null) {
        query = query + "and S.last_name like \"%" + lname + "%\" ";
        count = count + "and S.last_name like \"%" + lname + "%\" ";
        out.println("<input type = \"hidden\" name = \"lname\" value = \"" + lname + "\">");
      }
      
      if (order != null)
      {
        if (order.equals("TA"))
        {
          query = query + " ORDER BY title asc";
          out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");

        }
        else if (order.equals("TD"))
        {
          query = query + " ORDER BY title desc";
          out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");

        }
        else if (order.equals("YA"))
        {
          query = query + " ORDER BY year asc";
          out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");

        }
        else if (order.equals("YD"))
        {
          query = query + " ORDER BY year desc";
          out.println("<input type = \"hidden\" name = \"order\" value = \"" + order + "\">");
        }
      }
      

      if (limit != null)
      {

        Integer offset = Integer.valueOf((Integer.parseInt(page) - 1) * Integer.parseInt(limit));
        query = query + " LIMIT " + Integer.parseInt(limit) + " OFFSET " + offset;
      }
      

      out.println("<input type=\"hidden\" name=\"page\" value=\"1\" />");
      
      out.println("<input type = \"submit\"/>");
      
      out.println("</form>");
      


      System.out.println(query);
      
      System.out.println(count);
      
      ResultSet rs = statement.executeQuery(query);
      ResultSet rowcount = statement2.executeQuery(count);
      
      String uri = request.getScheme() + "://" + 
      
        request.getServerName() + (
        
        (("http".equals(request.getScheme())) && (request.getServerPort() == 80)) || (("https".equals(request.getScheme())) && (request.getServerPort() == 443)) ? "" : new StringBuilder(":").append(request.getServerPort()).toString()) + 
        
        request.getRequestURI() + (
        
        request.getQueryString() != null ? "?" + request.getQueryString() : "");
      



      String link = "";
      String link2 = "";
      if (order == null) {
        link = link + "<a href = \"" + uri.substring(0, uri.indexOf("&page=")) + "&order=TA" + "&page=1\">Title</a>";
        link2 = link2 + "<a href = \"" + uri.substring(0, uri.indexOf("&page=")) + "&order=YA" + "&page=1\">Year</a>";
      }
      else if (order.equals("TA")) {
        link = link + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TD" + "&page=1\">Title</a>";
        link2 = link2 + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YA" + "&page=1\">Year</a>";
      }
      else if (order.equals("TD")) {
        link = link + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TA" + "&page=1\">Title</a>";
        link2 = link2 + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YA" + "&page=1\">Year</a>";

      }
      else if (order.equals("YA")) {
        link = link + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TA" + "&page=1\">Title</a>";
        link2 = link2 + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YD" + "&page=1\">Year</a>";
      } else if (order.equals("YD")) {
        link = link + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=TA" + "&page=1\">Title</a>";
        link2 = link2 + "<a href = \"" + uri.substring(0, uri.indexOf("&order=")) + "&order=YA" + "&page=1\">Year</a>";
      }
      


      out.println("<TABLE border align=\"center\">");
      
      out.println("<tr><td>Image</td><td>ID</td><td>" + 
      

        link + "</td>" + 
        "<td>" + link2 + "</td>" + 
        "<td>" + "Director" + "</td>" + 
        "<td>" + "Stars" + "</td>" + 
        "<td>" + "Genres" + "</td>" + 
        "</tr>");
      
      while (rs.next())
      {
        String m_IG = rs.getString("M.banner_url");
        String m_ID = rs.getString("M.id");
        String m_TI = rs.getString("M.title");
        String m_YR = rs.getString("M.year");
        String m_DR = rs.getString("M.director");
        String table = "<tr><td><img src=" + 
          m_IG + " style= height:200px;/></td>" + 
          "<td rowspan=\"2\">" + m_ID + "</td>" + 
          "<td rowspan=\"2\"><a href=MoviePage?id=" + m_ID + ">" + m_TI + "</a></td>" + 
          "<td rowspan=\"2\">" + m_YR + "</td>" + 
          "<td rowspan=\"2\">" + m_DR + "</td><td rowspan=\"2\">";
        
        String starQuery = "Select S.first_name, S.last_name, S.id from movies M, stars_in_movies SM, stars S where M.id = SM.movie_id and S.id = SM.star_id ";
        String genreQuery = "Select G.name from genres G, genres_in_movies GM, movies M WHERE M.id = GM.movie_id and G.id = GM.genre_id ";
        

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
        table = table + "</td><td rowspan=\"2\">";
        while (gStar.next()) {
          String genre = gStar.getString("name");
          table = table + "<p>" + genre + "</p>";
        }
        table = table + "</td></tr>";
        table = table + "<tr><td><a href=\"ShoppingCart?id=" + m_ID + "&quantity=1\">" + "Add to Cart</a></td></tr>";
        out.println(table);
      }
      out.println("</TABLE>");
      

      Integer rowCount = null;
      while (rowcount.next()) {
        rowCount = Integer.valueOf(rowcount.getInt(1));
      }
      


      if ((limit != null) && 
        (rowCount.intValue() > 0)) {
        double l = Math.ceil(rowCount.intValue() / Double.parseDouble(limit));
        int z = (int)l;
        String f = z;
        Integer p = Integer.valueOf(Integer.parseInt(page));
        if ((page.equals("1")) && (z > 1)) {
          out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p.intValue() + 1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Next</button></a>");
        }
        else if ((page.equals(f)) && (z != 1)) {
          out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p.intValue() - 1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Prev</button></a>");
        } else if (z != 1) {
          out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p.intValue() - 1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Prev</button></a>");
          out.println("<a href = " + uri.substring(0, uri.indexOf("&page=")) + "&page=" + (p.intValue() + 1) + "><button style=\"margin:auto; display:block; padding:2em; text-align:center;\">Next</button></a>");
        }
      }
      
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