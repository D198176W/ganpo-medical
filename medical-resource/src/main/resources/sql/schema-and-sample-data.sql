-- 数据表 DDL（简化版）
CREATE DATABASE IF NOT EXISTS medical_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medical_db;

-- departments
DROP TABLE IF EXISTS departments;
CREATE TABLE departments (
                             id VARCHAR(64) PRIMARY KEY,
                             name VARCHAR(128) NOT NULL,
                             parent_id VARCHAR(64),
                             alias JSON,
                             description TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- hospitals
DROP TABLE IF EXISTS hospitals;
CREATE TABLE hospitals (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(255) NOT NULL,
                           level VARCHAR(64),
                           rating DECIMAL(3,1),
                           address VARCHAR(512),
                           lat DECIMAL(10,7),
                           lng DECIMAL(10,7),
                           phone VARCHAR(64),
                           core_departments JSON,
                           has_open_slots TINYINT(1) DEFAULT 1,
                           description TEXT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- pharmacies
DROP TABLE IF EXISTS pharmacies;
CREATE TABLE pharmacies (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            address VARCHAR(512),
                            lat DECIMAL(10,7),
                            lng DECIMAL(10,7),
                            phone VARCHAR(64),
                            is_designated TINYINT(1) DEFAULT 0,
                            opening_hours VARCHAR(255),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- users (简化，不存敏感信息示例)
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(64) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(128),
                       phone VARCHAR(64),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- appointments
DROP TABLE IF EXISTS appointments;
CREATE TABLE appointments (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT NOT NULL,
                              hospital_id BIGINT NOT NULL,
                              department_id VARCHAR(64),
                              doctor_id VARCHAR(64),
                              date DATE,
                              timeslot VARCHAR(64),
                              status VARCHAR(32) DEFAULT 'pending',
                              ticket_number VARCHAR(64),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                              FOREIGN KEY (hospital_id) REFERENCES hospitals(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- reimbursement rules
DROP TABLE IF EXISTS reimbursement_rules;
CREATE TABLE reimbursement_rules (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     crowd_type VARCHAR(64) NOT NULL, -- e.g., student
                                     outpatient_ratio INT,
                                     inpatient_ratio INT,
                                     exam_ratio INT,
                                     drug_ratio INT,
                                     max_per_year DECIMAL(12,2),
                                     effective_from DATE,
                                     effective_to DATE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- sample data
INSERT INTO departments (id,name) VALUES ('all','全部科室'),('gastroenterology','消化内科'),('respiratory','呼吸科'),('cardiology','心内科'),('neurology','神经内科'),('orthopedics','骨科'),('dermatology','皮肤科'),('ophthalmology','眼科'),('pediatrics','儿科');

INSERT INTO hospitals (name,level,rating,address,lat,lng,phone,core_departments,has_open_slots,description) VALUES
                                                                                                                ('南昌大学第一附属医院','三级甲等',4.8,'南昌市东湖区永外正街17号',28.6810,115.8730,'0791-123456','[\"消化内科\",\"呼吸科\",\"心内科\"]',1,'综合医院'),
                                                                                                                ('江西省人民医院','三级甲等',4.6,'南昌市西湖区爱国路92号',28.6825,115.8612,'0791-234567','[\"神经内科\",\"骨科\",\"眼科\"]',1,'省级医院'),
                                                                                                                ('江西中医药大学附属医院','三级甲等',4.7,'南昌市东湖区八一大道445号',28.6890,115.8583,'0791-345678','[\"消化内科\",\"皮肤科\",\"儿科\"]',0,'中医院');

INSERT INTO pharmacies (name,address,lat,lng,phone,is_designated) VALUES
                                                                      ('昌盛大药房(八一广场店)','南昌市西湖区八一大道358号',28.683569,115.858225,'0791-8628XXXX',1),
                                                                      ('开心人大药房(象山北路店)','南昌市东湖区象山北路236号',28.685569,115.861225,'0791-8673XXXX',1);

INSERT INTO users (username,password,name,phone) VALUES ('student1','$2a$10$xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx','学生A','13900000000');

INSERT INTO reimbursement_rules (crowd_type,outpatient_ratio,inpatient_ratio,exam_ratio,drug_ratio,max_per_year,effective_from,effective_to) VALUES
    ('student',50,70,60,65,100000,'2020-01-01','2099-12-31');