package com.northwest.lms.controllers;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
@RestController
public class DownloadController {

    @GetMapping("/download/{fileName:.+}")
    public void getLogFile(HttpServletResponse response, @PathVariable("fileName") String fileName) throws Exception {
        try {
            String filePathToBeServed = "C:\\lms\\";
            File fileToDownload = new File(filePathToBeServed + fileName);
            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception exception) {
            System.out.println("File does not Exist");
        }

    }
}
