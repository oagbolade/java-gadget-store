#!/bin/bash

set -e

echo "🚀 Starting Docker services..."
docker compose down --volumes
docker compose up -d

echo "⏳ Waiting for SQL Server to be ready..."
sleep 10

# wait up to ~60 sec
ATTEMPTS=0
until docker exec mssql /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'Myp@ssw0rd#2025' -Q "SELECT 1" > /dev/null 2>&1; do
  sleep 2
  ATTEMPTS=$((ATTEMPTS+1))
  echo "SQL Server not ready yet... ($ATTEMPTS)"
  if [ $ATTEMPTS -gt 30 ]; then
    echo "❌ SQL Server did not become ready in time."
    exit 1
  fi
done

echo "✅ SQL Server is up!"

echo "📦 Creating database if not exists..."
docker exec mssql /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P 'Myp@ssw0rd#2025' \
  -i /init.sql || true

echo "🎉 gadgetdb ready!"

echo "✅ App + DB + Redis running!"
docker compose ps
