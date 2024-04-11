CREATE TABLE Chat (
    id UUID PRIMARY KEY,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP
);

CREATE TABLE Command (
    id UUID PRIMARY KEY,
    log VARCHAR(255),
    content TEXT,
    messageType VARCHAR(255),
    chat_id UUID,
    createdAt TIMESTAMP,
    updatedAt TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES Chat(id)
);
