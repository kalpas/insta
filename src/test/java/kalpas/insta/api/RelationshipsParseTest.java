package kalpas.insta.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import kalpas.insta.api.domain.RelationshipsResponse;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RelationshipsParseTest {

    @Test
    public void test() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src\\test\\resources\\kalpas\\insta\\api\\followers.json");
        FileInputStream fileInputStream = new FileInputStream(file);
        RelationshipsResponse apiResponse = mapper.readValue(new InputStreamReader(fileInputStream, "UTF-8"),
                RelationshipsResponse.class);
    }

}
