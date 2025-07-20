SELECT scores.score, clients.name
FROM scores
LEFT JOIN clients on scores.client = clients.id
ORDER BY score DESC
LIMIT 10
