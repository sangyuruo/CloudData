
CREATE TABLE IF NOT EXISTS company (
    id uuid,
    name text,
    address text,
    telephone text,
    email text,
    PRIMARY KEY(id)
);
INSERT INTO company (id, name)
VALUES(83cec740-22b1-11e4-a4f0-7f1a8b30f852, 'Internal');

CREATE TABLE IF NOT EXISTS company_by_name (
    name text,
    id uuid,
    PRIMARY KEY(name, id)
);
INSERT INTO company_by_name (name, id)
VALUES('Internal',83cec740-22b1-11e4-a4f0-7f1a8b30f852);
