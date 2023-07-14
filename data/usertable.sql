CREATE DATABASE IF NOT EXISTS CloudDrive;
USE CloudDrive;
CREATE TABLE IF NOT EXISTS user
(
    id       bigint auto_increment
    PRIMARY KEY,
    username VARCHAR(50) NULL,
    password VARCHAR(50) NULL,
    email    VARCHAR(50) NULL
    );



