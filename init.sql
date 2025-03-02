CREATE TABLE IF NOT EXISTS post (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    link TEXT NOT NULL UNIQUE,
    description TEXT,
    time BIGINT
);
