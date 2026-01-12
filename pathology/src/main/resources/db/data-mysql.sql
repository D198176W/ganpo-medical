-- seed demo users (bcrypt hash for '123456', example)
INSERT INTO users(username, password, role, verified, doctor_name)
VALUES('doctor1', '$2a$10$2bJmQfY6k8lD1u8K6E1e5OZ9YxF4G6/HwqkzvE5bq8oFzqsb3dG1K', 'doctor', 1, '张医生');

INSERT INTO users(username, password, role, verified)
VALUES('user1', '$2a$10$2bJmQfY6k8lD1u8K6E1e5OZ9YxF4G6/HwqkzvE5bq8oFzqsb3dG1K', 'user', 0);

INSERT INTO knowledge_article(title, content, summary, category, source_url, evidence_level, created_by)
VALUES('鳞状细胞癌组织学特征','<p>鳞状细胞癌特征：角化珠、细胞间桥、异型性明显</p>','鳞状细胞癌的组织学特征','tumor','https://who.int','B',1);