use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::{
    command::{model::Command, value_objets::ChatId},
    fav::use_cases::create::CreateFavUseCase,
    task::{errors::TaskError, model::AgentResponse, repository::TaskRepository},
};

#[async_trait]
pub trait ExecuteTaskUseCase: Send + Sync {
    async fn execute(
        &self,
        agent_response: &AgentResponse,
        chat_id: ChatId,
    ) -> Result<Command, TaskError>;
    async fn get_task_type(&self, payload: &String) -> Result<AgentResponse, TaskError>;
    fn get_key(&self, key: String, agent_response: &AgentResponse) -> String;
}

pub struct ExecuteTaskUseCaseImpl {
    pub repository: Arc<dyn TaskRepository + Send + Sync>,
    pub fav_create_usecase: Arc<dyn CreateFavUseCase + Send + Sync>,
}
