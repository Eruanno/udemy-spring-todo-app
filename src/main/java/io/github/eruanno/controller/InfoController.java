package io.github.eruanno.controller;

import io.github.eruanno.TaskConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
class InfoController {
    private final DataSourceProperties dataSource;
    private final TaskConfigurationProperties myProp;

    InfoController(final DataSourceProperties dataSource, final TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/url")
    String url() {
        return this.dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProp() {
        return this.myProp.getTemplate().isAllowMultipleTasks();
    }
}
