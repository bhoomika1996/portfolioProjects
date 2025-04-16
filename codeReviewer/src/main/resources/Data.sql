CREATE TABLE IF NOT EXISTS codeSnippetRecords (
                                     id BIGINT PRIMARY KEY,
                                     lang VARCHAR(255),
                                     code VARCHAR(255),
    issues VARCHAR(255),
    suggestions VARCHAR(255),
    difficulty VARCHAR(255),
    userId VARCHAR(255),
    createdAt TIMESTAMP NOT NULL
    );
