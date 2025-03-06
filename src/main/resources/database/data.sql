INSERT INTO habit(id, name, habit_frequency, start_date, end_date, reminder)
VALUES (RANDOM_UUID(), 'Programing', 'DAILY', CURRENT_TIMESTAMP(), DATEADD(MONTH, 5, CURRENT_TIMESTAMP()), TRUE);

INSERT INTO habit(id, name, habit_frequency, start_date, end_date, reminder)
VALUES (RANDOM_UUID(), 'Finances', 'DAILY', CURRENT_TIMESTAMP(), DATEADD(MONTH, 1, CURRENT_TIMESTAMP()), TRUE);

INSERT INTO habit(id, name, habit_frequency)
VALUES (RANDOM_UUID(), 'Running', 'WEEKLY');
