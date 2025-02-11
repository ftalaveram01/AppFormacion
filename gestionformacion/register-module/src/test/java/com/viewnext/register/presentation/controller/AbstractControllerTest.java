package com.viewnext.register.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    private RegistroController registroController;

    @Before(value = "")
    public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(registroController).build();
}

    protected void testResponseBody(MvcResult result, Object object) throws Exception{

        String responseBody= result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        String familiasAsJSON = mapper.writeValueAsString(object);

        assertEquals(responseBody, familiasAsJSON);
    }

}
