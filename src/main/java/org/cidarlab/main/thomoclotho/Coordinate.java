/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import lombok.Getter;

/**
 *
 * @author mardian
 */
public class Coordinate {

    @Getter
    private int startX;
    @Getter
    private int endX;

    public Coordinate(int startX, int endX) {
        this.startX = startX;
        this.endX = endX;
    }
}
