package io.github.eruanno.model.projection;

import io.github.eruanno.model.Project;
import io.github.eruanno.model.ProjectStep;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProjectWriteModel {
    @NotBlank(message = "Resource description must not be empty")
    private String description;
    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public ProjectWriteModel() {
        steps.add(new ProjectStep());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<ProjectStep> getSteps() {
        return steps;
    }

    public void setSteps(final List<ProjectStep> steps) {
        this.steps = steps;
    }

    public Project toProject() {
        Project project = new Project();
        project.setDescription(description);
        steps.forEach(step -> step.setProject(project));
        project.setSteps(new HashSet<>(steps));
        return project;
    }
}
