package io.github.eruanno.logic;

import io.github.eruanno.model.Task;
import io.github.eruanno.model.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TempService {
    @Autowired
    List<String> temp(TaskGroupRepository repository) {
        // FIXME: N + 1 SELECT
        return repository.findAll().stream().flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(Task::getDescription).collect(Collectors.toList());
    }
}
