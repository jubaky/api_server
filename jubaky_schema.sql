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
    is_disable DATETIME DEFAULT FALSE NOT NULL
);


CREATE TABLE Builds(
    id INT AUTO_INCREMENT PRIMARY KEY,
    branch VARCHAR(128) NOT NULL,
    tag VARCHAR(32) NOT NULL,
    result TEXT,
    status VARCHAR(10) NOT NULL,
    application INT,
    creator INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    start_time DATETIME,
    finish_time DATETIME,

    FOREIGN KEY (application) REFERENCES Applications(id),
    FOREIGN KEY (creator) REFERENCES Users(id)
);

CREATE TABLE Templates(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    kind VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    application INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time DATETIME,

    FOREIGN KEY (application) REFERENCES Applications(id)
);

CREATE TABLE Permissions(
    id INT AUTO_INCREMENT PRIMARY KEY,
    `group` INT,
    application INT,
    permission VARCHAR(3) NOT NULL,

    FOREIGN KEY (`group`) REFERENCES Groups(id),
    FOREIGN KEY (application) REFERENCES Applications(id)
);


CREATE TABLE GroupMembers(
    id INT AUTO_INCREMENT PRIMARY KEY,
    `group` INT,
    user INT,

    FOREIGN KEY (`group`) REFERENCES Groups(id),
    FOREIGN KEY (user) REFERENCES Users(id)
);

CREATE TABLE Deploys(
    id INT AUTO_INCREMENT PRIMARY KEY,
    build INT,
    namespace VARCHAR(30) NOT NULL,
    status VARCHAR(10) NOT NULL,
    templace INT,
    creator INT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    finish_time DATETIME,

    FOREIGN KEY (build) REFERENCES Builds(id),
    FOREIGN KEY (templace) REFERENCES Templates(id),
    FOREIGN KEY (creator) REFERENCES Users(id)
);

CREATE TABLE Credentials(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user INT,
    user_name VARCHAR(30) NOT NULL,
    password VARCHAR(128) NOT NULL,
    `key` VARCHAR(256) NOT NULL,

    FOREIGN KEY (user) REFERENCES Users(id)
);