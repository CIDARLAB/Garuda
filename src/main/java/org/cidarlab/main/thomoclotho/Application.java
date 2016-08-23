/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.thomoclotho.util.InitConstructs;
import org.cidarlab.main.thomoclotho.util.InitGeneratedData;
import org.cidarlab.main.thomoclotho.util.InitMetadata;
import org.cidarlab.main.thomoclotho.util.InitParts;
import org.cidarlab.main.thomoclotho.util.InitQC;
import org.cidarlab.main.thomoclotho.util.InitRNASeq;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.BioDesign;
import org.clothocad.model.Feature;
import org.clothocad.model.Person;

/**
 *
 * @author mardian
 */
public class Application {
    
    public static List<Feature> partsID;
    public static List<Feature> constructsID;
    public static List<String> transcriptID;
    public static List<BioDesign> genDataID;
    
    public static void initiate (String username, String password, String xlsInput, String jsonOutput) {
        
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        /////////create user/////////
        clothoObject.createUser(username, password);
        
        /////////login/////////
        Object loginRet = clothoObject.login(username, password);
        
        if (loginRet == null) {
            conn.closeConnection();
            return;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            return;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return;
        }
        
        //query if there has already been a person instance with the same username
        /////////fix me/////////
        Person user = new Person (username);
        
        XLSReader(xlsInput, jsonOutput, clothoObject, user);
        
        //query for testing
        testQuery (clothoObject);
    }
    
    public static void XLSReader (String inputUrl, String outputUrl, Clotho clothoObject, Person user) {
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            
            //initiation of static lists that hold shared variables
            partsID = new ArrayList<Feature> ();
            constructsID = new ArrayList<Feature> ();
            transcriptID = new ArrayList<String> ();
            genDataID = new ArrayList<BioDesign> ();
                
            for (int i=0; i<workbook.getNumberOfSheets(); i++) {
            //for (int i=0; i<4; i++) {
                System.out.println("------" + i + "-----");
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName ();
                //String outputFileUrl = outputUrl + sheetName + ".txt";
                
                switch (sheetName) {
                    case "Metadata":
                    case "metadata":
                        InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Parts":
                    case "parts":
                        InitParts.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Constructs":
                    case "constructs":
                        InitConstructs.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Generated Data":
                    case "generated data":
                        InitGeneratedData.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "RNASeq":
                    case "rnaseq":
                        InitRNASeq.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "QC":
                    case "qc":
                        //InitQC.instantiate (sheet, outputUrl, clothoObject);
                        break;
                    default:
                        break;
                }
            }
            inputFile.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println ("File not found! Please enter the correct file name or url!");
        }
        catch (Exception ex) {
            ex.printStackTrace ();
        }
    }
    
    public static void testQuery (Clotho clothoObject) {
        
        Map queryMap = new HashMap();
        
        //queryMap.put("Vector", "pLac861");
        //queryMap.put("Measurement Conditions", "LCMS");
        
        queryMap.put("name", "medium1");
        
        JSONArray queryResults = (JSONArray) clothoObject.query (queryMap);
        //System.out.println (queryResults);
        
    }
}