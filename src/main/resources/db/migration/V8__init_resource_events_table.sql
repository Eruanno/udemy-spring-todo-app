drop table if exists resource_events;
create table resource_events
(
    id          int primary key auto_increment,
    resource_id int,
    classname   varchar(255),
    occurrence  datetime,
    name        varchar(255)
);
