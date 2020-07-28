package com.doherty.dogs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DogController.class)
class DogControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private DogRepository dogRepository;

    private Dog chuck;

    @BeforeEach
    void setUp() {
        chuck = Dog.builder().name("Chuck").breed("cattledog").color("copper").build();
    }

    @Test
    public void healthCheck() throws Exception {
        mockMvc.perform(get("/dog/health"))
                .andExpect(content().string(containsString("App works!")));
    }

    @Test
    void addDogMethod_WithValidDog_SavesTheDog() throws Exception {
        when(dogRepository.save(any(Dog.class))).thenReturn(chuck);
        MvcResult mvcResult = mockMvc.perform(post("/dog/add?name=Chuck&breed=cattledog&color=copper"))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assert(content).equals("Saved");
    }

    @Test
    void getDogs_ReturnsDogs() throws Exception {
        Dog rosco = Dog.builder().name("Rosco").breed("mutt").color("brown").build();
        List<Dog> dogList = new ArrayList<>();
        dogList.add(chuck);
        dogList.add(rosco);
        when(dogRepository.findAll()).thenReturn(dogList);
        MvcResult mvcResult = mockMvc.perform(get("/dog/all"))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Dog> returnedList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Dog>>(){});
        assert(returnedList.size() == 2);
        assert(returnedList.contains(chuck));
        assert(returnedList.contains(rosco));
    }
}