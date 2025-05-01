use async_trait::async_trait;

use crate::domain::errors::RepositoryError;

use super::model::AgentResponse;

#[async_trait]
pub trait TaskRepository: Send + Sync {
    async fn get_task_type(&self, request: &String) -> Result<AgentResponse, RepositoryError>;
    async fn code_assistant(
        &self,
        code: String,
        requirements: String,
    ) -> Result<String, RepositoryError>;
}
