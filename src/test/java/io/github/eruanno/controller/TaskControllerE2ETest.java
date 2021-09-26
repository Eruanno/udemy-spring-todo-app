package io.github.eruanno.controller;

import io.github.eruanno.model.Task;
import io.github.eruanno.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    TaskRepository taskRepository;

    @Test
    void httpGet_returnAllTasks() {
        // Arrange.
        int initial = taskRepository.findAll().size();
        taskRepository.save(new Task(LocalDateTime.now(), "foo"));
        taskRepository.save(new Task(LocalDateTime.now(), "bar"));

        // Act.
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        // Assert.
        assertThat(result).hasSize(initial + 2);
    }
}
