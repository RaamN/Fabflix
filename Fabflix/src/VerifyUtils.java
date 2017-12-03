import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;











public class VerifyUtils
{
  public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
  
  public VerifyUtils() {}
  
  public static boolean verify(String gRecaptchaResponse)
  {
    if ((gRecaptchaResponse == null) || (gRecaptchaResponse.length() == 0))
    {
      return false;
    }
    

    try
    {
      URL verifyUrl = new URL("https://www.google.com/recaptcha/api/siteverify");
      




      HttpsURLConnection conn = (HttpsURLConnection)verifyUrl.openConnection();
      



      conn.setRequestMethod("POST");
      
      conn.setRequestProperty("User-Agent", "Mozilla/5.0");
      
      conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      



      String postParams = "secret=6Lcs6x8UAAAAAEYa8v3ZkJmpvs1yZ4YKk38rK-1k&response=" + gRecaptchaResponse;
      




      conn.setDoOutput(true);
      






      OutputStream outStream = conn.getOutputStream();
      
      outStream.write(postParams.getBytes());
      


      outStream.flush();
      
      outStream.close();
      












      InputStream is = conn.getInputStream();
      


      JsonReader jsonReader = Json.createReader(is);
      
      JsonObject jsonObject = jsonReader.readObject();
      
      jsonReader.close();
      








      return jsonObject.getBoolean("success");

    }
    catch (Exception e)
    {

      e.printStackTrace();
    }
    return false;
  }
}