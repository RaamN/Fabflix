import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StoredProcedure extends javax.servlet.http.HttpServlet
{
  private static final long serialVersionUID = 1L;
  
  public StoredProcedure() {}
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException
  {
    String loginUser = "mytestuser";
    String loginPasswd = "mypassword";
    String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
    
    response.setContentType("text/html");
    

    PrintWriter out = response.getWriter();
    
    out.println("<HTML><HEAD><TITLE>Results</TITLE>");
    out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\"><link href=\"css/bootstrap.min.css\" rel=\"stylesheet\"></HEAD>");
    

    out.println("<BODY style=\"background-color: lightgrey;\"><h2 style=\"display: inline-block;z-index: 1;padding:1em;\"><a href=\"dashboard\">Dashboard</a></h2>");
    



    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      
      Connection dbcon = java.sql.DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
      


      String meta = request.getParameter("meta");
      String moviestar = request.getParameter("star");
      String movie = request.getParameter("movie");
      





      String title = request.getParameter("title");
      String y = request.getParameter("year");
      Integer year = null;
      String director = request.getParameter("director");
      


      String first_name = request.getParameter("fname");
      String last_name = request.getParameter("lname");
      


      String gname = request.getParameter("gname");
      







      if (y != null) {
        year = Integer.valueOf(Integer.parseInt(y));
      }
      

      if (moviestar != null) {
        Statement statement = dbcon.createStatement();
        
        String insert = "";
        
        if (last_name.equals("")) {
          insert = insert + "INSERT INTO stars(first_name, last_name) VALUES(\"\",\"" + first_name + "\")";
          out.println("<h3 style=\"text-align:center;\">Inserted " + first_name + " into database!</h3>");
        }
        else {
          insert = insert + "INSERT INTO stars(first_name, last_name) VALUES(\"" + first_name + "\",\"" + last_name + "\")";
          out.println("<h3 style=\"text-align:center;\">Inserted " + first_name + " " + last_name + " into database!</h3>");
        }
        
        int i = statement.executeUpdate(insert);
      }
      

      if (meta != null)
      {
        DatabaseMetaData databaseMetaData = dbcon.getMetaData();
        ResultSet result = databaseMetaData.getTables(null, null, null, null);
        
        out.println("<h2 style=\"text-align: center;\">Metadata of Moviedb</h2>");
        out.println("<TABLE border align=\"center\">");
        
        while (result.next())
        {
          String table = result.getString(3);
          out.println("<tr><td style=\"padding:1em;\">TABLE NAME: " + table + "</td></tr>");
          
          ResultSet columns = databaseMetaData.getColumns(null, null, table, null);
          
          out.println("<tr><td style=\"text-align: left; padding: 1em;\">");
          while (columns.next()) {
            out.println("COLUMN NAME: " + String.format("%-15s", new Object[] { columns.getString("COLUMN_NAME") }) + columns.getString("TYPE_NAME"));
            out.println("</br>");
          }
          out.println("</td></tr>");
        }
        out.println("</table>");
      }
      



      if (movie != null)
      {
        String call = "{call add_movie(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
        CallableStatement state = dbcon.prepareCall(call);
        state.setInt(1, 0);
        state.setString(2, title);
        state.setInt(3, year.intValue());
        state.setString(4, director);
        
        state.setInt(5, 0);
        state.setString(6, first_name);
        state.setString(7, last_name);
        
        state.setInt(8, 0);
        state.setString(9, gname);
        
        state.registerOutParameter(10, 12);
        state.registerOutParameter(11, 12);
        state.registerOutParameter(12, 12);
        state.registerOutParameter(13, 12);
        
        state.executeUpdate();
        
        String error = state.getString(10);
        String movie_status = state.getString(11);
        String star_status = state.getString(12);
        String genre_status = state.getString(13);
        
        if (error != null) {
          out.println("<h3 style='text-align:center;'>Error: " + error + "</h3>");
        }
        else
        {
          out.println("<h3 style=\"text-align:center;\">Movie status: " + movie_status + "</h3>");
          out.println("<h3 style=\"text-align:center;\">Star status: " + star_status + "</h3>");
          out.println("<h3 style=\"text-align:center;\">Genre status: " + genre_status + "</h3>");
        }
      }
      
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