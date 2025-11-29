CREATE DATABASE IF NOT EXISTS HEAR_db;
USE HEAR_db;

CREATE TABLE USER (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      Has_admin_permissions BOOLEAN DEFAULT FALSE,
                      Hashed_password VARCHAR(255) NOT NULL,
                      Name VARCHAR(100) NOT NULL,
                      Phone VARCHAR(20) NOT NULL UNIQUE,
                      Email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE FACULTY_MEMBER (
                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                User_id INT NOT NULL UNIQUE,
                                Job_title VARCHAR(150),
                                Department ENUM('CS','IS','IT','AI','CYSEC'),
                                Scientific_degree VARCHAR(150),
                                Bio TEXT,
                                FOREIGN KEY (User_id) REFERENCES USER(ID) ON DELETE CASCADE
);

CREATE TABLE STUDENT (
                         ID INT AUTO_INCREMENT PRIMARY KEY,
                         User_id INT NOT NULL UNIQUE,
                         Student_code INT UNIQUE,
                         Current_level TINYINT,
                         Department ENUM('GENERAL','CS','IS','IT','AI','CYSEC'),
                         FOREIGN KEY (User_id) REFERENCES USER(ID) ON DELETE CASCADE
);

CREATE TABLE COURSE (
                        ID INT AUTO_INCREMENT PRIMARY KEY,
                        Name VARCHAR(200) NOT NULL UNIQUE,
                        Course_code VARCHAR(50) NOT NULL UNIQUE,
                        Description TEXT,
                        Credit_hours TINYINT NOT NULL
);

CREATE TABLE CLASS (
                       ID INT AUTO_INCREMENT PRIMARY KEY,
                       Course_id INT NOT NULL,
                       Faculty_member_id INT,
                       Start_time TIME,
                       End_time TIME,
                       Day ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY'),
                       Room VARCHAR(50),
                       Type ENUM('LEC','SEC1','SEC2','SEC3','SEC4','SEC5','SEC6','SEC7','SEC8','SEC9','SEC10') NOT NULL,
                       Name VARCHAR(50) NOT NULL,
                       FOREIGN KEY (Course_id) REFERENCES COURSE(ID) ON DELETE CASCADE,
                       FOREIGN KEY (Faculty_member_id) REFERENCES FACULTY_MEMBER(ID) ON DELETE SET NULL
);

CREATE TABLE MATERIALS (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           Course_id INT,
                           Added_by INT,
                           Approved_by INT,
                           Title VARCHAR(255) NOT NULL,
                           Type VARCHAR(10),
                           Category VARCHAR(255),
                           Link TEXT,
                           Status ENUM('WAITING','APPROVED','REJECTED') DEFAULT 'WAITING',
                           Time_added DATETIME DEFAULT CURRENT_TIMESTAMP,
                           Time_approved DATETIME,
                           FOREIGN KEY (Course_id) REFERENCES COURSE(ID) ON DELETE SET NULL,
                           FOREIGN KEY (Added_by) REFERENCES USER(ID) ON DELETE SET NULL,
                           FOREIGN KEY (Approved_by) REFERENCES USER(ID) ON DELETE SET NULL
);

CREATE TABLE QUESTION (
                          ID INT AUTO_INCREMENT PRIMARY KEY,
                          Course_id INT,
                          Added_by INT,
                          Approved_by INT,
                          Question_text TEXT NOT NULL,
                          Type ENUM('MCQ','TF','ESSAY') NOT NULL,
                          Correct_answer TEXT NOT NULL,
                          Status ENUM('WAITING','APPROVED','REJECTED') DEFAULT 'WAITING',
                          Time_added DATETIME DEFAULT CURRENT_TIMESTAMP,
                          Time_approved DATETIME,
                          FOREIGN KEY (Course_id) REFERENCES COURSE(ID) ON DELETE SET NULL,
                          FOREIGN KEY (Added_by) REFERENCES USER(ID) ON DELETE SET NULL,
                          FOREIGN KEY (Approved_by) REFERENCES USER(ID) ON DELETE SET NULL
);

CREATE TABLE CHOICES (
                         ID INT AUTO_INCREMENT PRIMARY KEY,
                         Question_id INT NOT NULL,
                         Choice TEXT NOT NULL,
                         FOREIGN KEY (Question_id) REFERENCES QUESTION(ID) ON DELETE CASCADE
);

CREATE TABLE NOTIFICATION (
                              ID INT AUTO_INCREMENT PRIMARY KEY,
                              Creator_id INT,
                              Title VARCHAR(255) NOT NULL,
                              Message TEXT,
                              Created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (Creator_id) REFERENCES USER(ID) ON DELETE SET NULL
);

CREATE TABLE User_Has_Notification (
                                       User_ID INT NOT NULL,
                                       Notification_id INT NOT NULL,
                                       PRIMARY KEY (User_ID, Notification_id),
                                       FOREIGN KEY (User_ID) REFERENCES USER(ID) ON DELETE CASCADE,
                                       FOREIGN KEY (Notification_id) REFERENCES NOTIFICATION(ID) ON DELETE CASCADE
);

CREATE TABLE Question_Favoured_By_User (
                                           User_id INT NOT NULL,
                                           Question_id INT NOT NULL,
                                           PRIMARY KEY (User_id, Question_id),
                                           FOREIGN KEY (User_id) REFERENCES USER(ID) ON DELETE CASCADE,
                                           FOREIGN KEY (Question_id) REFERENCES QUESTION(ID) ON DELETE CASCADE
);

CREATE TABLE Material_Favoured_By_User (
                                           User_id INT NOT NULL,
                                           Material_id INT NOT NULL,
                                           PRIMARY KEY (User_id, Material_id),
                                           FOREIGN KEY (User_id) REFERENCES USER(ID) ON DELETE CASCADE,
                                           FOREIGN KEY (Material_id) REFERENCES MATERIALS(ID) ON DELETE CASCADE
);

CREATE TABLE Question_Solved_By_Student (
                                            Student_id INT NOT NULL,
                                            Question_id INT NOT NULL,
                                            PRIMARY KEY (Student_id, Question_id),
                                            FOREIGN KEY (Student_id) REFERENCES STUDENT(ID) ON DELETE CASCADE,
                                            FOREIGN KEY (Question_id) REFERENCES QUESTION(ID) ON DELETE CASCADE
);

CREATE TABLE Student_Enrolled_In_Course (
                                            Student_id INT NOT NULL,
                                            Course_id INT NOT NULL,
                                            PRIMARY KEY (Student_id, Course_id),
                                            FOREIGN KEY (Student_id) REFERENCES STUDENT(ID) ON DELETE CASCADE,
                                            FOREIGN KEY (Course_id) REFERENCES COURSE(ID) ON DELETE CASCADE
);

CREATE TABLE Student_Takes_Class (
                                     Student_id INT NOT NULL,
                                     Class_id INT NOT NULL,
                                     PRIMARY KEY (Student_id, Class_id),
                                     FOREIGN KEY (Student_id) REFERENCES STUDENT(ID) ON DELETE CASCADE,
                                     FOREIGN KEY (Class_id) REFERENCES CLASS(ID) ON DELETE CASCADE
);