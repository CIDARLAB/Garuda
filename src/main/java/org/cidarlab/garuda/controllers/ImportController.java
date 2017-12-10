/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.services.AquariumParser;
import org.cidarlab.garuda.services.Guy_Parser;
import org.cidarlab.garuda.services.RM_Parser;
import org.cidarlab.garuda.services.RWR_Parser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jayajr
 */
@Controller
public class ImportController {

    @Autowired
    private static Guy_Parser guyparser;

    @Autowired
    private static RM_Parser rmparser;

    @Autowired
    private static RWR_Parser rwrparser;

    @Autowired
    private static AquariumParser aqparser;

    protected String fileLocation;

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public String getImportPage(HttpSession session, Model model) {

        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");

        if (user == null || authHeader == null) {
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());

            return "login";
        }

        return "import";
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importFile(
            @RequestParam("multipartFile") MultipartFile multipartFile,
            HttpSession session,
            Model model)
            throws IOException {

        if (multipartFile.isEmpty()) {
            System.out.println("File empty!");
            return "recommender";
        }

        //{ Uploading the file
        InputStream in = multipartFile.getInputStream();
        File currDir = new File(".");

        String path = currDir.getAbsolutePath();

        fileLocation = path.substring(0, path.length() - 1) + multipartFile.getOriginalFilename();

        FileOutputStream f = new FileOutputStream(fileLocation);
        int ch = 0;

        while ((ch = in.read()) != -1) {
            f.write(ch);
        }

        f.flush();
        f.close();

        System.out.println("file uploaded!");
        System.out.println("location at " + currDir.getAbsolutePath());
        model.addAttribute("message", "File: " + multipartFile.getOriginalFilename() + " has been uploaded successfully!");

        //} Uploading the file
        //{ Parsing the file
        String user = (String) session.getAttribute("username");

        System.out.println(user);

        try {
            if (user.equals("user")) {
                System.out.println("parsing with RW");
                rwrparser.parse(fileLocation, user, session);
            } else if (user.equals("aquarium")) {
                System.out.println("parsing with Aquarium");
                AquariumParser.parse(fileLocation, user, session);
            } else if (user.equals("robwarden")) {
                System.out.println("parsing with RW");
                rwrparser.parse(fileLocation, user, session);
            } else if (user.equals("guy")) {
                System.out.println("parsing with Guy");
                guyparser.parse(fileLocation, "resources/output-", user, session);
            } else if (user.equals("aquariumbot")) {
                System.out.println("parsing with Aquarium");
                aqparser.importData(fileLocation, session);
            } else {
                System.out.println("parsing with RW");
                rwrparser.parse(fileLocation, user, session);
            }

            System.out.println("done parsing");
            model.addAttribute("result", "Import Successful!");
        } catch (RuntimeException e) {
            model.addAttribute("result", "Error: Import aborted.");
            e.printStackTrace();
        }

        //} Parsing the file
        //return "/result";
        return "import";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String getUploadPage(HttpSession session, Model model) {
        return "upload";
    }


    @RequestMapping(value = "/aquarium", method = RequestMethod.GET)
    public String getAquariumImport(HttpSession session, Model model) {
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");

        if (user == null || authHeader == null) {
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());

            return "login";
        }

        return "aquarium";
    }

    @RequestMapping(value = "/aquarium", method = RequestMethod.POST)
    public String postAquariumImport(
            @RequestParam("multipartFile") MultipartFile multipartFile,
            HttpSession session,
            Model model) throws IOException, ParseException {

        if (multipartFile.isEmpty()){
            return "import";
        }

        //{ Uploading the file
        InputStream in = multipartFile.getInputStream();
        File currDir = new File(".");

        String path = currDir.getAbsolutePath();

        fileLocation = path.substring(0, path.length() - 1) + multipartFile.getOriginalFilename();

        FileOutputStream f = new FileOutputStream(fileLocation);
        int ch = 0;

        while ((ch = in.read()) != -1) {
            f.write(ch);
        }

        f.flush();
        f.close();

        System.out.println("file uploaded!");
        System.out.println("location at " + currDir.getAbsolutePath());
        model.addAttribute("message", "File: " + multipartFile.getOriginalFilename() + " has been uploaded successfully!");


        try {
            System.out.println("parsing " + fileLocation + " with aquarium");
            aqparser.importData(fileLocation, session);
        } catch (RuntimeException e) {
            model.addAttribute("result", "Error: Import aborted.");
        }


        return "import";
    }
}
