package kalpas.insta.config;

import kalpas.insta.api.HttpClientFactory;
import kalpas.insta.persist.Neo4jDBServiceFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"org.apache.http", "kalpas.insta"})
public class AppConfig {


    @Bean
    public CloseableHttpClient httpClientFactory() {
        return HttpClientFactory.getHttpClient();
    }

    @Bean
    public GraphDatabaseService mockService() {
        return Neo4jDBServiceFactory.getMockNeo4jService();
    }

    @Bean("authResponseType")
    public String authResponseType(){
        return "code";
    }

    @Bean("redirectUri")
    public String redirectUri(){
        return "http://localhost:8080/auth/code";
    }

    @Bean("clientSecret")
    public String clientSecret(){
        return "59bb738b95ca4f978a560b8580275fca";
    }

    @Bean("clientId")
    public String clientId(){
        return "798fa31c52e8486db7cbb56efaf4d6f0";
    }

//     <bean id="neo4jDBService" class="kalpas.insta.persist.Neo4jDBServiceFactory"
//    factory-method="getNeo4jService"> <constructor-arg value="insta.db"></constructor-arg>
//		</bean>


//    <!-- DEV INSTA CONFIG -->
//	<!-- <bean id="API" class="kalpas.insta.api.API">
//		<property name="clientId" value="798fa31c52e8486db7cbb56efaf4d6f0" />
//		<property name="clientSecret" value="59bb738b95ca4f978a560b8580275fca" />
//		<property name="authResponseType" value="code" />
//		<property name="redirectUri" value="http://localhost:8080/auth/code" />
//	</bean> -->
//
//	<!-- PRODUCTION INSTA CONFIG -->
//	<bean id="API" class="kalpas.insta.api.API">
//		<property name="clientId" value="cdcfc3e943a74d9e84291b97d62d5c02" />
//		<property name="clientSecret" value="970b5b5dd5e14b2bb75b5873ae9868a3" />
//		<property name="authResponseType" value="code" />
//		<property name="redirectUri" value="http://insta.kalpas.net/auth/code" />
//	</bean>
}
