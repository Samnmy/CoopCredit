#!/bin/bash
echo "=== LIMPIEZA Y ARRANQUE COMPLETO ==="

# 1. Limpiar proyecto
echo "1. Limpiando proyecto..."
mvn clean

# 2. Detener Docker si está corriendo
echo "2. Deteniendo contenedores Docker..."
docker-compose down 2>/dev/null

# 3. Iniciar PostgreSQL
echo "3. Iniciando PostgreSQL..."
docker-compose up -d postgres

# 4. Esperar a que PostgreSQL esté listo
echo "4. Esperando a PostgreSQL..."
sleep 10

# 5. Crear base de datos si no existe
echo "5. Creando base de datos..."
docker exec coopcredit-postgres psql -U postgres -c "CREATE DATABASE coopcredit;" 2>/dev/null || true

# 6. Compilar proyecto
echo "6. Compilando proyecto..."
mvn compile

# 7. Ejecutar migraciones Flyway directamente
echo "7. Ejecutando migraciones Flyway..."
mvn flyway:migrate

# 8. Verificar migraciones
echo "8. Verificando migraciones..."
docker exec coopcredit-postgres psql -U postgres -d coopcredit -c "\dt"

# 9. Ejecutar aplicación
echo "9. Iniciando aplicación..."
echo "=== APLICACIÓN INICIANDO ==="
mvn spring-boot:run