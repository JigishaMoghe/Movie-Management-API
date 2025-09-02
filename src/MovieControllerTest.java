package com.example.movieapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

  
    @BeforeEach
    void clearData() throws Exception {
        mvc.perform(delete("/movies/test/clear")).andExpect(status().isNoContent());
    }

    @Test
    void listMoviesReturnsEmptyListInitially() throws Exception {
        mvc.perform(get("/movies"))
           .andExpect(status().isOk())
           .andExpect(content().json("[]"));
    }

    @Test
    void fullCrudFlow() throws Exception {
        // create
        String body = """
          {"title":"Inception","director":"Nolan","releaseYear":2010,"genre":"Sci-Fi","rating":9}
        """;
        String id = mvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", not(emptyString())))
                .andReturn().getResponse().getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        // get
        mvc.perform(get("/movies/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"));

        // update
        mvc.perform(put("/movies/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\":8}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(8));

        // list (pagination)
        mvc.perform(get("/movies?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // delete
        mvc.perform(delete("/movies/" + id))
                .andExpect(status().isNoContent());

        mvc.perform(get("/movies/" + id)).andExpect(status().isNotFound());
    }


    @Test
    void validationErrors() throws Exception {
        String bad = """
          {"title":"","rating":11}
        """;
        mvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bad))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pagination() throws Exception {
        
        for (int i = 1; i <= 5; i++) {
            String json = String.format("{\"title\":\"Movie %d\",\"rating\":5}", i);
            mvc.perform(post("/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
               .andExpect(status().isCreated());
        }
       
        String extraJson = "{" +
                "\"title\":\"Inception\"," +
                "\"director\":\"Christopher Nolan\"," +
                "\"rating\":9" +
                "}";
        mvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(extraJson))
           .andExpect(status().isCreated());

        mvc.perform(get("/movies?page=1&size=2"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(2)))
           .andExpect(jsonPath("$[0].title", is("Movie 3")))
           .andExpect(jsonPath("$[1].title", is("Movie 4")));
    }
}