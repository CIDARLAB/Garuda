/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.clotho.model;

/**
 *
 * @author mardian
 */
public interface Sharable  {
    //Metadata for all Sharables
    public ObjectId getId();
    public Person getAuthor();
    public String getIcon();
    public String getName();
    public String getDescription();
}