CREATE TABLE user_roles (
    user_id varchar(32) NOT NULL,
    role_id BIGINT NOT NULL
);

CREATE TABLE users (
    login varchar(32) PRIMARY KEY,
    name varchar(32) NOT NULL,
    password varchar(32) NOT NULL
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    name varchar(32) NOT NULL
);