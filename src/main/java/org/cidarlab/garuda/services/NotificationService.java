/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

/**
 *
 * @author mardian
 */
public interface NotificationService {
    
    void addInfoMessage(String msg);
    void addErrorMessage(String msg);
}
