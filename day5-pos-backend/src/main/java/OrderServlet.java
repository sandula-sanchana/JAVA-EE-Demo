

import com.google.gson.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = "/api/v1/orders")
public class OrderServlet extends HttpServlet {

    private BasicDataSource ds;

    @Override
    public void init() {
        ServletContext context = getServletContext();
        ds = (BasicDataSource) context.getAttribute("datasource");
    }

    // ================== PLACE ORDER ==================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);

        String orderId = json.get("orderId").getAsString();
        String date = json.get("date").getAsString();
        String customerId = json.get("customerId").getAsString();
        JsonArray orderDetails = json.getAsJsonArray("orderDetails");

        Connection con = null;

        try {
            con = ds.getConnection();
            con.setAutoCommit(false); // 🔐 start transaction

            // 1️⃣ Insert order
            PreparedStatement orderStmt = con.prepareStatement(
                    "INSERT INTO orders (id, date, customer_id) VALUES (?,?,?)"
            );
            orderStmt.setString(1, orderId);
            orderStmt.setDate(2, java.sql.Date.valueOf(date));
            orderStmt.setString(3, customerId);

            if (orderStmt.executeUpdate() == 0) {
                con.rollback();
                sendError(resp, "Order insert failed");
                return;
            }

            // 2️⃣ Prepare statements
            PreparedStatement detailStmt = con.prepareStatement(
                    "INSERT INTO order_details (order_id, item_id, qty, price) VALUES (?,?,?,?)"
            );

            PreparedStatement stockCheckStmt = con.prepareStatement(
                    "SELECT qty_on_hand FROM item WHERE code=?"
            );

            PreparedStatement stockUpdateStmt = con.prepareStatement(
                    "UPDATE item SET qty_on_hand = qty_on_hand - ? WHERE code=?"
            );

            // 3️⃣ Process order details
            for (JsonElement el : orderDetails) {
                JsonObject obj = el.getAsJsonObject();

                String itemCode = obj.get("itemCode").getAsString();
                int qty = obj.get("qty").getAsInt();
                double price = obj.get("unitPrice").getAsDouble();

                // 🔍 Stock check
                stockCheckStmt.setString(1, itemCode);
                ResultSet rs = stockCheckStmt.executeQuery();

                if (!rs.next() || rs.getInt("qty_on_hand") < qty) {
                    con.rollback();
                    sendError(resp, "Insufficient stock for item: " + itemCode);
                    return;
                }

                // 📦 Insert order detail
                detailStmt.setString(1, orderId);
                detailStmt.setString(2, itemCode);
                detailStmt.setInt(3, qty);
                detailStmt.setDouble(4, price);
                detailStmt.executeUpdate();

                // 📉 Update stock
                stockUpdateStmt.setInt(1, qty);
                stockUpdateStmt.setString(2, itemCode);
                stockUpdateStmt.executeUpdate();
            }

            // ✅ Commit transaction
            con.commit();

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"status\":\"OK\",\"message\":\"Order placed successfully\"}");

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ignored) {}

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\":\"ERROR\",\"message\":\"" + e.getMessage() + "\"}");

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ignored) {}
        }
    }


    // ================== GET NEXT ORDER ID ==================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try (Connection con = ds.getConnection()) {

            ResultSet rs = con.prepareStatement(
                    "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1"
            ).executeQuery();

            String nextId = "O001";

            if (rs.next()) {
                int num = Integer.parseInt(rs.getString(1).substring(1)) + 1;
                nextId = String.format("O%03d", num);
            }

            resp.setContentType("application/json");
            resp.getWriter().write("{\"nextOrderId\":\"" + nextId + "\"}");

        } catch (SQLException e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"DB error\"}");
        }
    }

    private void sendError(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(400);
        resp.getWriter().write("{\"error\":\"" + msg + "\"}");
    }
}
