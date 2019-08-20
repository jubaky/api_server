DROP TABLE IF EXISTS Credentials;
DROP TABLE IF EXISTS Deploys;
DROP TABLE IF EXISTS GroupMembers;
DROP TABLE IF EXISTS Permissions;
DROP TABLE IF EXISTS Templates;
DROP TABLE IF EXISTS Builds;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS `Groups`;
DROP TABLE IF EXISTS Applications;

CREATE TABLE Applications(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL,
    repository_addr VARCHAR(256) NOt NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time DATETIME
);


CREATE TABLE `Groups`(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(256) UNIQUE NOT NULL
);


CREATE TABLE Users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    email_id VARCHAR(256) UNIQUE NOT NULL,
    password BINARY(128) NOT NULL,
    name VARCHAR(30) NOT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_login_time DATETIME,
    is_disable BOOLEAN DEFAULT 0 NOT NULL
);


CREATE TABLE Builds(
    id INT AUTO_INCREMENT PRIMARY KEY,
    branch VARCHAR(128) NOT NULL,
    tag VARCHAR(32) NOT NULL,
    result TEXT,
    status VARCHAR(10) NOT NULL,
    application_id INT,
    creator_id INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    start_time DATETIME,
    finish_time DATETIME,

    FOREIGN KEY (application_id) REFERENCES Applications(id),
    FOREIGN KEY (creator_id) REFERENCES Users(id)
);

CREATE TABLE Templates(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    kind VARCHAR(20) NOT NULL,
    yaml TEXT NOT NULL,
    application_id INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time DATETIME,

    FOREIGN KEY (application_id) REFERENCES Applications(id)
);

CREATE TABLE Permissions(
    id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT,
    application_id INT,
    permission VARCHAR(3) NOT NULL,

    FOREIGN KEY (group_id) REFERENCES Groups(id),
    FOREIGN KEY (application_id) REFERENCES Applications(id)
);


CREATE TABLE GroupMembers(
    id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT,
    user_id INT,

    FOREIGN KEY (group_id) REFERENCES Groups(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Deploys(
    id INT AUTO_INCREMENT PRIMARY KEY,
    build_id INT,
    namespace VARCHAR(30) NOT NULL,
    status VARCHAR(10) NOT NULL,
    template_id INT,
    creator_id INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    finish_time DATETIME,

    FOREIGN KEY (build_id) REFERENCES Builds(id),
    FOREIGN KEY (template_id) REFERENCES Templates(id),
    FOREIGN KEY (creator_id) REFERENCES Users(id)
);

CREATE TABLE Credentials(
    id INT AUTO_INCREMENT PRIMARY KEY,
--     user INT,
    user_name VARCHAR(30) NOT NULL,
    password VARCHAR(128) NOT NULL,
    `key` VARCHAR(256) NOT NULL

--     FOREIGN KEY (user) REFERENCES Users(id)
);