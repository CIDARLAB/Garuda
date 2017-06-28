/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author mardian
 */
public class RESTRequest {
    
    private String url = "https://localhost:8443/data/post";

    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

    public String HTTPReq(URL url, String jsonString, String verb) throws ProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setRequestMethod(verb);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setInstanceFollowRedirects(false);
        if (!verb.equals("GET")) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(jsonString.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        if (responseCode != 400 && responseCode != 404 && responseCode != 500) {
            //print result
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            String alloutput = "";
            while ((output = br.readLine()) != null) {
                alloutput += output;
            }
            conn.disconnect();
            return alloutput;
        } else {
            conn.disconnect();
            return "ERROR " + responseCode;
        }

    }

    public void createUser(String username, String email, String password) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        System.out.println("Testing Create User");
        String jsonString = "{'username':'" + username + "','password':'" + password + "'}";
        URL url = new URL(this.url + "/create/user");

        String output = HTTPReq(url, jsonString, "POST");

        System.out.println(output);
    }
  
    public String createPart(String jsonString) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        //String jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Sequence','sequence':'ata'}";
        //URL url = new URL(this.url + "/create/sequence");
        //String seqId = HTTPReq(url, jsonString, "POST");

        //System.out.println("Testing Create Part");
        //jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Part', 'id':'" + seqId + "'}";
        URL url = new URL(this.url + "/create/conveniencePart/");

        String output = HTTPReq(url, jsonString, "POST");

        //System.out.println(output);
        return output;
    }
    
    public String createDevice(String jsonString) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        //System.out.println("Testing Create Convenience Device");

        /*String jsonString1 = "{'username':'jsmith','objectName':'Test Convenience Device Part1','displayID':'Test Convenience Device Part1','sequence':'tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac', 'role':'GENE', 'params': [{'name':'n', 'value':'121.5', 'variable':'var', 'units' : 'unit'}]}";
        URL url1 = new URL(this.url + "/create/conveniencePart/");

        String jsonString2 = "{'username':'jsmith','objectName':'Test Convenience Device Part2','displayID':'Test Convenience Device Part2', 'sequence':'tccctatcagtgatagagattgacatccctatcgagatactgagcac', 'role':'GENE', 'params': [{'name':'n', 'value':'121.5', 'variable':'var', 'units' : 'unit'}]}";
        URL url2 = new URL(this.url + "/create/conveniencePart/");

        String partID1 = HTTPReq(url1, jsonString1, "POST");
        String partID2 = HTTPReq(url2, jsonString2, "POST");

        String partIDs = partID1 + "," + partID2;

        String jsonString = "{'username':'jsmith','objectName':'Test Convenience Device','displayID':'Test Convenience Device', 'createSeqFromParts':'False', 'partIDs':'" + partIDs + "'}";*/
        URL url = new URL(this.url + "/create/convenienceDevice/");

        String output = HTTPReq(url, jsonString, "POST");

        //System.out.println(output);
        return output;

        /*System.out.println("Testing Get Convenience Device");

        jsonString = "{'objectName':'Test Convenience Device','displayID':'Test Convenience Device','role':'GENE'}";
        url = new URL(this.url + "/get/convenienceDevice/");
        output = HTTPReq(url, jsonString, "POST");

        System.out.println(output);*/
    }

    public String getPart(String jsonString) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        
        URL url = new URL(this.url + "/get/conveniencePart/");
        return HTTPReq(url, jsonString, "POST");
    }

    public String getDevice(String jsonString) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        
        URL url = new URL(this.url + "/get/convenienceDevice/");
        return HTTPReq(url, jsonString, "POST");
    }

    public String getPartID(String jsonString) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        
        URL url = new URL(this.url + "/get/conveniencePartID/");
        return HTTPReq(url, jsonString, "POST");
    }

    public String getDeviceID(String jsonString) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        
        URL url = new URL(this.url + "/get/convenienceDeviceID/");
        return HTTPReq(url, jsonString, "POST");
    }

    public String createSequence() throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        //System.out.println("Testing Create Sequence");
        String jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Sequence','sequence':'ata'}";
        URL url = new URL(this.url + "/create/sequence");

        String output = HTTPReq(url, jsonString, "POST");

        return output;
    }

    public String testCreateSequence(String jsonString) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        System.out.println("Testing Create Sequence");
        //String jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Sequence','sequence':'ata'}";
        URL url = new URL(this.url + "/create/sequence");

        String output = HTTPReq(url, jsonString, "POST");

        return output;
    }

    public String testCreatePart() throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {

        String jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Sequence','sequence':'ata'}";
        URL url = new URL(this.url + "/create/sequence");
        String seqId = HTTPReq(url, jsonString, "POST");

        System.out.println("Testing Create Part");
        jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Part', 'id':'" + seqId + "'}";
        url = new URL(this.url + "/create/part");

        String output = HTTPReq(url, jsonString, "POST");

        return output;
    }

    public void testGetByName() throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        System.out.println("Testing Get Sequence by Name");

        String jsonString = "{'username':'jsmith','password':'asdf','objectName':'TestSequence','sequence':'ata'}";
        URL url = new URL(this.url + "/create/sequence");
        String seqId = HTTPReq(url, jsonString, "POST");


        url = new URL("https://localhost:8443/data/get/getByName/TestSequence/jsmith:asdf");

        String output = HTTPReq(url, "", "GET");

        System.out.println(output);
    }

    public void getById(String objId, String user, String pass) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {

        //String jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Sequence','sequence':'ata'}";
        //url = new URL(this.url + "/create/sequence");
        //String seqId = HTTPReq(url, jsonString, "POST");

        //System.out.println("Testing Get By Id");
        URL url = new URL("https://localhost:8443/data/get/getById/" + objId + "/" + user + ":" + pass);

        String output = HTTPReq(url, "", "GET");

        System.out.println(output);

    }

    public void testSet() throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {

        String jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Sequence','sequence':'ata'}";
        URL url = new URL(this.url + "/create/sequence");
        String seqId = HTTPReq(url, jsonString, "POST");

        System.out.println("Testing Set");
        jsonString = "{'username':'jsmith','password':'asdf', 'squence' : 'atatatatatatat','id' : '" + seqId + "a'}";
        url = new URL(this.url + "/set");

        String output = HTTPReq(url, jsonString, "PUT");

        System.out.println(output);
    }

    public void testDelete() throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        System.out.println("Testing Delete Sequence");

        String jsonString = "{'username':'jsmith','password':'asdf','objectName':'Test Sequence','sequence':'ata'}";
        URL url = new URL(this.url + "/create/sequence");

        String sequenceId = HTTPReq(url, jsonString, "POST");

        jsonString = "{'username':'jsmith','password':'asdf','id':" + sequenceId + "}";
        url = new URL(this.url + "/delete/delete");

        String output = HTTPReq(url, jsonString, "DELETE");

        System.out.println(output);
    }
    
    public void testConvenienceDevice() throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {
        
        System.out.println("Testing Create Convenience Part");

        String jsonString = "{'username':'jsmith','objectName':'Test Convenience Device Part','sequence':'tccctatcagtgatagagattgacatccctatcagtgatagagatactgagcac', 'role':'GENE'}";
        URL url = new URL(this.url + "/create/conveniencePart/");

        String partIDs = HTTPReq(url, jsonString, "POST");
        
        jsonString = "{'username':'jsmith','objectName':'Test Convenience Device','sequence':'actacttcgcatcatgttcatca', 'role':'GENE', 'partIDs':'" + partIDs +"'}";
        url = new URL(this.url + "/create/convenienceDevice/");

        String output = HTTPReq(url, jsonString, "POST");

        System.out.println(output);
    }
    
}
