\connect postgres

SELECT 'CREATE DATABASE anomalydb'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'anomalydb'
)\gexec

SELECT 'CREATE DATABASE airqualitydb'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'airqualitydb'
)\gexec
