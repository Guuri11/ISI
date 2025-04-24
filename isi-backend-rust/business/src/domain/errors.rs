#[derive(Debug)]
pub enum RepositoryError {
    NotFound(String),
    Persistence(String),
    DuplicateEntity(String),
    DatabaseError(String),
}
