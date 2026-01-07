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

@WebServlet(urlPatterns = "/api/v1/item")
public class ItemServlet extends HttpServlet {

    private BasicDataSource ds;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        ds = (BasicDataSource) ctx.getAttribute("ds");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        JsonObject response = new JsonObject();
        JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);

        String id = body.get("id").getAsString();
        String name = body.get("name").getAsString();
        double price = body.get("price").getAsDouble();
        int qty = body.get("qty").getAsInt();

        String sql = "INSERT INTO item (id, name, price, qty) VALUES (?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, qty);

            ps.executeUpdate();

            resp.setStatus(HttpServletResponse.SC_CREATED);
            response.addProperty("status", "OK");
            response.addProperty("message", "Item saved successfully");

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.addProperty("status", "ERROR");
            response.addProperty("message", e.getMessage());
        }

        resp.getWriter().print(response.toString());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        JsonArray items = new JsonArray();
        String sql = "SELECT * FROM item";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                JsonObject item = new JsonObject();
                item.addProperty("id", rs.getString("id"));
                item.addProperty("name", rs.getString("name"));
                item.addProperty("price", rs.getDouble("price"));
                item.addProperty("qty", rs.getInt("qty"));
                items.add(item);
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(items.toString());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("status", "ERROR");
            error.addProperty("message", e.getMessage());
            resp.getWriter().print(error.toString());
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        JsonObject response = new JsonObject();
        JsonObject body = gson.fromJson(req.getReader(), JsonObject.class);

        String id = body.get("id").getAsString();
        String name = body.get("name").getAsString();
        double price = body.get("price").getAsDouble();
        int qty = body.get("qty").getAsInt();

        String sql = "UPDATE item SET name=?, price=?, qty=? WHERE id=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, qty);
            ps.setString(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                response.addProperty("status", "OK");
                response.addProperty("message", "Item updated successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.addProperty("status", "NOT_FOUND");
                response.addProperty("message", "Item not found");
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.addProperty("status", "ERROR");
            response.addProperty("message", e.getMessage());
        }

        resp.getWriter().print(response.toString());
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        JsonObject response = new JsonObject();
        String id = req.getParameter("id");

        if (id == null || id.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.addProperty("status", "BAD_REQUEST");
            response.addProperty("message", "Item ID is required");
            resp.getWriter().print(response.toString());
            return;
        }

        String sql = "DELETE FROM item WHERE id=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                resp.setStatus(HttpServletResponse.SC_OK);
                response.addProperty("status", "OK");
                response.addProperty("message", "Item deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.addProperty("status", "NOT_FOUND");
                response.addProperty("message", "Item not found");
            }

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.addProperty("status", "ERROR");
            response.addProperty("message", e.getMessage());
        }

        resp.getWriter().print(response.toString());
    }
}
