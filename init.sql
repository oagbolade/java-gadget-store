IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'gadgetdb')
BEGIN
    CREATE DATABASE gadgetdb;
END
GO
