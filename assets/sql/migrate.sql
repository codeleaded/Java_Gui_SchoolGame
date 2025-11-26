CREATE TABLE IF NOT EXISTS clients(
    id uuid PRIMARY KEY NOT NULL,
    ip cidr NOT NULL,
    name text NOT NULL,
    style smallint NOT NULL,
    active boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS worlds(
    name text PRIMARY KEY NOT NULL,
    creator uuid NOT NULL REFERENCES clients(id),
    data bytea NOT NULL
);

CREATE TABLE IF NOT EXISTS scores(
    client uuid PRIMARY KEY NOT NULL REFERENCES clients(id),
    score integer NOT NULL
);
