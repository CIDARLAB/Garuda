/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

/**
 *
 * @author mardian
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.clothocad.core.persistence.ClothoConnection;
import org.clothocad.core.security.CredentialStore;

/**
 *
 * @author spaige
 */
public class JongoTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CredentialStore.class).to(TestEnvConnection.class);
        bind(TestEnvConnection.class).in(Singleton.class);
        bind(ClothoConnection.class).to(TestEnvConnection.class);
        bind(RolePermissionResolver.class).to(TestEnvConnection.class);
    }

}
