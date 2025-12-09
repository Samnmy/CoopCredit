-- V2__insert_initial_data.sql
-- Insertar datos iniciales

-- Primero insertar afiliados (sin dependencias)
INSERT INTO affiliate (document, name, salary, affiliation_date, status) VALUES
('123456789', 'John Doe', 3000.00, '2023-01-15', 'ACTIVE'),
('987654321', 'Jane Smith', 4500.00, '2023-03-20', 'ACTIVE'),
('456789123', 'Bob Johnson', 2500.00, '2023-06-10', 'ACTIVE'),
('789123456', 'Alice Brown', 3500.00, '2024-01-05', 'ACTIVE')
ON CONFLICT (document) DO NOTHING;

-- Insertar usuarios
-- Contraseña para todos: password123 (encriptada con BCrypt)
INSERT INTO users (username, password, email, affiliate_id, enabled) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJsUi', 'admin@coopcredit.com', NULL, true),
('johndoe', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJsUi', 'john.doe@email.com', 1, true),
('janesmith', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJsUi', 'jane.smith@email.com', 2, true),
('bobjohnson', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJsUi', 'bob.johnson@email.com', 3, true),
('alicebrown', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJsUi', 'alice.brown@email.com', 4, true),
('analyst', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVJsUi', 'analyst@coopcredit.com', NULL, true)
ON CONFLICT (username) DO NOTHING;

-- Asignar roles a usuarios
INSERT INTO user_roles (user_id, role) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_ANALYST'),
(2, 'ROLE_AFFILIATE'),
(3, 'ROLE_AFFILIATE'),
(4, 'ROLE_AFFILIATE'),
(5, 'ROLE_AFFILIATE'),
(6, 'ROLE_ANALYST')
ON CONFLICT (user_id, role) DO NOTHING;