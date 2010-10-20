package se.knowit.developer.files;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.*;
import javax.servlet.*;

/**
 * This servlet is the receiving end of a "<input type="file" name="filepath">" html tag.
 * The class is responsible for uploading the content of the provided file and output a link
 * so that the file can be downloaded by someone else.
 * For every file a UUID is appended so that it won't be possible to find the file without the link. 
 *
 * @author Daniel Oskarsson (daniel.oskarsson@knowit.se)
 *
 */
public class UploadServlet extends HttpServlet {

    private static String SERVER_PREFIX = "http://localhost:8080/files/";

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(request)) {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
            servletFileUpload.setSizeMax(94371840); // 94371840 bytes == 90 megabytes @ 1024 bytes per MB

            List fileItemsList = null;
            try {
                fileItemsList = servletFileUpload.parseRequest(request);
            } catch (FileUploadBase.SizeLimitExceededException e) {
                PrintWriter out = response.getWriter();
                out.println("SizeLimitExceededException: Max Size 90 MB");
                out.close();
            } catch (FileUploadException e) {
                PrintWriter out = response.getWriter();
                out.println("FileUploadException");
                out.close();
            }

            Iterator iterator = fileItemsList.iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = (FileItem)iterator.next();
                if (fileItem.isFormField() == false) {
                    String relativeLink = this.uploadFile(fileItem);
                    PrintWriter out = response.getWriter();
                    out.println(SERVER_PREFIX + relativeLink);
                    out.close();
                }
            }
        }
    }

    private String uploadFile(FileItem fileItem) throws IOException {
        UUID uuid = UUID.randomUUID();
        String root = getServletContext().getRealPath("/");
        String link = uuid.toString() + "/" + fileItem.getName();

        File file = new File(root + link);
        FileUtils.writeStringToFile(file, ""); // To create empty file
        OutputStream outputStream = new FileOutputStream(file);
        InputStream inputStream = fileItem.getInputStream();
        IOUtils.copy(inputStream, outputStream);
        return link;
    }

}
