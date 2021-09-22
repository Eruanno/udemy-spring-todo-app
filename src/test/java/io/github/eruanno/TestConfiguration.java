package io.github.eruanno;

import io.github.eruanno.model.TaskRepository;
import io.github.eruanno.model.TestTaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
class TestConfiguration {
    @Bean
    @Profile("integration")
    TaskRepository testRepo() {
        return new TestTaskRepository();
    }
}
