package edu.csumb.spring19.capstone.config.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import edu.csumb.spring19.capstone.config.mongo.converters.StringToGrantedAuthorityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Mongo extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.host:userdb}")
    private String host;
    @Value("${spring.data.mongodb.database:userdb}")
    private String database;
    @Value("${spring.data.mongodb.port:27017}")
    private Integer port;
    private final List<Converter<?, ?>> converters = new ArrayList<>();

    @Override
    public MongoClient mongoClient() {        
        return MongoClients.create("mongodb://" + host + ":" + port);
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoCustomConversions customConversions() {
        converters.add(new StringToGrantedAuthorityConverter());
        return new MongoCustomConversions(converters);
    }
}
