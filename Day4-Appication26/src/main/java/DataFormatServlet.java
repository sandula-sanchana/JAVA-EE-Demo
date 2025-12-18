import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/data-formats")
@MultipartConfig
public class DataFormatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();
        System.out.println("contentType:"+contentType);
//        System.out.println(req.getParameter("name"));//for query param

        /// /////////////////////////////////////////////////////////////////

//        System.out.println(req.getParameter("id"));//x-www form url encoded

        /// //////////////////////////////////////////////////////////////

        System.out.println(req.getParameter("age"));//form data// u need @multiplepartconfig annotation
        System.out.println(req.getParameter("image"));
    }
}
