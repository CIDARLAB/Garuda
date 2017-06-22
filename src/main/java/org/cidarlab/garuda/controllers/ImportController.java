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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/import")
public class ImportController {
    
    private String fileLocation;
    
    @RequestMapping(method=RequestMethod.GET)
    public String getImportPage(HttpSession session) {        
        return "import";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String importFile(
            MultipartFile file,
            HttpSession session,
            Model model)
            throws IOException{
        
        InputStream in = file.getInputStream();
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
        FileOutputStream f = new FileOutputStream(fileLocation);
        int ch = 0;
        while ((ch=in.read()) != -1){
            f.write(ch);
        }
        
        f.flush();
        f.close();
        
        model.addAttribute("message", "File: " + file.getOriginalFilename() + " has been uploaded successfully!");
                
        return "/excel";
    }
}

