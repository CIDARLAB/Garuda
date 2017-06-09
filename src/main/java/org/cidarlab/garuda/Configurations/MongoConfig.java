/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Configurations;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 *
 * @author jayajr
 */
@Configuration
@EnableMongoRepositories(basePackages = "org.cidarlab.garuda.Database")
public class MongoConfig extends AbstractMongoConfiguration{
    

    @Override
    protected String getDatabaseName() {
        return "accountDatabase";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1", 27017);
    }
    
    @Override
    protected String getMappingBasePackage(){
        return "org.cidarlab";
    }
}
