INSERT INTO worlds(creator, name, data)
VALUES (?::uuid, ?, ?)
ON CONFLICT (name) DO
UPDATE SET data = EXCLUDED.data
WHERE worlds.creator = EXCLUDED.creator
