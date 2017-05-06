/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import org.apache.shiro.subject.Subject;
import org.clothocad.core.security.ServerSubject;

/**
 *
 * @author mardian
 */
public class AuthorizedShiroTest extends AbstractShiroTest {

    public AuthorizedShiroTest() {
        super();
        Subject subjectUnderTest = new ServerSubject();
        setSubject(subjectUnderTest);     
    }
}

