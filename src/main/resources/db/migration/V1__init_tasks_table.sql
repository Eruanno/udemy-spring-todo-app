drop table if exists tasks;
create table TASKS
(
    ID          INTEGER auto_increment primary key,
    DESCRIPTION VARCHAR(255),
    DONE        BOOLEAN not null
);