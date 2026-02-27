package standalone;

import org.apache.hc.client5.http.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ResourceBundle;

public class Tasca_5 {

    public static void main(String[] args) {

    // https://mastodont.cat/api/v1/accounts/109862447110628983/statuses?limit=1
        String URI = "https://mastodont.cat/api/v1/accounts/109862447110628983/statuses?limit=1";
        String TOKEN = ResourceBundle.getBundle("token").getString("token");

        try {
            String output = Request.get(URI)
                    .addHeader("Authorization","Bearer "+TOKEN)
                    .execute()
                    .returnContent()
                    .asString();

            var result = new JSONArray(output);
            JSONObject firsttweet = result.getJSONObject(0);
            String idfirsttweet = firsttweet.getString("id");
            //System.out.println(idfirsttweet);

            URI = "https://mastodont.cat/api/v1/statuses/" + idfirsttweet + "/reblog";
            output = Request.post(URI)
                    .addHeader("Authorization","Bearer "+TOKEN)
                    .execute()
                    .returnContent()
                    .asString();
            System.out.println(output);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
