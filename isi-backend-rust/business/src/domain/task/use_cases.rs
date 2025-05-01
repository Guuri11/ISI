use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::command::{model::Command, value_objets::ChatId};

use super::{errors::TaskError, model::AgentResponse, repository::TaskRepository};

#[async_trait]
pub trait TaskUseCases: Send + Sync {
    async fn execute_task(
        &self,
        agent_response: &AgentResponse,
        chat_id: ChatId,
    ) -> Result<Command, TaskError>;
    async fn get_task_type(&self, payload: &String) -> Result<AgentResponse, TaskError>;
    fn get_key(&self, key: String, agent_response: &AgentResponse) -> String;
}

pub struct TaskUseCasesImpl {
    pub repository: Arc<dyn TaskRepository + Send + Sync>,
}
