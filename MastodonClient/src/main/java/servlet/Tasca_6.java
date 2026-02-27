package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.client5.http.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(value = "/")
public class Tasca_6 extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType ("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONArray result;
        String URI = "https://mastodont.cat/api/v1/accounts/109862447110628983/followers?limit=10";
        String TOKEN = ResourceBundle.getBundle("token").getString("token");

        try {
            String output = Request.get(URI)
                    .addHeader("Authorization", "Bearer " + TOKEN)
                    .execute()
                    .returnContent()
                    .asString();

            result = new JSONArray(output);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }



        out.println("""
        <!DOCTYPE html>
        <html lang='ca'>
        <head>
            <meta charset='UTF-8'>
            <meta name='viewport' content='width=device-width, initial-scale=1.0'>
            <title>Tasca 6</title>
            <link rel='stylesheet' href='styles.css'>
        </head>
        <body>
        """);


        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM 'de' yyyy", new Locale("ca", "ES"));
        String formattedDate = today.format(formatter);
        out.println("<div class=\"header\">\n" +
                "  <h1>Els cinc tuts més recents del comptes seguits per l'usuari 'fib_asw'</h1>\n" +
                "  <p>" + formattedDate + " a les " + java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                + "</p>\n" +
                "</div>");

        for (int i = 0; i < result.length(); i++) {
            JSONObject account = result.getJSONObject(i);
            String avatarurl = account.getString("avatar");
            String username = account.getString("username");
            String displayname = account.getString("display_name");
            String acct = account.getString("acct");
            Integer nfollowers = account.getInt("followers_count");
            JSONArray statuses;

            try {
                URI = "https://mastodont.cat/api/v1/accounts/" + account.getString("id") + "/statuses?limit=5";
                String output = Request.get(URI)
                        .addHeader("Authorization", "Bearer " + TOKEN)
                        .execute()
                        .returnContent()
                        .asString();
                statuses = new JSONArray(output);
            } catch (Exception ex) {
                ex.printStackTrace();

                return;
            }

            // print the html
            out.println(
                    "<div class=\"account\">\n" +
                    "<h2><img src=\"" + avatarurl + "\" alt=\"Avatar\" class=\"account-avatar\"> " + displayname + " (@" + acct + ")</h2>\n" +
                    "</h2>" +
                            "<p>Nombre de seguidors: "+ nfollowers + "</p>\n" +
                    "<div class=\"tuts\">\n"
            );
            for(int j = 0; j < statuses.length(); j++) {
                JSONObject tut = statuses.getJSONObject(j);
                String content = tut.getString("content");
                String createdAt = tut.getString("created_at");
                String dateSpace = createdAt.replace("T", " ").replace("Z", "");
                boolean reblog = tut.getBoolean("reblogged");
                String classes = "tut";
                if(reblog) {
                    classes += " reblog";
                    // get the retut content
                    JSONObject originaltut = tut.getJSONObject("reblog");
                    content = originaltut.getString("content");
                    dateSpace = originaltut.getString("created_at").replace("T", " ").replace("Z", "");
                    Object oaccount = originaltut.get("account");
                    //String originalauthor = " <span class=\"original-author\">(Original: " + oaccount.)
                    //dateSpace = "\uD83D\uDD01 Retut - "+ dateSpace + ;
                }
                out.println("" +
                        "<div class=\""+ classes +"\">" +
                        "<p class=\"timestamp\">" + dateSpace + "</p>\n" +
                        "<div class=\"content\">" + content + "</div>\n" +
                        "</div>\n");
            }




            out.println("</div>\n" +
                    "</div>\n"
                    );

        }

        //TODO TasK#6

        out.println("""
        </body>
        </html>
        """);

    }
}
