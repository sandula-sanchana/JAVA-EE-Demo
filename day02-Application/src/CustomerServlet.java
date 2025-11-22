import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;



@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    ArrayList<CustomerDto> customerDtos=new ArrayList<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("customer doPost");
        String id=req.getParameter("id");
        System.out.println("customer id:"+id);
        String name=req.getParameter("name");
        System.out.println("customer name:"+name);
        String address=req.getParameter("address");
        System.out.println("customer address:"+address);

        customerDtos.add(new CustomerDto(id,name,address));

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<table border='1'>");
        out.println("<tr><th>#</th><th>ID</th><th>Name</th><th>Address</th></tr>");
        int index = 1;
        for(CustomerDto customerDto:customerDtos){
            System.out.println(customerDto);
            out.println(
                    "<tr>" +
                            "<td>" + index + "</td>" +
                            "<td>" + customerDto.getId() + "</td>" +
                            "<td>" + customerDto.getName() + "</td>" +
                            "<td>" + customerDto.getAddress() + "</td>" +
                            "</tr>"
            );
            index++;
        }
        out.println("</table>");
    }
}