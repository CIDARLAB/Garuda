/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.clotho.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mardian
 */
@NoArgsConstructor
public class Lab extends SharableObjBase {

    @Getter
    @Setter
    private String department, address;

    @Getter
    @Setter
    private String website;

    @Getter
    @Setter
    //@Reference
    private Person PI;

    @Getter
    @Setter
    //@Reference
    private Institution institution;
    //TODO: validation: unique name criterion

    public Lab( Institution inst, Person PI, String name, String department, String address ) {
        //XXX: Lab seems to be an example of a Sharable that does not have an author
        super(name, null);
        this.department = department;
        this.address = address;
        institution = inst;
        this.PI = PI;
    }

    public static Lab retrieveByName( String name ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
