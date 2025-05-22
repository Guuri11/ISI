#[derive(Debug)]
pub enum RepositoryError {
    NotFound(String),
    Persistence(String),
    Duplicated(String),
    DatabaseError(String),
}
