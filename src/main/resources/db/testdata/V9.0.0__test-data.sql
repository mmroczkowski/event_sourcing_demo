INSERT INTO persistent_event (aggregate_id, type, payload, create_date)
VALUES ('123e4567-e89b-12d3-a456-426655440000',
        'org.demo.write.aggregate.account.event.AccountOpenedEvent',
        '{ "aggregateId": "123e4567-e89b-12d3-a456-426655440000", "date":"2022-01-01 12:00:00.654", "accountNumber": "A12345", "initialDeposit": 1000, "creditLine": 5000 }',
        '2022-01-01 12:00:00.654'),
       ('456e4567-e89b-12d3-a456-426655441111',
        'org.demo.write.aggregate.account.event.AccountOpenedEvent',
        '{ "aggregateId": "456e4567-e89b-12d3-a456-426655441111", "date":"2022-01-02 12:00:00.956", "accountNumber": "B67890", "initialDeposit": 2000, "creditLine": 6000 }',
        '2022-01-02 12:00:00.956');

INSERT INTO persistent_event (aggregate_id, type, payload, create_date)
VALUES ('123e4567-e89b-12d3-a456-426655440000',
        'org.demo.write.aggregate.account.event.PaymentExecutedEvent',
        '{ "aggregateId": "123e4567-e89b-12d3-a456-426655440000", "date":"2022-01-03 12:00:00.367", "amount": -500 }',
        '2022-01-03 12:00:00.367'),
       ('456e4567-e89b-12d3-a456-426655441111',
        'org.demo.write.aggregate.account.event.PaymentExecutedEvent',
        '{ "aggregateId": "456e4567-e89b-12d3-a456-426655441111", "date":"2022-01-04 12:00:00.843", "amount": 1000 }',
        '2022-01-04 12:00:00.843');

INSERT INTO account_projection (id, account_number, balance, credit_line)
VALUES ('123e4567-e89b-12d3-a456-426655440000',
        'A12345',
        500,
        5000),
       ('456e4567-e89b-12d3-a456-426655441111',
        'B67890',
        3000,
        6000);