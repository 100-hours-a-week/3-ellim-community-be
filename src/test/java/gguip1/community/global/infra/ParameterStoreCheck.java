//package gguip1.community.global.infra;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Arrays;
//
//@SpringBootTest(classes = ParameterStoreOnlyTest.TestConfig.class)
//@ActiveProfiles("prod")
//class ParameterStoreOnlyTest {
//    @Value("${cors.allowed-origins:FAILED}")
//    private String allowedOrigins;
//
//    @Configuration
//    @EnableAutoConfiguration(exclude = {
//            DataSourceAutoConfiguration.class,
//            HibernateJpaAutoConfiguration.class
//    })
//    static class TestConfig {
//    }
//
//    @Test
//    void parameterStoreOnlyTest() {
//        System.out.println("allowedOrigins = " + allowedOrigins);
//        System.out.println("allowedOrigins List = " + Arrays.toString(allowedOrigins.split(",")));
//    }
//}
