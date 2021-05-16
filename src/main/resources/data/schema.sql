CREATE TABLE IF NOT EXISTS hibernate_sequence (next_val numeric);

CREATE TABLE IF NOT EXISTS weather (
    id integer not null,
    city varchar(255) not null,
    country varchar(255) not null,
    date_time integer not null,
    temperature double precision not null,
    primary key (id)
);
