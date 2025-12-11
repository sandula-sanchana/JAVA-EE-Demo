import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/dbcp")
public class DBCPServlet extends HttpServlet {
    BasicDataSource ds ;
    @Override
    public void init() throws ServletException {
        ds = new BasicDataSource();
       ds.setDriverClassName("com.mysql.jdbc.Driver");
       ds.setUrl("jdbc:mysql://localhost:3306/javaeeapp");
       ds.setUsername("root");
       ds.setPassword("92540010");
       ds.setInitialSize(5);
       ds.setMaxTotal(5);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String addr = req.getParameter("address");

        String sql = "INSERT INTO customer VALUES (?,?,?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));
            ps.setString(2, name);
            ps.setString(3, addr);

            resp.getWriter().println(ps.executeUpdate() > 0 ? "Customer Saved" : "Insert Failed");

        } catch (Exception e) {
            resp.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String sql = "SELECT * FROM customer";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             PrintWriter out = resp.getWriter()) {

            while (rs.next()) {
                out.println(
                        "<tr>" +
                                "<td>" + rs.getInt("id") + "</td>" +
                                "<td>" + rs.getString("name") + "</td>" +
                                "<td>" + rs.getString("address") + "</td>" +
                                "</tr>"
                );
            }

        } catch (Exception e) {
            resp.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String addr = req.getParameter("address");

        String sql = "UPDATE customer SET name=?, address=? WHERE id=?";

        try (Connection con =ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, addr);
            ps.setInt(3, Integer.parseInt(id));

            resp.getWriter().println(ps.executeUpdate() > 0 ? "Customer Updated" : "Update Failed");

        } catch (Exception e) {
            resp.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");

        String sql = "DELETE FROM customer WHERE id=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));
            resp.getWriter().println(ps.executeUpdate() > 0 ? "Customer Deleted" : "Delete Failed");

        } catch (Exception e) {
            resp.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

