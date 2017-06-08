/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.MongoDb;

import org.springframework.data.annotation.Id;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jayajr
 */
public class Account {
    
    @Id
    public String id;
    
    @Getter
    @Setter
    private String userName;
    
    @Getter
    @Setter
    private String email;
    
    @Getter
    @Setter
    private String encryptedPasswd;
    
    public Account(){};
    
    public Account(
            String userName,
            String email,
            String passwd)
            throws NoSuchAlgorithmException{
        
        this.userName = userName;
        this.email = email;
        this.encryptedPasswd = encrypt(passwd);
    }
    
    private String encrypt(String passwd) throws NoSuchAlgorithmException{
        
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(passwd.getBytes());
        
        return new String(messageDigest.digest());
    }
    
    @Override
    public String toString() {
        return String.format(
                "Account[id=%s, Username='%s', E-mail='%s']",
                id, userName, email);
    }
}
