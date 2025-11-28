import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/javaeeapp",
                "root",
                "92540010"
        );
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String addr = req.getParameter("address");

        try (Connection con = getConnection()) {
            String query = "INSERT INTO customer VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, name);
            ps.setString(3, addr);

            int inserted = ps.executeUpdate();
            resp.getWriter().println(inserted > 0 ? "Customer Saved" : "Insert Failed");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try (Connection con = getConnection()) {
            String query = "SELECT * FROM customer";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<tr>" +
                        "<td>" + rs.getInt("id") + "</td>" +
                        "<td>" + rs.getString("name") + "</td>" +
                        "<td>" + rs.getString("address") + "</td>" +
                        "</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String addr = req.getParameter("address");

        try (Connection con = getConnection()) {
            String query = "UPDATE customer SET name=?, address=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, addr);
            ps.setInt(3, Integer.parseInt(id));

            int updated = ps.executeUpdate();
            resp.getWriter().println(updated > 0 ? "Customer Updated" : "Update Failed");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");

        try (Connection con = getConnection()) {
            String query = "DELETE FROM customer WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(id));

            int deleted = ps.executeUpdate();
            resp.getWriter().println(deleted > 0 ? "Customer Deleted" : "Delete Failed");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().println("Error: " + e.getMessage());
        }
    }
}
