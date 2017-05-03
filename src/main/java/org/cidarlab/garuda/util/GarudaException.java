/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

/**
 *
 * @author mardian
 */
public class GarudaException extends Exception {
    //Parameterless Constructor

    public GarudaException() {
    }

    //Constructor that accepts a message
    public GarudaException(String message) {
        super(message);
    }
}
