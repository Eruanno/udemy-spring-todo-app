create table projects
(
    ID          INTEGER auto_increment primary key,
    DESCRIPTION VARCHAR(255)
);
alter table task_groups add column PROJECT_ID int null;
alter table task_groups add foreign key (project_id) references projects (ID);
create table project_steps
(
    ID               INTEGER auto_increment primary key,
    DESCRIPTION      VARCHAR(255),
    PROJECT_ID       INTEGER,
    DAYS_TO_DEADLINE INTEGER
);
alter table project_steps add foreign key (PROJECT_ID) references projects (ID);