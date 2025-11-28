import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/slc")
public class ServletLifeCycle extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("ServletLifeCycle init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("ServletLifeCycle doGet");
    }

    @Override
    public void destroy() {
        System.out.println("ServletLifeCycle destroy");
    }
}
