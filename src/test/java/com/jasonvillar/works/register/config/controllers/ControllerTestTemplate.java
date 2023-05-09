package com.jasonvillar.works.register.config.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jasonvillar.works.register.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = GlobalExceptionHandler.class)
@WithMockUser
public class ControllerTestTemplate {
    @Autowired
    public MockMvc mockMvc;

    public ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @BeforeEach
    public void setup() {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);;
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
