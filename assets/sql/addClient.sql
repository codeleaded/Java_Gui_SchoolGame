INSERT INTO clients(id, ip, name, style, active)
VALUES (?::uuid, ?::cidr, ?, 0, true)
