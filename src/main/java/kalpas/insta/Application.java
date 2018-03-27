package kalpas.insta;

import kalpas.insta.api.HttpClientFactory;
import kalpas.insta.persist.Neo4jDBServiceFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan ({"org.apache.http", "kalpas.insta"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public CloseableHttpClient httpClientFactory(){
        return HttpClientFactory.getHttpClient();
    }

    @Bean
    public GraphDatabaseService mockService() {
        return Neo4jDBServiceFactory.getMockNeo4jService();
    }

//     <bean id="neo4jDBService" class="kalpas.insta.persist.Neo4jDBServiceFactory"
//    factory-method="getNeo4jService"> <constructor-arg value="insta.db"></constructor-arg>
//		</bean>
}
