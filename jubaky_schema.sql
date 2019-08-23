DROP TABLE IF EXISTS Credentials;
DROP TABLE IF EXISTS Deploys;
DROP TABLE IF EXISTS GroupMembers;
DROP TABLE IF EXISTS Permissions;
DROP TABLE IF EXISTS Templates;
DROP TABLE IF EXISTS Builds;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS `Groups`;
DROP TABLE IF EXISTS Jobs;
DROP TABLE IF EXISTS Applications;

CREATE TABLE Applications(
    id INT AUTO_INCREMENT,
    name VARCHAR(32) UNIQUE NOT NULL,
    repository_addr VARCHAR(256) NOt NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time DATETIME,

    PRIMARY KEY (id)
);


CREATE TABLE `Groups`(
    id INT AUTO_INCREMENT,
    name VARCHAR(256) UNIQUE NOT NULL,

    PRIMARY KEY (id)
);


CREATE TABLE Users(
    id INT AUTO_INCREMENT,
    email_id VARCHAR(256) UNIQUE NOT NULL,
    password VARCHAR(128) NOT NULL,
    name VARCHAR(30) NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_login_time DATETIME,
    is_disable BOOLEAN DEFAULT 0 NOT NULL,

    PRIMARY KEY (id)
);


CREATE TABLE Jobs(
     id INT AUTO_INCREMENT,
     branch VARCHAR(128) NOT NULL,
     application_id INT,
     last_build_number INT DEFAULT 0,
     tag VARCHAR(128) DEFAULT 'lts',

     FOREIGN KEY (application_id) REFERENCES Applications(id),
     PRIMARY KEY (id)
);


CREATE TABLE Builds(
    id INT AUTO_INCREMENT,
    branch VARCHAR(128) NOT NULL,
    job_id INT,
    tag VARCHAR(32) NOT NULL,
    result TEXT,
    status VARCHAR(10) NOT NULL,
    application_id INT,
    creator_id INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    start_time DATETIME,
    finish_time DATETIME,

    FOREIGN KEY (application_id) REFERENCES Applications(id),
    FOREIGN KEY (creator_id) REFERENCES Users(id),
    FOREIGN KEY (job_id) REFERENCES Jobs(id),
    PRIMARY KEY (id)
);

CREATE TABLE Templates(
    id INT AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    kind VARCHAR(20) NOT NULL,
    yaml TEXT NOT NULL,
    application_id INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time DATETIME,

    FOREIGN KEY (application_id) REFERENCES Applications(id),
    PRIMARY KEY (id)
);

CREATE TABLE Permissions(
    id INT AUTO_INCREMENT,
    group_id INT,
    application_id INT,
    permission VARCHAR(3) NOT NULL,

    FOREIGN KEY (group_id) REFERENCES Groups(id),
    FOREIGN KEY (application_id) REFERENCES Applications(id),
    PRIMARY KEY (id)
);


CREATE TABLE GroupMembers(
    id INT AUTO_INCREMENT,
    group_id INT,
    user_id INT,

    FOREIGN KEY (group_id) REFERENCES Groups(id),
    FOREIGN KEY (user_id) REFERENCES Users(id),
    PRIMARY KEY (id)
);

CREATE TABLE Deploys(
    id INT AUTO_INCREMENT,
    build_id INT,
    namespace VARCHAR(30) NOT NULL,
    status VARCHAR(10) NOT NULL,
    application_id INT,
    template_id INT,
    creator_id INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    finish_time DATETIME,

    FOREIGN KEY (application_id) REFERENCES Applications(id),
    FOREIGN KEY (build_id) REFERENCES Builds(id),
    FOREIGN KEY (template_id) REFERENCES Templates(id),
    FOREIGN KEY (creator_id) REFERENCES Users(id),
    PRIMARY KEY (id)
);

CREATE TABLE Credentials(
    id INT AUTO_INCREMENT,
    user_id INT,
    user_name VARCHAR(30) NOT NULL,
    password VARCHAR(128) NOT NULL,
    `key` VARCHAR(256) NOT NULL,

    FOREIGN KEY (user_id) REFERENCES Users(id),
    PRIMARY KEY (id)
);