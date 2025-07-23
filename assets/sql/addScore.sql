INSERT INTO scores(client, score)
VALUES (?::uuid, ?)
ON CONFLICT (client)
DO UPDATE SET score = EXCLUDED.score
