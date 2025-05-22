use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::{
    command::{
        model::Command,
        value_objets::{ChatId, MessageType},
    },
    task::{
        errors::TaskError,
        executor::{TaskExecutor, TaskExecutorImpl},
        model::{AgentResponse, TaskType},
        repository::TaskRepository,
        use_cases::{TaskUseCases, TaskUseCasesImpl},
    },
};

impl TaskUseCasesImpl {
    pub fn new(repository: Arc<dyn TaskRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl TaskUseCases for TaskUseCasesImpl {
    async fn execute_task(
        &self,
        agent_response: &AgentResponse,
        chat_id: ChatId,
    ) -> Result<Command, TaskError> {
        let result = match agent_response.task {
            TaskType::Refactor => {
                let code = self.get_key("code".to_string(), &agent_response);
                let request = self.get_key("request".to_string(), &agent_response);
                TaskExecutorImpl::refactor(self.repository.clone(), code, request)
            }
            TaskType::Weather => {
                let location = self.get_key("location".to_string(), &agent_response);
                TaskExecutorImpl::weather(location)
            }
            TaskType::OpenApp => {
                let app_name = self.get_key("app_name".to_string(), &agent_response);
                TaskExecutorImpl::open_app(app_name)
            }
            TaskType::BookmarkRecommendations => {
                let topic = self.get_key("topic".to_string(), &agent_response);
                TaskExecutorImpl::bookmark_recommendations(topic)
            }
            TaskType::OtherTopics => {
                let answer = self.get_key("answer".to_string(), &agent_response);
                TaskExecutorImpl::other_topics(self.repository.clone(), answer)
            }
            TaskType::LinkedinOfferRejection => {
                let offer = self.get_key("offer".to_string(), &agent_response);
                TaskExecutorImpl::linkedin_offer_rejection(self.repository.clone(), offer)
            }
        }
        .await;

        let result = result.map_err(|e| TaskError::ExecutionFailed(e.to_string()))?;
        let fav_name = self.get_key("topic".to_string(), &agent_response);

        let command = Command::new(
            "".to_string(),
            result,
            MessageType::Assistant,
            chat_id,
            fav_name,
            agent_response.task.clone(),
        )
        .map_err(TaskError::ExecutionFailed)?;

        Ok(command)
    }

    async fn get_task_type(&self, payload: &String) -> Result<AgentResponse, TaskError> {
        self.repository
            .get_task_type(&payload)
            .await
            .map_err(|e| e.into())
    }

    fn get_key(&self, key: String, agent_response: &AgentResponse) -> String {
        agent_response
            .parameters
            .get(key)
            .and_then(|v| v.as_str())
            .unwrap_or("")
            .to_string()
    }
}
