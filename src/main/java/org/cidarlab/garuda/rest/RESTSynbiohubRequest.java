/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sbolstandard.core2.Component;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.Sequence;
import org.synbiohub.frontend.IdentifiedMetadata;
import org.synbiohub.frontend.SearchCriteria;
import org.synbiohub.frontend.SearchQuery;
import org.synbiohub.frontend.SynBioHubException;
import org.synbiohub.frontend.SynBioHubFrontend;

/**
 *
 * @author mardian
 */
public class RESTSynbiohubRequest {

    private List<String> ids;
    private List<String> unique_parts;
    private Set<URI> uris;
    private Set<String> partdetails;
    
    private int num_of_constructs;
    private int num_of_parts;
    private int construct_size;

    private double[][] data;
    private double[][] label;

    private List<String> partnames;
    private List<String> roles;
    private List<String> sequences;

    public RESTSynbiohubRequest() {

        uris = new HashSet<URI>();
        partdetails = new HashSet<String>();
        ids = new ArrayList<String>();
        construct_size = 0;
        num_of_constructs = 0;
        num_of_parts = 0;
        unique_parts = new ArrayList<String>();

        partnames = new ArrayList<String>();
        roles = new ArrayList<String>();
        sequences = new ArrayList<String>();
    }

    public String getLibSBOLJ() throws SynBioHubException, URISyntaxException {

        String message = "Data retrieved!";
        String root = "https://synbiohub.programmingbiology.org/";
        
        String root_global = "http://synbiohub.org/";

        SynBioHubFrontend sbh = new SynBioHubFrontend(root);

        String key_global = "igem:experience";
        String value_global = "http://wiki.synbiohub.org/wiki/Terms/igem#experience/Fails";
        
        
        String key_owl = "https://www.cidarlab.org/owl/Synbiohub_Demo/owl_experience";
        String value_owl = "works";
        
        
        String key = "role";
        String value = "http://identifiers.org/so/SO:0000804";

        ArrayList<IdentifiedMetadata> metadata = new ArrayList<IdentifiedMetadata>();
        ArrayList<SearchCriteria> sc_list = new ArrayList<SearchCriteria>();

        SearchCriteria sc = new SearchCriteria();
        sc.setKey(key_owl);
        sc.setValue(value_owl);
        sc_list.add(sc);

        SearchQuery sq = new SearchQuery();
        sq.setOffset(0);
        sq.setLimit(10000);
        sq.setCriteria(sc_list);

        try {
            metadata = sbh.search(sq);
        } catch (SynBioHubException e) {
            e.printStackTrace();
            message = "Something wrong!";
        }

        if (metadata.size() == 0) {
            return "Nothing found!";
        }

        message = "*** Found " + metadata.size() + " matches ***";

        int max_size = 0;
        for (int i = 0; i < metadata.size(); i++) {

            System.out.println(">>> iteration:  " + i);
            max_size = 0;
            this.num_of_constructs++;
            ids.add("g" + i);

            SBOLDocument sb = sbh.getSBOL(URI.create(metadata.get(i).getUri()));

            for (ComponentDefinition cd : sb.getComponentDefinitions()) {

                for (Component c : cd.getComponents()) {

                    System.out.println("*** uri: " + c.getDefinitionURI().toString());
                    uris.add(c.getDefinitionURI());
                    max_size++;

                    String uri = c.getDefinitionURI().toString();
                    if (!uri.startsWith(root)) {
                        continue;
                    }

                    SBOLDocument sb_in = sbh.getSBOL(c.getDefinitionURI());

                    for (ComponentDefinition cd_in : sb_in.getComponentDefinitions()) {

                        System.out.println("+++ part: " + cd_in.getDisplayId() + ", role uri: " + cd_in.getRoles().iterator().next());
                        partdetails.add(cd_in.getDisplayId().toString() + "," + cd_in.getRoles().iterator().next().toString() + "," + cd_in.getSequences().iterator().next().getElements());

                        if (!unique_parts.contains(cd_in.getDisplayId())) {
                            unique_parts.add(cd_in.getDisplayId());
                        }
                    }
                }
            }
        }

        this.construct_size = (max_size > this.construct_size) ? max_size : this.construct_size;
        this.num_of_parts = unique_parts.size();

        System.out.println("Size: " + this.construct_size + "\tConstructs: " + this.num_of_constructs + "\tParts: " + this.num_of_parts);

        write_to_file("resources/uris.txt", "resources/roles.txt");

        return message;
    }

    /*private void createRecommendation() {

        this.data = new double[num_of_constructs][num_of_parts];
        this.label = new double[num_of_constructs][1];

        for (int i = 0; i < num_of_constructs; i++) {
            if (Math.random()) {
                label[i][0] = 1.0;
            } else {
                label[i][0] = 0.0;
            }

        }
    }*/

    public String postLibSBOLJ(String username, String password) {

        String message = "Data uploaded!";
        String root = "https://synbiohub.programmingbiology.org/";

        read_uris_file("resources/uris.txt");
        read_details_file("resources/roles.txt");

        try {
            SynBioHubFrontend sbh = new SynBioHubFrontend(root);

            SBOLDocument document = new SBOLDocument();
            document.setDefaultURIprefix("https://www.cidarlab.org/garuda/synbiohub/");
            document.setComplete(true);
            document.setCreateDefaults(true);

            /*Collection col = document.createCollection("Mardian", "");
            col.setName("Garuda's Magic");
            col.setDescription("Collection of parts generated by Garuda");
            
            for (URI uri : uris) {
                col.addMember(uri);
            }*/
            for (int i = 0; i < partnames.size(); i++) {
                System.out.println(partnames.get(i) + " 1.0 " + URI.create(roles.get(i)).toString());
                ComponentDefinition cds = document.createComponentDefinition(partnames.get(i), "1.0", URI.create(roles.get(i)));
                cds.addSequence(document.createSequence(partnames.get(i) + "_seq", "1.0", sequences.get(i), Sequence.IUPAC_DNA));
            }

            //SBOLWriter.write(document,(System.out));
            sbh.login(username, password);
            sbh.submit("garuda_20170531", "1.0", "Garuda Parts", "This is a description", "", "Garuda_magic", "1", document);
        
        } catch (Exception e) {
            message = "Something wrong!";
        } finally {
            return message;
        }
    }

    private void write_to_file(String filename, String rolesname) {

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            fw = new FileWriter(filename);
            bw = new BufferedWriter(fw);

            for (URI uri : uris) {

                bw.write(uri.toString() + "\n");

            }

            System.out.println("***URIs written!!");

            bw.close();
            fw.close();

            fw = new FileWriter(rolesname);
            bw = new BufferedWriter(fw);

            for (String s : partdetails) {

                bw.write(s + "\n");

            }

            System.out.println("***Roles written!!");

            bw.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void read_uris_file(String input) {

        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(input);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                uris.add(URI.create(sCurrentLine));
            }

            br.close();
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void read_details_file(String input) {

        BufferedReader br = null;
        FileReader fr = null;

        try {

            fr = new FileReader(input);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] st = sCurrentLine.split(",");
                partnames.add(st[0]);
                roles.add(st[1]);
                sequences.add(st[2]);
            }

            br.close();
            fr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
