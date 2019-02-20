package edu.csumb.spring19.capstone;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = ApplicationTest.Initializer.class)
public class ApplicationTest {
    static final int port = 27017;

    @ClassRule
    public static GenericContainer mongo = new GenericContainer("mongo:4").withExposedPorts(port);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                  "spring.data.mongodb.host=" + mongo.getContainerIpAddress(),
                  "spring.data.mongodb.port=" + mongo.getMappedPort(port)
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @Test
    public void contextLoads() throws Exception {}
}
