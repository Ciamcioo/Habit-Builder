INSERT INTO habit_user(id, email, username, first_name, last_name, age)
VALUES (RANDOM_UUID(), 'fooBar12@gmail.com', 'FooBar', 'Foo', 'Bar', 27);

INSERT INTO habit_user(id, email, username, first_name, last_name, age)
VALUES (RANDOM_UUID(), 'jhonyDoo@gov.com', 'John27', 'Johny', 'Doo', 30);

INSERT INTO habit_user(id, email, username)
VALUES (RANDOM_UUID(), 'maloclmNarvaes@gmail.com', 'MalcomXL');

INSERT INTO habit(id, name, habit_frequency, start_date, end_date, reminder, user_id)
VALUES (RANDOM_UUID(),
        'Programing',
        'DAILY',
        CURRENT_TIMESTAMP(),
        DATEADD(MONTH, 5, CURRENT_TIMESTAMP()),
        TRUE,
        SELECT id FROM habit_user WHERE habit_user.email='jhonyDoo@gov.com'
       );

INSERT INTO habit(id, name, habit_frequency, start_date, end_date, reminder , user_id)
VALUES (RANDOM_UUID(),
        'Finances',
        'DAILY',
        CURRENT_TIMESTAMP(),
        DATEADD(MONTH, 1, CURRENT_TIMESTAMP()),
        TRUE,
        SELECT id FROM habit_user WHERE habit_user.email='maloclmNarvaes@gmail.com'
       );

INSERT INTO habit(id, name, habit_frequency, user_id)
VALUES (RANDOM_UUID(),
        'Running',
        'WEEKLY',
        SELECT id FROM habit_user WHERE habit_user.email='fooBar12@gmail.com'
       );

