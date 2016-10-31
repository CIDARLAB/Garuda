/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import static org.cidarlab.main.thomoclotho.Application.constructsID;
import static org.cidarlab.main.thomoclotho.Application.genDataID;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.model.BasicModule;
import org.clothocad.model.BioDesign;
import org.clothocad.model.Compound;
import org.clothocad.model.Compound.CompoundType;
import org.clothocad.model.ExperimentalDesign;
import org.clothocad.model.Feature;
import org.clothocad.model.Medium;
import org.clothocad.model.Module.ModuleRole;
import org.clothocad.model.Parameter;
import org.clothocad.model.Person;
import org.clothocad.model.Variable;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitGeneratedData {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Clotho clothoObject, Person user) {
        
        try {
            FileWriter medJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-medium.txt");
            FileWriter modJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-module.txt");
            FileWriter bioJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-bioDesign.txt");
            FileWriter varJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-variable.txt");
            FileWriter parJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-parameter.txt");
            FileWriter exdJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-experimentalDesign.txt");
            FileWriter cmpJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-compound.txt");
            FileWriter proJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-project-update.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject modJSON = new JSONObject();
            JSONArray modArr = new JSONArray();
            
            JSONObject bioJSON = new JSONObject();
            JSONArray bioArr = new JSONArray();
            
            JSONObject medJSON = new JSONObject();
            JSONArray medArr = new JSONArray();
            
            JSONObject varJSON = new JSONObject();
            JSONArray varArr = new JSONArray();
            
            JSONObject parJSON = new JSONObject();
            JSONArray parArr = new JSONArray();
            
            JSONObject exdJSON = new JSONObject();
            JSONArray exdArr = new JSONArray();
            
            JSONObject cmpJSON = new JSONObject();
            JSONArray cmpArr = new JSONArray();
            
            JSONObject proJSON = new JSONObject();
            JSONArray proArr = new JSONArray();
            
            //counter for clotho
            int[] clothoCount = new int[9];
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                Set<Variable> response = new HashSet<Variable>();
                Set<Variable> controlled = new HashSet<Variable>();
                
                //-----medium----- [col 4]
                String medname = row.getCell(4).getStringCellValue();
                Medium newMed = new Medium (medname, "", user);
                
                JSONObject medObj = newMed.getJSON();
                Map medMap = newMed.getMap();
                String medClo = (String) clothoObject.create(medMap);
                if (!medClo.equals(null)) {
                    clothoCount[0]++;
                }
                medArr.add(medObj);
                
                //-----transforming from construct to module/biodesign [col 2]
                int bioDesignIdx = (int) row.getCell(2).getNumericCellValue();
                
                //-----module-----
                String modname = "mod" + System.currentTimeMillis();
                Set<Feature> features = new HashSet<Feature>();
                features.add(constructsID.get(bioDesignIdx-1));
                BasicModule newMod = new BasicModule(modname, "", ModuleRole.TRANSCRIPTION, features, user);
                
                JSONObject modObj = newMod.getJSON();
                Map modMap = newMod.getMap();
                String modClo = (String) clothoObject.create(modMap);
                if (!modClo.equals(null)) {
                    clothoCount[1]++;
                }
                modArr.add(modObj);
                
                //-----biodesign-----
                String bioname = "bio" + System.currentTimeMillis();
                BioDesign newBiod = new BioDesign (bioname, "", newMod, user);
                //add the medium to the bioDesign
                newBiod.addMedium(newMed);
                
                //-----variable & parameter----- [col 5-9] 
                //5
                    double param = row.getCell(5).getNumericCellValue();
                    Variable newVar = new Variable("Growth Rate", "", user);
                    response.add(newVar);
                    Parameter newParam = new Parameter(param, newVar);
                    
                    JSONObject varObj = newVar.getJSON();
                    Map varMap = newVar.getMap();
                    String varClo = (String) clothoObject.create(varMap);
                    if (!varClo.equals(null)) {
                        clothoCount[3]++;
                    }
                    varArr.add(varObj);
                    
                    //add the parameter to the bioDesign
                    newBiod.addParameter(newParam);
                    
                    JSONObject parObj = newParam.getJSON();
                    Map parMap = newParam.getMap();
                    String parClo = (String) clothoObject.create(parMap);
                    if (!parClo.equals(null)) {
                        clothoCount[4]++;
                    }
                    parArr.add(parObj);
                    
                //6   
                    param = row.getCell(6).getNumericCellValue();
                    newVar = new Variable("Duration", "", user);
                    controlled.add(newVar);
                    newParam = new Parameter(param, newVar);
                    
                    varObj = newVar.getJSON();
                    varMap = newVar.getMap();
                    varClo = (String) clothoObject.create(varMap);
                    if (!varClo.equals(null)) {
                        clothoCount[3]++;
                    }
                    varArr.add(varObj);
                    
                    //add the parameter to the bioDesign
                    newBiod.addParameter(newParam);
                    
                    parObj = newParam.getJSON();
                    parMap = newParam.getMap();
                    parClo = (String) clothoObject.create(parMap);
                    if (!parClo.equals(null)) {
                        clothoCount[4]++;
                    }
                    parArr.add(parObj);
                    
                //7    
                    param = row.getCell(7).getNumericCellValue();
                    newVar = new Variable("Performance", "", user);
                    response.add(newVar);
                    newParam = new Parameter(param, newVar);
                    
                    varObj = newVar.getJSON();
                    varMap = newVar.getMap();
                    varClo = (String) clothoObject.create(varMap);
                    if (!varClo.equals(null)) {
                        clothoCount[3]++;
                    }
                    varArr.add(varObj);
                    
                    //add the parameter to the bioDesign
                    newBiod.addParameter(newParam);
                    
                    parObj = newParam.getJSON();
                    parMap = newParam.getMap();
                    parClo = (String) clothoObject.create(parMap);
                    if (!parClo.equals(null)) {
                        clothoCount[4]++;
                    }
                    parArr.add(parObj);
                    
                //8    
                    param = row.getCell(8).getNumericCellValue();
                    newVar = new Variable("Purity", "", user);
                    response.add(newVar);
                    newParam = new Parameter(param, newVar);
                    
                    varObj = newVar.getJSON();
                    varMap = newVar.getMap();
                    varClo = (String) clothoObject.create(varMap);
                    if (!varClo.equals(null)) {
                        clothoCount[3]++;
                    }
                    varArr.add(varObj);
                    
                    parObj = newParam.getJSON();
                    parMap = newParam.getMap();
                    parClo = (String) clothoObject.create(parMap);
                    if (!parClo.equals(null)) {
                        clothoCount[4]++;
                    }
                    parArr.add(parObj);
                    
                //9
                    param = row.getCell(9).getNumericCellValue();
                    newVar = new Variable("Acidity", "", user);
                    response.add(newVar);
                    newParam = new Parameter(param, newVar);
                    
                    varObj = newVar.getJSON();
                    varMap = newVar.getMap();
                    varClo = (String) clothoObject.create(varMap);
                    if (!varClo.equals(null)) {
                        clothoCount[3]++;
                    }
                    varArr.add(varObj);
                    
                    //add the parameter to the bioDesign
                    newBiod.addParameter(newParam);
                    
                    parObj = newParam.getJSON();
                    parMap = newParam.getMap();
                    parClo = (String) clothoObject.create(parMap);
                    if (!parClo.equals(null)) {
                        clothoCount[4]++;
                    }
                    parArr.add(parObj);
                    
                genDataID.add(newBiod);
                
                //add bioDesign to JSON and clotho later
                JSONObject bioObj = newBiod.getJSON();
                Map bioMap = newBiod.getMap();
                String bioClo = (String) clothoObject.create(bioMap);
                if (!bioClo.equals(null)) {
                    clothoCount[2]++;
                }
                bioArr.add(bioObj);
                
                //------experimental design------ [col 3 & 10 for integration site & comment]
                String exdname = "exd" + System.currentTimeMillis();
                int site = (int) row.getCell(3).getNumericCellValue();
                String notes = row.getCell(10).getStringCellValue();
                ExperimentalDesign newExd = new ExperimentalDesign (exdname, "", response, controlled, newBiod, site, notes, user);
                
                JSONObject exdObj = newExd.getJSON();
                Map exdMap = newExd.getMap();
                String exdClo = (String) clothoObject.create(exdMap);
                if (!exdClo.equals(null)) {
                    clothoCount[5]++;
                }
                exdArr.add(exdObj);
                
                //------library------ [col 1] --updating projects
                String libraryId = row.getCell(1).getStringCellValue();
                Map queryMap = new HashMap();
                queryMap.put("libraryId", libraryId);
                JSONArray queryResults = (JSONArray) clothoObject.query (queryMap);
                
                for (int j=0; j<queryResults.size(); j++) {
                    Map queryResult = (Map) queryResults.get(j);
                    //String objectId = (String) queryResult.get("id");
                    queryResult.put("experimentalDesign", exdname);
                    String setResult = (String) clothoObject.set(queryResult);
                    if (!setResult.equals(null)) {
                        clothoCount[8]++;
                    }
                    proArr.add(queryResult);
                }

                //------protein and metabolite [col 12-...]------
                int compounds = (int) row.getCell(11).getNumericCellValue();
                
                for (int j=0; j<compounds; j++) {
                    
                    CompoundType type = CompoundType.PROTEIN;
                    Cell cell = row.getCell(12+(j*6));
                    if ((int) cell.getNumericCellValue()==1) {
                        type = CompoundType.METABOLITE;
                    }
                    
                    String cmpname = "met" + System.currentTimeMillis();
                    String desc = row.getCell(13+(j*6)+0).getStringCellValue();
                    boolean isProduct = row.getCell(13+(j*6)+1).getNumericCellValue()==1;
                    String identifier = row.getCell(13+(j*6)+2).getStringCellValue();
                    double level = row.getCell(13+(j*6)+3).getNumericCellValue();
                    String formula = row.getCell(13+(j*6)+4).getStringCellValue();
                    
                    Compound newComp = new Compound(cmpname, desc, user, type, isProduct, identifier, level, formula);
                    
                    JSONObject cmpObj = newComp.getJSON();
                    Map cmpMap = newComp.getMap();
                    String cmpClo = (String) clothoObject.create(cmpMap);
                    if (!cmpClo.equals(null)) {
                        if (type==CompoundType.METABOLITE)
                            clothoCount[6]++;
                        else if (type==CompoundType.PROTEIN)
                            clothoCount[7]++;
                    }
                    cmpArr.add(cmpObj);
                }
            }
            
            System.out.println("Created " + clothoCount[0] + " Medium objects" + "\n" +
                                "Created " + clothoCount[1] + " Module objects" + "\n" +
                                "Created " + clothoCount[2] + " BioDesign objects" + "\n" +
                                "Created " + clothoCount[3] + " Variable objects" + "\n" +
                                "Created " + clothoCount[4] + " Parameter objects" + "\n" +
                                "Created " + clothoCount[5] + " Experimental Design objects" + "\n" +
                                "Created " + clothoCount[6] + " Metabolite objects" + "\n" +
                                "Created " + clothoCount[7] + " Protein objects" + "\n" +
                                "Updated " + clothoCount[8] + " Project objects");
            
            medJSON.put("Name", "Medium");
            medJSON.put("Entries", medArr);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(medJSON);
            medJSONfile.write (prettyJson);
            
            modJSON.put("Name", "Module");
            modJSON.put("Entries", modArr);
            
            prettyJson = gson.toJson(modJSON);
            modJSONfile.write (prettyJson);
            
            bioJSON.put("Name", "BioDesign");
            bioJSON.put("Entries", bioArr);
            
            prettyJson = gson.toJson(bioJSON);
            bioJSONfile.write (prettyJson);
            
            varJSON.put("Name", "Variable");
            varJSON.put("Entries", varArr);
            
            prettyJson = gson.toJson(varJSON);
            varJSONfile.write (prettyJson);
            
            parJSON.put("Name", "Parameter");
            parJSON.put("Entries", parArr);
            
            prettyJson = gson.toJson(parJSON);
            parJSONfile.write (prettyJson);
            
            exdJSON.put("Name", "Experiment Design");
            exdJSON.put("Entries", exdArr);
            
            prettyJson = gson.toJson(exdJSON);
            exdJSONfile.write (prettyJson);
            
            cmpJSON.put("Name", "Compound");
            cmpJSON.put("Entries", cmpArr);
            
            prettyJson = gson.toJson(cmpJSON);
            cmpJSONfile.write (prettyJson);
            
            proJSON.put("Name", "Project");
            proJSON.put("Entries", proArr);
            
            prettyJson = gson.toJson(proJSON);
            proJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");
            
            medJSONfile.close();
            modJSONfile.close();
            bioJSONfile.close();
            parJSONfile.close();
            varJSONfile.close();
            exdJSONfile.close();
            cmpJSONfile.close();
            proJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}