#!/bin/bash

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variables
PROJECT_DIR=$(pwd)
LOG_FILE="/tmp/coopcredit_verification.log"
TEST_RESULTS="/tmp/coopcredit_test_results.txt"
POSTGRES_CONTAINER="coopcredit-postgres"
APP_CONTAINER="credit-application-service"
RISK_CONTAINER="risk-central-mock-service"
TOKEN_FILE="/tmp/jwt_token.txt"

# Inicializar archivos
> $LOG_FILE
> $TEST_RESULTS

# Función para loguear
log() {
    echo -e "$1" | tee -a $LOG_FILE
}

# Función para verificar y registrar resultado
check() {
    local test_name="$1"
    local command="$2"
    local expected="$3"

    log "${BLUE}➡️  Verificando: $test_name${NC}"

    if eval "$command" >> $LOG_FILE 2>&1; then
        log "${GREEN}   ✅ $test_name - OK${NC}"
        echo "✅ $test_name" >> $TEST_RESULTS
        return 0
    else
        log "${RED}   ❌ $test_name - FALLÓ${NC}"
        echo "❌ $test_name" >> $TEST_RESULTS
        return 1
    fi
}

# Función para verificar contenido
check_content() {
    local test_name="$1"
    local command="$2"
    local expected_content="$3"

    log "${BLUE}➡️  Verificando: $test_name${NC}"

    local output
    output=$(eval "$command" 2>&1)

    if echo "$output" | grep -q "$expected_content"; then
        log "${GREEN}   ✅ $test_name - OK${NC}"
        echo "✅ $test_name" >> $TEST_RESULTS
        return 0
    else
        log "${RED}   ❌ $test_name - FALLÓ${NC}"
        log "   Esperaba: $expected_content"
        log "   Obtuve: $output"
        echo "❌ $test_name" >> $TEST_RESULTS
        return 1
    fi
}

# Banner
clear
log "${BLUE}========================================${NC}"
log "${GREEN}   VERIFICACIÓN COMPLETA PROYECTO M6   ${NC}"
log "${BLUE}   CoopCredit - Credit Application     ${NC}"
log "${BLUE}========================================${NC}\n"

# ==================== 1. VERIFICACIÓN INICIAL ====================
log "${YELLOW}=== 1. VERIFICACIÓN INICIAL DEL PROYECTO ===${NC}\n"

# 1.1 Verificar estructura del proyecto
check "Estructura del proyecto" "find . -type f -name 'pom.xml' -o -name 'Dockerfile' -o -name 'docker-compose.yml' | grep -E 'pom.xml|Dockerfile|docker-compose.yml' | wc -l | grep -q '3'"

# 1.2 Verificar Spring Boot 3.5.8
check_content "Spring Boot 3.5.8" "grep -A2 'spring-boot-starter-parent' pom.xml" "3.5.8"

# 1.3 Verificar Java 17
check_content "Java 17" "grep 'java.version' pom.xml" "17"

# 1.4 Verificar que es proyecto Maven
check "Proyecto Maven" "test -f pom.xml"

# ==================== 2. ARQUITECTURA HEXAGONAL ====================
log "\n${YELLOW}=== 2. ARQUITECTURA HEXAGONAL ===${NC}\n"

# 2.1 Verificar estructura de carpetas hexagonal
check "Carpeta domain/" "test -d src/main/java/com/coopcredit/creditapplicationservice/domain"
check "Carpeta application/" "test -d src/main/java/com/coopcredit/creditapplicationservice/application"
check "Carpeta infrastructure/" "test -d src/main/java/com/coopcredit/creditapplicationservice/infrastructure"

# 2.2 Verificar puertos (interfaces)
check_content "Puerto AffiliatePort" "find src/main/java -name 'AffiliatePort.java' -type f" "AffiliatePort.java"
check_content "Puerto CreditApplicationPort" "find src/main/java -name 'CreditApplicationPort.java' -type f" "CreditApplicationPort.java"
check_content "Puerto RiskCentralPort" "find src/main/java -name 'RiskCentralPort.java' -type f" "RiskCentralPort.java"

# 2.3 Verificar adaptadores
check_content "Adaptador AffiliateAdapter" "find src/main/java -name 'AffiliateAdapter.java' -type f" "AffiliateAdapter.java"
check_content "Adaptador CreditApplicationAdapter" "find src/main/java -name 'CreditApplicationAdapter.java' -type f" "CreditApplicationAdapter.java"

# 2.4 Verificar MapStruct
check_content "MapStruct configurado" "grep 'mapstruct' pom.xml" "mapstruct"
check_content "Mapper AffiliateMapper" "find src/main/java -name 'AffiliateMapper.java' -type f" "AffiliateMapper.java"

# 2.5 Verificar casos de uso
check_content "Caso de uso ApplyForCreditUseCase" "find src/main/java -name 'ApplyForCreditUseCase.java' -type f" "ApplyForCreditUseCase.java"
check_content "Caso de uso RegisterAffiliateUseCase" "find src/main/java -name 'RegisterAffiliateUseCase.java' -type f" "RegisterAffiliateUseCase.java"

# ==================== 3. BASE DE DATOS Y FLYWAY ====================
log "\n${YELLOW}=== 3. BASE DE DATOS Y FLYWAY ===${NC}\n"

# 3.1 Verificar dependencias PostgreSQL y Flyway
check_content "Dependencia PostgreSQL" "grep 'postgresql' pom.xml" "postgresql"
check_content "Dependencia Flyway" "grep 'flyway-core' pom.xml" "flyway-core"

# 3.2 Verificar migraciones Flyway
check "Migraciones Flyway V1__" "test -f src/main/resources/db/migration/V1__create_schema.sql"
check "Migraciones Flyway V2__" "test -f src/main/resources/db/migration/V2__insert_initial_data.sql"

# 3.3 Verificar configuración de base de datos
check_content "Configuración PostgreSQL en application.yml" "grep -A3 'datasource:' src/main/resources/application.yml | grep 'postgresql'" "postgresql"

# ==================== 4. SEGURIDAD JWT ====================
log "\n${YELLOW}=== 4. SEGURIDAD JWT ===${NC}\n"

# 4.1 Verificar dependencias JWT
check_content "Dependencia JJWT" "grep 'jjwt' pom.xml" "jjwt"

# 4.2 Verificar configuración de seguridad
check_content "Configuración JWT secret" "grep 'jwt:' src/main/resources/application.yml" "jwt:"
check_content "Clase JwtUtil" "find src/main/java -name 'JwtUtil.java' -type f" "JwtUtil.java"
check_content "Clase SecurityConfig" "find src/main/java -name 'SecurityConfig.java' -type f" "SecurityConfig.java"

# 4.3 Verificar roles
check_content "Roles definidos" "grep -r 'ROLE_AFFILIATE\|ROLE_ANALYST\|ROLE_ADMIN' src/main/java --include='*.java' | head -3" "ROLE_"

# 4.4 Verificar endpoints de autenticación
check_content "AuthController" "find src/main/java -name 'AuthController.java' -type f" "AuthController.java"

# ==================== 5. VALIDACIONES Y MANEJO DE ERRORES ====================
log "\n${YELLOW}=== 5. VALIDACIONES Y MANEJO DE ERRORES ===${NC}\n"

# 5.1 Verificar validaciones
check_content "Bean Validation en DTOs" "grep -r '@NotBlank\|@NotNull\|@Size\|@Email' src/main/java --include='*.java' | head -2" "@"

# 5.2 Verificar GlobalExceptionHandler
check_content "GlobalExceptionHandler" "find src/main/java -name 'GlobalExceptionHandler.java' -type f" "GlobalExceptionHandler.java"

# 5.3 Verificar ProblemDetail
check_content "Uso de ProblemDetail" "grep -r 'ProblemDetail' src/main/java --include='*.java'" "ProblemDetail"

# ==================== 6. OBSERVABILIDAD ====================
log "\n${YELLOW}=== 6. OBSERVABILIDAD ===${NC}\n"

# 6.1 Verificar Actuator
check_content "Dependencia Actuator" "grep 'spring-boot-starter-actuator' pom.xml" "actuator"
check_content "Configuración Actuator" "grep 'actuator:' src/main/resources/application.yml" "actuator"

# 6.2 Verificar Prometheus
check_content "Dependencia Micrometer Prometheus" "grep 'micrometer-registry-prometheus' pom.xml" "prometheus"

# ==================== 7. DOCKER Y CONTAINERIZACIÓN ====================
log "\n${YELLOW}=== 7. DOCKER Y CONTAINERIZACIÓN ===${NC}\n"

# 7.1 Verificar Dockerfile
check "Dockerfile existe" "test -f Dockerfile"
check_content "Dockerfile multi-stage" "grep 'FROM.*AS build' Dockerfile" "AS build"

# 7.2 Verificar docker-compose.yml
check "docker-compose.yml existe" "test -f docker-compose.yml"
check_content "Servicios en docker-compose" "grep -c '^  [a-z]' docker-compose.yml | grep -q '[4-9]'"

# ==================== 8. PRUEBAS ====================
log "\n${YELLOW}=== 8. PRUEBAS ===${NC}\n"

# 8.1 Verificar dependencias de pruebas
check_content "Dependencia JUnit" "grep 'spring-boot-starter-test' pom.xml" "starter-test"
check_content "Dependencia Testcontainers" "grep 'testcontainers' pom.xml" "testcontainers"

# 8.2 Verificar pruebas unitarias
check "Pruebas unitarias existen" "find src/test -name '*Test.java' -type f | wc -l | grep -q '[1-9]'"

# ==================== 9. COMPILACIÓN Y CONSTRUCCIÓN ====================
log "\n${YELLOW}=== 9. COMPILACIÓN Y CONSTRUCCIÓN ===${NC}\n"

# 9.1 Verificar que compila
check "Proyecto compila correctamente" "mvn clean compile -q"

# 9.2 Verificar que pasa las pruebas
check "Pruebas unitarias pasan" "mvn test -q"

# ==================== 10. EJECUCIÓN CON DOCKER COMPOSE ====================
log "\n${YELLOW}=== 10. EJECUCIÓN CON DOCKER COMPOSE ===${NC}\n"

# 10.1 Detener contenedores previos si existen
log "${BLUE}Deteniendo contenedores previos...${NC}"
docker-compose down >> $LOG_FILE 2>&1

# 10.2 Construir y levantar servicios
log "${BLUE}Construyendo y levantando servicios con Docker Compose...${NC}"
docker-compose up --build -d >> $LOG_FILE 2>&1

# Esperar a que los servicios estén listos
log "${BLUE}Esperando 45 segundos para que los servicios inicien...${NC}"
sleep 45

# 10.3 Verificar que los servicios están corriendo
check "PostgreSQL está corriendo" "docker ps | grep -q '$POSTGRES_CONTAINER'"
check "Credit Application Service está corriendo" "docker ps | grep -q '$APP_CONTAINER'"
check "Risk Central Mock Service está corriendo" "docker ps | grep -q '$RISK_CONTAINER'"

# ==================== 11. PRUEBAS FUNCIONALES ====================
log "\n${YELLOW}=== 11. PRUEBAS FUNCIONALES ===${NC}\n"

# 11.1 Probar health check
check_content "Health check Actuator" "curl -s http://localhost:8080/api/actuator/health" "UP"

# 11.2 Probar login admin
log "${BLUE}Probando login de administrador...${NC}"
ADMIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')

if echo "$ADMIN_RESPONSE" | grep -q '"token"'; then
    log "${GREEN}   ✅ Login admin - OK${NC}"
    echo "✅ Login admin" >> $TEST_RESULTS

    # Extraer token
    ADMIN_TOKEN=$(echo "$ADMIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    echo "$ADMIN_TOKEN" > $TOKEN_FILE
else
    log "${RED}   ❌ Login admin - FALLÓ${NC}"
    echo "❌ Login admin" >> $TEST_RESULTS
fi

# 11.3 Probar creación de afiliado (requiere admin)
if [ ! -z "$ADMIN_TOKEN" ]; then
    log "${BLUE}Probando creación de afiliado...${NC}"
    AFFILIATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/affiliates \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $ADMIN_TOKEN" \
      -d '{
        "document": "999888777",
        "name": "Usuario Test",
        "salary": 3500.0,
        "affiliationDate": "2023-06-15",
        "status": "ACTIVE"
      }')

    if echo "$AFFILIATE_RESPONSE" | grep -q '"id"'; then
        log "${GREEN}   ✅ Creación afiliado - OK${NC}"
        echo "✅ Creación afiliado" >> $TEST_RESULTS
    else
        log "${RED}   ❌ Creación afiliado - FALLÓ${NC}"
        echo "❌ Creación afiliado" >> $TEST_RESULTS
    fi
fi

# 11.4 Probar login afiliado
log "${BLUE}Probando login de afiliado...${NC}"
AFFILIATE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"johndoe","password":"password123"}')

if echo "$AFFILIATE_RESPONSE" | grep -q '"token"'; then
    log "${GREEN}   ✅ Login afiliado - OK${NC}"
    echo "✅ Login afiliado" >> $TEST_RESULTS

    # Extraer token
    AFFILIATE_TOKEN=$(echo "$AFFILIATE_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
else
    log "${RED}   ❌ Login afiliado - FALLÓ${NC}"
    echo "❌ Login afiliado" >> $TEST_RESULTS
fi

# 11.5 Probar solicitud de crédito
if [ ! -z "$AFFILIATE_TOKEN" ]; then
    log "${BLUE}Probando solicitud de crédito...${NC}"
    CREDIT_RESPONSE=$(curl -s -X POST http://localhost:8080/api/credit-applications \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $AFFILIATE_TOKEN" \
      -d '{
        "affiliateId": 1,
        "requestedAmount": 5000,
        "termMonths": 12,
        "proposedRate": 10.0
      }')

    if echo "$CREDIT_RESPONSE" | grep -q '"status"'; then
        log "${GREEN}   ✅ Solicitud crédito - OK${NC}"

        # Extraer detalles
        STATUS=$(echo "$CREDIT_RESPONSE" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        RISK_LEVEL=$(echo "$CREDIT_RESPONSE" | grep -o '"riskLevel":"[^"]*"' | cut -d'"' -f4)

        log "   📊 Estado: $STATUS, Nivel Riesgo: $RISK_LEVEL"
        echo "✅ Solicitud crédito" >> $TEST_RESULTS
    else
        log "${RED}   ❌ Solicitud crédito - FALLÓ${NC}"
        echo "❌ Solicitud crédito" >> $TEST_RESULTS
    fi
fi

# 11.6 Probar Risk Central Mock Service
log "${BLUE}Probando Risk Central Mock Service...${NC}"
RISK_RESPONSE=$(curl -s -X POST http://localhost:8081/api/risk-evaluation \
  -H "Content-Type: application/json" \
  -d '{
    "documento": "123456789",
    "monto": 5000000,
    "plazo": 36
  }')

if echo "$RISK_RESPONSE" | grep -q '"score"'; then
    log "${GREEN}   ✅ Risk Central Service - OK${NC}"

    # Verificar que el score está entre 300-950
    SCORE=$(echo "$RISK_RESPONSE" | grep -o '"score":[0-9]*' | cut -d':' -f2)
    if [ "$SCORE" -ge 300 ] && [ "$SCORE" -le 950 ]; then
        log "   📊 Score recibido: $SCORE (correcto: 300-950)"
        echo "✅ Risk Central Service" >> $TEST_RESULTS
    else
        log "${RED}   ❌ Score fuera de rango: $SCORE${NC}"
        echo "❌ Risk Central Service" >> $TEST_RESULTS
    fi
else
    log "${RED}   ❌ Risk Central Service - FALLÓ${NC}"
    echo "❌ Risk Central Service" >> $TEST_RESULTS
fi

# 11.7 Probar métricas Prometheus
log "${BLUE}Probando métricas Prometheus...${NC}"
METRICS_RESPONSE=$(curl -s http://localhost:8080/api/actuator/prometheus)

if echo "$METRICS_RESPONSE" | grep -q "http_server_requests_seconds_count"; then
    log "${GREEN}   ✅ Métricas Prometheus - OK${NC}"
    echo "✅ Métricas Prometheus" >> $TEST_RESULTS
else
    log "${RED}   ❌ Métricas Prometheus - FALLÓ${NC}"
    echo "❌ Métricas Prometheus" >> $TEST_RESULTS
fi

# 11.8 Probar validaciones de entrada
log "${BLUE}Probando validaciones...${NC}"
VALIDATION_RESPONSE=$(curl -s -X POST http://localhost:8080/api/credit-applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $AFFILIATE_TOKEN" \
  -d '{
    "affiliateId": 1,
    "requestedAmount": -100,
    "termMonths": 0,
    "proposedRate": -5.0
  }')

if echo "$VALIDATION_RESPONSE" | grep -q '"status":400'; then
    log "${GREEN}   ✅ Validaciones Bean Validation - OK${NC}"
    echo "✅ Validaciones Bean Validation" >> $TEST_RESULTS
else
    log "${RED}   ❌ Validaciones Bean Validation - FALLÓ${NC}"
    echo "❌ Validaciones Bean Validation" >> $TEST_RESULTS
fi

# ==================== 12. VERIFICACIÓN DE BASE DE DATOS ====================
log "\n${YELLOW}=== 12. VERIFICACIÓN DE BASE DE DATOS ===${NC}\n"

# 12.1 Verificar que Flyway creó las tablas
log "${BLUE}Verificando tablas en base de datos...${NC}"
TABLES_COUNT=$(docker exec $POSTGRES_CONTAINER psql -U postgres -d coopcredit -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='public';" 2>/dev/null | tr -d '[:space:]')

if [ "$TABLES_COUNT" -ge 5 ]; then
    log "${GREEN}   ✅ Tablas creadas: $TABLES_COUNT - OK${NC}"
    echo "✅ Tablas en base de datos" >> $TEST_RESULTS

    # Listar tablas
    log "   📋 Tablas encontradas:"
    docker exec $POSTGRES_CONTAINER psql -U postgres -d coopcredit -c "SELECT table_name FROM information_schema.tables WHERE table_schema='public' ORDER BY table_name;" 2>/dev/null | grep -v "table_name" | grep -v "^-" | grep -v "^$" | while read table; do
        log "      - $table"
    done
else
    log "${RED}   ❌ Faltan tablas: $TABLES_COUNT (esperado: ≥5)${NC}"
    echo "❌ Tablas en base de datos" >> $TEST_RESULTS
fi

# 12.2 Verificar datos iniciales
log "${BLUE}Verificando datos iniciales...${NC}"
USERS_COUNT=$(docker exec $POSTGRES_CONTAINER psql -U postgres -d coopcredit -t -c "SELECT COUNT(*) FROM users;" 2>/dev/null | tr -d '[:space:]')
AFFILIATES_COUNT=$(docker exec $POSTGRES_CONTAINER psql -U postgres -d coopcredit -t -c "SELECT COUNT(*) FROM affiliate;" 2>/dev/null | tr -d '[:space:]')

if [ "$USERS_COUNT" -ge 5 ] && [ "$AFFILIATES_COUNT" -ge 4 ]; then
    log "${GREEN}   ✅ Datos iniciales - OK${NC}"
    log "      Usuarios: $USERS_COUNT, Afiliados: $AFFILIATES_COUNT"
    echo "✅ Datos iniciales" >> $TEST_RESULTS
else
    log "${RED}   ❌ Datos iniciales incompletos${NC}"
    echo "❌ Datos iniciales" >> $TEST_RESULTS
fi

# ==================== 13. LIMPIEZA ====================
log "\n${YELLOW}=== 13. LIMPIEZA ===${NC}\n"

log "${BLUE}Deteniendo servicios...${NC}"
docker-compose down >> $LOG_FILE 2>&1

# ==================== 14. RESUMEN FINAL ====================
log "\n${YELLOW}=== 14. RESUMEN FINAL ===${NC}\n"

TOTAL_TESTS=$(wc -l < $TEST_RESULTS)
PASSED_TESTS=$(grep -c "✅" $TEST_RESULTS)
FAILED_TESTS=$(grep -c "❌" $TEST_RESULTS)

log "${BLUE}📊 RESULTADOS DE VERIFICACIÓN:${NC}"
log "   Total pruebas: $TOTAL_TESTS"
log "   ${GREEN}Aprobadas: $PASSED_TESTS${NC}"
log "   ${RED}Falladas: $FAILED_TESTS${NC}"

if [ $FAILED_TESTS -eq 0 ]; then
    log "\n${GREEN}🎉 ¡FELICITACIONES! 🎉${NC}"
    log "Tu proyecto CUMPLE CON TODOS los requerimientos del PDF."
    log "Puedes entregarlo con confianza."
else
    log "\n${YELLOW}⚠️  ATENCIÓN ⚠️${NC}"
    log "Hay $FAILED_TESTS requisitos que necesitan atención."
    log "\n${BLUE}Pruebas falladas:${NC}"
    grep "❌" $TEST_RESULTS

    log "\n${BLUE}Revisa el archivo de log para más detalles:${NC}"
    log "   $LOG_FILE"
fi

log "\n${BLUE}========================================${NC}"
log "${GREEN}   VERIFICACIÓN COMPLETADA              ${NC}"
log "${BLUE}========================================${NC}"

# Mostrar resultados detallados
log "\n${YELLOW}=== DETALLE COMPLETO DE PRUEBAS ===${NC}"
cat $TEST_RESULTS

# Eliminar archivos temporales
rm -f $TOKEN_FILE

# Retornar código de salida
if [ $FAILED_TESTS -eq 0 ]; then
    exit 0
else
    exit 1
fi