#!/bin/bash
/opt/mssql/bin/sqlservr &

echo "⏳ Waiting for SQL Server to start..."
sleep 15

echo "⚙️ Creating database gadgetdb..."
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "Myp@ssw0rd#2025" -Q "IF DB_ID('gadgetdb') IS NULL CREATE DATABASE gadgetdb"

wait