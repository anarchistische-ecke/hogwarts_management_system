SELECT *
FROM student
WHERE age BETWEEN 5 AND 20;

SELECT name
FROM student;

SELECT name
FROM student
WHERE LOWER(name) LIKE LOWER('%o%');

SELECT *
FROM student
WHERE age < id;

SELECT *
FROM student
ORDER BY age;