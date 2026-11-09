CREATE DATABASE IF NOT EXISTS cyber_incident_db;
USE cyber_incident_db;

CREATE TABLE IF NOT EXISTS analysts (
    analyst_id INT PRIMARY KEY AUTO_INCREMENT,
    analyst_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    specialization VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS incidents (
    incident_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    severity ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL,
    status ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED') NOT NULL DEFAULT 'OPEN',
    reported_by VARCHAR(100) NOT NULL,
    analyst_id INT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_incident_analyst
        FOREIGN KEY (analyst_id) REFERENCES analysts(analyst_id)
        ON DELETE SET NULL
);

INSERT IGNORE INTO analysts (analyst_id, analyst_name, email, specialization) VALUES
(1, 'Asha Nair', 'asha@example.com', 'Network Security'),
(2, 'Rahul Menon', 'rahul@example.com', 'Malware Analysis'),
(3, 'Meera Das', 'meera@example.com', 'Incident Response');

INSERT INTO incidents (title, description, severity, status, reported_by, analyst_id)
SELECT 'Suspicious login attempts',
       'Several failed login attempts were detected on a staff account.',
       'MEDIUM', 'OPEN', 'IT Helpdesk', 1
WHERE NOT EXISTS (
    SELECT 1 FROM incidents WHERE title = 'Suspicious login attempts'
);

INSERT INTO incidents (title, description, severity, status, reported_by, analyst_id)
SELECT 'Unusual endpoint activity',
       'Endpoint monitoring reported an application behaving unexpectedly.',
       'HIGH', 'IN_PROGRESS', 'Security Monitor', 2
WHERE NOT EXISTS (
    SELECT 1 FROM incidents WHERE title = 'Unusual endpoint activity'
);

SELECT 'Database ready. Sample analysts and incidents inserted if missing.' AS SetupMessage;
