import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/api/v1/customer")
public class CustomerServlet extends HttpServlet {
    BasicDataSource ds;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ds = (BasicDataSource) servletContext.getAttribute("ds");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        int id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String address = jsonObject.get("address").getAsString();

        String sql = "INSERT INTO customer VALUES (?, ?, ?)";
        JsonObject jo = new JsonObject();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, address);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                jo.addProperty("status", "OK");
                jo.addProperty("message", "Customer saved successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jo.addProperty("status", "ERROR");
                jo.addProperty("message", "Cannot save customer");
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jo.addProperty("status", "ERROR");
            jo.addProperty("message", e.getMessage());
            e.printStackTrace();
        }

        resp.getWriter().print(jo.toString());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        JsonArray ja = new JsonArray();
        String sql = "SELECT * FROM customer";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                JsonObject c = new JsonObject();
                c.addProperty("id", rs.getInt("id"));
                c.addProperty("name", rs.getString("name"));
                c.addProperty("address", rs.getString("address"));
                ja.add(c);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(ja.toString());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject jo = new JsonObject();
            jo.addProperty("status", "ERROR");
            jo.addProperty("message", e.getMessage());
            resp.getWriter().print(jo.toString());
            e.printStackTrace();
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        int id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String address = jsonObject.get("address").getAsString();

        String sql = "UPDATE customer SET name=?, address=? WHERE id=?";
        JsonObject jo = new JsonObject();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                jo.addProperty("status", "OK");
                jo.addProperty("message", "Customer updated successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                jo.addProperty("status", "NOT_FOUND");
                jo.addProperty("message", "Customer not found");
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jo.addProperty("status", "ERROR");
            jo.addProperty("message", e.getMessage());
            e.printStackTrace();
        }

        resp.getWriter().print(jo.toString());
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String idParam = req.getParameter("id");
        JsonObject jo = new JsonObject();

        if (idParam == null || idParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jo.addProperty("status", "BAD_REQUEST");
            jo.addProperty("message", "Customer ID is required");
            resp.getWriter().print(jo.toString());
            return;
        }

        int id = Integer.parseInt(idParam);
        String sql = "DELETE FROM customer WHERE id=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                jo.addProperty("status", "OK");
                jo.addProperty("message", "Customer deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                jo.addProperty("status", "NOT_FOUND");
                jo.addProperty("message", "Customer not found");
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jo.addProperty("status", "ERROR");
            jo.addProperty("message", e.getMessage());
            e.printStackTrace();
        }

        resp.getWriter().print(jo.toString());
    }

}
