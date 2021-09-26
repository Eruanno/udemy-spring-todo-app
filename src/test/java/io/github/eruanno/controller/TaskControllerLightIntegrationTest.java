package io.github.eruanno.controller;

import io.github.eruanno.model.Task;
import io.github.eruanno.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(TaskController.class)
class TaskControllerLightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnGivenTask() throws Exception {
        // Arrange.
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(new Task(LocalDateTime.now(), "foo")));

        // Act and Assert.
        mockMvc.perform(get("/tasks/" + 1)).andDo(print())
                .andExpect(content().string(containsString("\"description\":\"foo\"")));
    }
}
