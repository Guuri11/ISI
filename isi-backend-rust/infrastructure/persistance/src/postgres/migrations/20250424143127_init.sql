-- Add migration script here

-- Up migration
-- Crear tipos enumerados
CREATE TYPE message_type AS ENUM ('Assistant', 'User', 'System', 'Function');


CREATE TABLE IF NOT EXISTS chats (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS favs (
    id UUID PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS commands (
    id UUID PRIMARY KEY,
    log VARCHAR(255),
    content TEXT NOT NULL,
    message_type message_type NOT NULL,
    chat_id UUID NOT NULL,
    fav_name TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chats(id)
);

