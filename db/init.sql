CREATE DATABASE KnowTify;
USE KnowTify;

CREATE TABLE users_personal_info (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    surname VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE users_login (
    login_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users_personal_info(user_id) ON DELETE CASCADE
);

CREATE TABLE study_struggles (
    struggle_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    struggle_area VARCHAR(255) NOT NULL,
    suggested_method VARCHAR(255),
    weekly_goal INT,
    FOREIGN KEY (user_id) REFERENCES users_personal_info(user_id) ON DELETE CASCADE
);

CREATE TABLE subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    academic_year VARCHAR(9) NOT NULL,
    semester VARCHAR(50) NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    professor_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users_personal_info(user_id) ON DELETE CASCADE
);

CREATE TABLE schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    day_of_week TINYINT NOT NULL, 
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    building_room VARCHAR(100),
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

CREATE TABLE tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
	subject_id INT NOT NULL,
    task_title VARCHAR(255) NOT NULL,
    task_description TEXT,
    due_date DATE NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'in_progress', 'done') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES users_personal_info(user_id) ON DELETE CASCADE,
	FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

CREATE TABLE task_analysis (
    analysis_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    task_id INT NOT NULL,
    total_tasks INT NOT NULL,  
    completed_tasks INT NOT NULL, 
    time_until_due INT NOT NULL, 
    approx_time_to_finish DECIMAL(5, 2) NOT NULL,  
    recommended_daily_prep_time DECIMAL(5, 2) NOT NULL, 
    analysis_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users_personal_info(user_id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);