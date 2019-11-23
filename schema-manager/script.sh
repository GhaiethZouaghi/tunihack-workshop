#! /bin/bash

DB_HOST=cassandra
DB_PORT=9042
DB_USER=dse
DB_PASS=\"\"

ARGS="$DB_HOST $DB_PORT -u $DB_USER -p $DB_PASS"

#### Create keyspace
echo "[INFO] Creating keyspace"
query="CREATE KEYSPACE IF NOT EXISTS tunihack WITH replication = {'class': 'SimpleStrategy','replication_factor': 3 };"
cqlsh $ARGS -e "$query"

#### Create tables
echo "[INFO] Creating student table"
student="CREATE TABLE IF NOT EXISTS tunihack.student (
  id INT,
  name TEXT,
  age INT,
  specialization TEXT,
  class LIST<TEXT>,
  PRIMARY KEY (id)
);"

cqlsh $ARGS -e "$student"

echo "[INFO] Creating teacher table"
teacher="CREATE TABLE IF NOT EXISTS tunihack.teacher (
  id INT,
  name TEXT,
  age INT,
  class TEXT,
  students LIST<INT>,
  PRIMARY KEY (id)
);"

cqlsh $ARGS -e "$teacher"

echo "[INFO] Tables created successfully"

echo "[INFO] Loading student data"
query="INSERT INTO tunihack.student(id, name, age, specialization, class) 
    VALUES (0, 'Mohamed', 20, 'Math Physics', ['Math', 'Physics', 'Programming']);
    INSERT INTO tunihack.student(id, name, age, specialization, class) 
    VALUES (1, 'Ghaieth', 28, 'Computer Science',['Machine Learning', 'Physics', 'Programming']);
    INSERT INTO tunihack.student(id, name, age, specialization, class) 
    VALUES (2, 'Zied', 23, 'Chemistry', ['Programming', 'Chemistry']);"
cqlsh $ARGS -e "$query"

echo "[INFO] Loading teacher data"
query="INSERT INTO tunihack.teacher(id, name, age, class, students) 
    VALUES (0, 'Mark', 40, 'Math', [0]);
    INSERT INTO tunihack.teacher(id, name, age, class, students) 
    VALUES (1, 'Greg', 50, 'Machine Learning', [1]);
    INSERT INTO tunihack.teacher(id, name, age, class, students) 
    VALUES (2, 'Elias', 45, 'Programming', [0, 1, 2]);"
cqlsh $ARGS -e "$query"

echo "[INFO] Data Loaded successfully"
