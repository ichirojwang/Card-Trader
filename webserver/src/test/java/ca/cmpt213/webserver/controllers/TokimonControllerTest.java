package ca.cmpt213.webserver.controllers;

import ca.cmpt213.webserver.models.Tokimon;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokimonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokimonController controller;

    String filename;

    @BeforeEach
    void setUp() {
        filename = "data/tokimontest.json";
        controller.setFilename(filename);
        try {
            new FileWriter(filename, false).close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    void contextLoads() throws Exception {
        assertNotNull(controller);
    }

    @Test
    void addandGetTokimon() throws Exception {
        Tokimon tokimon1 = new Tokimon("AToki1", "One", 10, "picture.jpg", 99);
        Tokimon tokimon2 = new Tokimon("AToki2", "Two", 10, "picture.jpg", 99);

        ObjectMapper mapper = new ObjectMapper();
        String tokimon1Json = mapper.writeValueAsString(tokimon1);
        String tokimon2Json = mapper.writeValueAsString(tokimon2);

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon1Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon2Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(get("/api/tokimon/{tid}", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(get("/api/tokimon/{tid}", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

    }

    @Test
    void getTokimonEmpty() throws Exception {

        this.mockMvc.perform(get("/api/tokimon/{tid}", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    void getAllTokimon() throws Exception {
        Tokimon tokimon1 = new Tokimon("AToki1", "One", 10, "picture.jpg", 99);
        Tokimon tokimon2 = new Tokimon("AToki2", "Two", 10, "picture.jpg", 99);
        Tokimon tokimon3 = new Tokimon("AToki3", "Three", 10, "picture.jpg", 99);

        ObjectMapper mapper = new ObjectMapper();
        String tokimon1Json = mapper.writeValueAsString(tokimon1);
        String tokimon2Json = mapper.writeValueAsString(tokimon2);
        String tokimon3Json = mapper.writeValueAsString(tokimon3);

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon1Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon2Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon3Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":2,\"name\":\"AToki3\",\"type\":\"Three\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(get("/api/tokimon/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}," +
                                                     "{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}," +
                                                     "{\"tid\":2,\"name\":\"AToki3\",\"type\":\"Three\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}]"));

    }

    @Test
    void getAllTokimonEmpty() throws Exception {

        this.mockMvc.perform(get("/api/tokimon/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    @Test
    void editTokimon() throws Exception {
        Tokimon tokimon1 = new Tokimon("AToki1", "One", 10, "picture.jpg", 99);
        Tokimon tokimon2 = new Tokimon("AToki2", "Two", 2, "picture2.jpg", 2);

        ObjectMapper mapper = new ObjectMapper();
        String tokimon1Json = mapper.writeValueAsString(tokimon1);
        String tokimon2Json = mapper.writeValueAsString(tokimon2);

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon1Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform((put("/api/tokimon/edit/{tid}", 0))
                .content(tokimon2Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":2,\"pictureUrl\":\"picture2.jpg\",\"hp\":2}"));

        this.mockMvc.perform(get("/api/tokimon/{tid}", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":2,\"pictureUrl\":\"picture2.jpg\",\"hp\":2}"));

    }
    @Test
    void editTokimonFail() throws Exception {
        Tokimon tokimon1 = new Tokimon("AToki1", "One", 10, "picture.jpg", 99);
        Tokimon tokimon2 = new Tokimon("AToki2", "Two", 2, "picture2.jpg", 2);

        ObjectMapper mapper = new ObjectMapper();
        String tokimon1Json = mapper.writeValueAsString(tokimon1);
        String tokimon2Json = mapper.writeValueAsString(tokimon2);

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon1Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform((put("/api/tokimon/edit/{tid}", 1))
                .content(tokimon2Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        this.mockMvc.perform(get("/api/tokimon/{tid}", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

    }

    @Test
    void deleteTokimon() throws Exception {
        Tokimon tokimon1 = new Tokimon("AToki1", "One", 10, "picture.jpg", 99);
        Tokimon tokimon2 = new Tokimon("AToki2", "Two", 10, "picture.jpg", 99);

        ObjectMapper mapper = new ObjectMapper();
        String tokimon1Json = mapper.writeValueAsString(tokimon1);
        String tokimon2Json = mapper.writeValueAsString(tokimon2);

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon1Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(post("/api/tokimon/add")
                .content(tokimon2Json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(delete("/api/tokimon/{tid}", "0"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(delete("/api/tokimon/{tid}", "1"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().json("{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(get("/api/tokimon/{tid}", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        this.mockMvc.perform(get("/api/tokimon/{tid}", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    void deleteTokimonFail() throws Exception {

        this.mockMvc.perform(delete("/api/tokimon/{tid}", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    void deleteAllTokimon() throws Exception {
        Tokimon tokimon1 = new Tokimon("AToki1", "One", 10, "picture.jpg", 99);
        Tokimon tokimon2 = new Tokimon("AToki2", "Two", 10, "picture.jpg", 99);
        Tokimon tokimon3 = new Tokimon("AToki3", "Three", 10, "picture.jpg", 99);

        ObjectMapper mapper = new ObjectMapper();
        String tokimon1Json = mapper.writeValueAsString(tokimon1);
        String tokimon2Json = mapper.writeValueAsString(tokimon2);
        String tokimon3Json = mapper.writeValueAsString(tokimon3);

        this.mockMvc.perform(post("/api/tokimon/add")
                        .content(tokimon1Json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(post("/api/tokimon/add")
                        .content(tokimon2Json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(post("/api/tokimon/add")
                        .content(tokimon3Json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"tid\":2,\"name\":\"AToki3\",\"type\":\"Three\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}"));

        this.mockMvc.perform(delete("/api/tokimon/all"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().json("[{\"tid\":0,\"name\":\"AToki1\",\"type\":\"One\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}," +
                        "{\"tid\":1,\"name\":\"AToki2\",\"type\":\"Two\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}," +
                        "{\"tid\":2,\"name\":\"AToki3\",\"type\":\"Three\",\"rarity\":10,\"pictureUrl\":\"picture.jpg\",\"hp\":99}]"));

        this.mockMvc.perform(get("/api/tokimon/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void deleteAllTokimonEmpty() throws Exception {

        this.mockMvc.perform(delete("/api/tokimon/all"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("[]"));


    }










}