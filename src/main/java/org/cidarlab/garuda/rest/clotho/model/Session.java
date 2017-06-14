/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.rest.clotho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jayajr
 */
    /*
{
    "user": {
        "_id": "5941157dccd4db3d327e2831",
        "username": "jerome",
        "email": "jerome@email.com",
        "roles": {
            "account": {
                "id": "5941157dccd4db3d327e2832",
                "name": "jerome andaya"
            }
        }
    },

    "session": {
        "userId": "5941157dccd4db3d327e2831",
        "key": "4938dd8a-16f6-47e7-99f4-38dd3ce02822",
        "time": "2017-06-14T11:26:32.335Z",
        "_id": "59411d68ccd4db3d327e2834"
    },
    "authHeader": "Basic NTk0MTFkNjhjY2Q0ZGIzZDMyN2UyODM0OjQ5MzhkZDhhLTE2ZjYtNDdlNy05OWY0LTM4ZGQzY2UwMjgyMg=="
}
    */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {
    
    @Getter
    @Setter
    private String _id;
    
    @Getter
    @Setter
    private String userId;
    
    @Getter
    @Setter
    private String key;
    
    @Getter
    @Setter
    private String time;
    
    @Override
    public String toString(){
        return "session{"
                + "_id='"   + _id       + ","
                + "userId=" + userId    + ","
                + "key="    + key       + ","
                + "time="   + time
                + "}";
    }    
}
