/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Database;

import org.springframework.data.annotation.Id;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author jayajr
 */
@Document(collection = "accounts")
public class Account {
    
    @Id
    @Getter
    @Setter
    private String id;
    
    @Getter
    @Setter
    private String username;
    
    @Getter
    @Setter
    private String email;
    
    
    @Getter
    @Setter
    private String passwd;
    
//    @Getter
//    @Setter
//    private String encryptedPasswd;
    
    public Account(
            String username,
            String email,
            String passwd) {
            //throws NoSuchAlgorithmException{ //encrypt exception
        
        this.username = username;
        this.email = email;
        this.passwd = passwd;
        //this.encryptedPasswd = encrypt(passwd);
    }
    
    public static String encrypt(String passwd) throws NoSuchAlgorithmException{
        
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(passwd.getBytes());
        
        return new String(messageDigest.digest());
    }
    
    @Override
    public String toString() {
        return String.format(
                "Account{id=%s, userName='%s', email='%s'}",
                id, username, email);
    }
}
