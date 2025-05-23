drop table if exists t_mups;

create table t_mups(
        mid int primary key auto_increment,
        mname varchar(255),
        martists varchar(255),
        duration int
);

select * from t_mups;