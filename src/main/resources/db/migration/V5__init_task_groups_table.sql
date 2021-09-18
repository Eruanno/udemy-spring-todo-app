create table task_groups
(
    ID          INTEGER auto_increment primary key,
    DESCRIPTION VARCHAR(255),
    DONE        BOOLEAN not null
);
alter table tasks add column task_group_id int null;
alter table tasks add foreign key (task_group_id) references task_groups (id);