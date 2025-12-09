-- V1__create_schema.sql
-- Flyway requiere que el archivo comience con comentarios

-- Tabla de afiliados
CREATE TABLE IF NOT EXISTS affiliate (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    salary DECIMAL(15,2) NOT NULL,
    affiliation_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT check_salary_positive CHECK (salary > 0),
    CONSTRAINT check_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

-- Tabla de solicitudes de crédito
CREATE TABLE IF NOT EXISTS credit_application (
    id BIGSERIAL PRIMARY KEY,
    affiliate_id BIGINT NOT NULL,
    requested_amount DECIMAL(15,2) NOT NULL,
    term_months INTEGER NOT NULL,
    proposed_rate DECIMAL(5,2) NOT NULL,
    application_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_affiliate FOREIGN KEY (affiliate_id) REFERENCES affiliate(id),
    CONSTRAINT check_amount_positive CHECK (requested_amount > 0),
    CONSTRAINT check_term_positive CHECK (term_months > 0),
    CONSTRAINT check_rate_non_negative CHECK (proposed_rate >= 0),
    CONSTRAINT check_app_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED'))
);

-- Tabla de evaluación de riesgo
CREATE TABLE IF NOT EXISTS risk_evaluation (
    id BIGSERIAL PRIMARY KEY,
    credit_application_id BIGINT NOT NULL UNIQUE,
    score INTEGER NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    details TEXT,
    evaluated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credit_application FOREIGN KEY (credit_application_id) REFERENCES credit_application(id),
    CONSTRAINT check_score_range CHECK (score >= 300 AND score <= 950),
    CONSTRAINT check_risk_level CHECK (risk_level IN ('HIGH', 'MEDIUM', 'LOW'))
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    affiliate_id BIGINT,
    enabled BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_affiliate_user FOREIGN KEY (affiliate_id) REFERENCES affiliate(id)
);

-- Tabla de roles de usuario
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT check_role CHECK (role IN ('ROLE_AFFILIATE', 'ROLE_ANALYST', 'ROLE_ADMIN'))
);