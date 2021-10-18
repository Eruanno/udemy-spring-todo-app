package io.github.eruanno.model.projection;

import io.github.eruanno.model.Task;
import io.github.eruanno.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline() {
        // Arrange.
        TaskGroup source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task(null, "bar")));

        // Act.
        GroupReadModel result = new GroupReadModel(source);

        // Assert.
        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }
}