import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

@WebServlet(urlPatterns = "/data-formats/*")
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

//        System.out.println(req.getParameter("age"));//form data// u need @multiplepartconfig annotation
//
//        Part filePart = req.getPart("image");
//        String fileName = filePart.getSubmittedFileName();
//        System.out.println("fileName:"+fileName);
//        File dir = new File("/home/sandula-sanchana/Desktop/GDSE73/JAVA-EE-THEORY/Day4-Appication26/src/main/resources/images");
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//
//        String uploadPath = dir.getAbsolutePath() + File.separator + fileName;
//        filePart.write(uploadPath);
//
//        resp.getWriter().println("Image uploaded successfully");


        /// //////////////////////////////////////////////////////////////////////////////////////

        //System.out.println(req.getFile);// path variable

    }
}
