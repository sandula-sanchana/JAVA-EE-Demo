import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/json")
public class JSONProcessing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);
        System.out.println(jsonObject.get("name").getAsString());

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name","thththth");
        jsonObject.addProperty("age",18);
        jsonObject.addProperty("contact",1212121212);
        JsonArray jsonArray = new JsonArray();

        JsonObject address1 = new JsonObject();
        address1.addProperty("street","mathugama");
        address1.addProperty("city","mathugama");
        address1.addProperty("state","mathugama");

        jsonArray.add(address1);
        JsonObject address2 = new JsonObject();
        address2.addProperty("street","scott");
        address2.addProperty("city","scott");
        address2.addProperty("state","scott");
        jsonArray.add(address2);

        jsonObject.add("address",jsonArray);

        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println(jsonObject);
    }
}
