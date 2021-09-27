package io.github.eruanno.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.*;

public class TestTaskRepository implements TaskRepository {
    private final Map<Integer, Task> tasks = new HashMap<>();

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Page<Task> findAll(final Pageable page) {
        return null;
    }

    @Override
    public Optional<Task> findById(final Integer id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public boolean existsById(final Integer id) {
        return tasks.containsKey(id);
    }

    @Override
    public List<Task> findByDone(final boolean done) {
        return null;
    }

    @Override
    public boolean existsByDoneIsFalseAndGroup_Id(final Integer groupId) {
        return false;
    }

    @Override
    public Task save(final Task entity) {
        final int key = tasks.size() + 1;
        try {
            Field field = Task.class.getSuperclass().getSuperclass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, key);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        tasks.put(key, entity);
        return tasks.get(key);
    }

    @Override
    public List<Task> findAllByGroup_Id(final Integer groupId) {
        return List.of();
    }
}
