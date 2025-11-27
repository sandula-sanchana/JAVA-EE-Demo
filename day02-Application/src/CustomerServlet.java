import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;



@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    public  static ArrayList<CustomerDto> customers = new ArrayList<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String id= req.getParameter("id");
       String name= req.getParameter("name");
       String addr= req.getParameter("address");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaeeapp","root","92540010");
            String query = "insert into customer values(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            int idInt = Integer.parseInt(id);
            ps.setInt(1,idInt );
            ps.setString(2, name);
            ps.setString(3, addr);
            int rowInserted = ps.executeUpdate();
            if(rowInserted>0){
                resp.getWriter().println("Customer Save Successfully");
            }else {
                resp.getWriter().println("Customer Save Failed");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out=resp.getWriter();

        for(CustomerDto c:customers){
            out.println("<tr>"+"<td>"+c.getId()+"</td>"+"<td>"+c.getName()+"</td>"+"<td>"+c.getAddress()+"</td>"+"</tr>");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id").trim();
        String name = req.getParameter("name");
        String addr = req.getParameter("address");
        System.out.println(customers.size());
        boolean updated = false;

        for (CustomerDto c : customers) {
            if (c.getId().equals(id)) {
                c.setName(name);
                c.setAddress(addr);
                updated = true;
                System.out.println(customers.size());
                System.out.println(c);
                break;
            }
        }

        if (!updated) {
            customers.add(new CustomerDto(id, name, addr));
            //System.out.println(customers.size());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id").trim();

        customers.removeIf(c -> c.getId().equals(id));
    }
}