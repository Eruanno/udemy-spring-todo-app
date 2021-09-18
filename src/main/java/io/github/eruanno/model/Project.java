package io.github.eruanno.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends Resource {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<ProjectStep> steps;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;

    public Project() {
    }

    public Set<ProjectStep> getSteps() {
        return steps;
    }

    void setSteps(final Set<ProjectStep> steps) {
        this.steps = steps;
    }

    Set<TaskGroup> getGroups() {
        return groups;
    }

    void setGroups(final Set<TaskGroup> taskGroups) {
        this.groups = taskGroups;
    }
}
