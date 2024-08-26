CREATE TABLE Chat (
    id UUID PRIMARY KEY,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

CREATE TABLE Fav (
    id UUID PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE Command (
    id UUID PRIMARY KEY,
    log VARCHAR(255),
    content TEXT,
    messageType VARCHAR(255),
    chat_id UUID,
    fav_name TEXT,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES Chat(id)
);
