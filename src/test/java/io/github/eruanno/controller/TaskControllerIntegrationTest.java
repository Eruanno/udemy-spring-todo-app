package io.github.eruanno.controller;

import io.github.eruanno.model.Task;
import io.github.eruanno.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnGivenTask() throws Exception {
        // Arrange.
        int id = taskRepository.save(new Task(LocalDateTime.now(), "foo")).getId();

        // Act and Assert.
        mockMvc.perform(get("/tasks/" + id)).andExpect(status().is2xxSuccessful());
    }
}
