package edu.csumb.spring19.capstone.config;

import com.mongodb.MongoClient;
import edu.csumb.spring19.capstone.config.mongoConverters.StringToGrantedAuthorityConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class Mongo extends AbstractMongoConfiguration {
    private final String host = "userdb", database = "userdb";
    private final Integer port = 27017;
    private final List<Converter<?, ?>> converters = new ArrayList<>();

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
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
