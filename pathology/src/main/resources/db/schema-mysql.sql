-- MySQL schema for pathology demo
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    role VARCHAR(50) DEFAULT 'user',
    verified TINYINT(1) DEFAULT 0,
    institution VARCHAR(255),
    doctor_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chat_session (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            user_id BIGINT,
                                            title VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS chat_message (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            session_id BIGINT,
                                            sender VARCHAR(50),
    content LONGTEXT,
    sources VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS knowledge_article (
                                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                 title VARCHAR(255),
    content LONGTEXT,
    summary VARCHAR(500),
    category VARCHAR(100),
    source_url VARCHAR(1000),
    evidence_level VARCHAR(50),
    created_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS image_entity (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            uploader_id BIGINT,
                                            file_path VARCHAR(1000),
    file_name VARCHAR(255),
    metadata VARCHAR(1000),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS image_analysis (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              image_id BIGINT,
                                              model_version VARCHAR(100),
    result LONGTEXT,
    confidence VARCHAR(50),
    reviewer_id BIGINT,
    reviewed_at TIMESTAMP
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;