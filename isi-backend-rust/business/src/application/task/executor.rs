use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::{
    fav::use_cases::create::CreateFavUseCase,
    task::{errors::TaskError, executor::TaskExecutor, repository::TaskRepository},
};

pub struct TaskExecutorImpl;

// TODO: quizás renombrar task repository por task service, no sé, lo que hago es preguntar a un servicio de ia realmente
#[async_trait]
impl TaskExecutor for TaskExecutorImpl {
    async fn refactor(
        repository: Arc<dyn TaskRepository + Send + Sync>,
        code: String,
        requirements: String,
    ) -> Result<String, TaskError> {
        let result = repository.code_assistant(code, requirements).await?;
        Ok(result)
    }

    async fn weather(location: String) -> Result<String, TaskError> {
        // TODO: Implement the weather fetching logic
        Ok(format!("Weather in {} on", location))
    }

    async fn open_app(app_name: String) -> Result<String, TaskError> {
        // TOOD: Open the app using the app name
        Ok(format!("Opening app: {}", app_name))
    }

    async fn bookmark_recommendations(
        topic: String,
        fav_create_usecase: Arc<dyn CreateFavUseCase + Send + Sync>,
    ) -> Result<String, TaskError> {
        fav_create_usecase
            .execute(topic)
            .await
            .map_err(|e| TaskError::ExecutionFailed(e.to_string()))?;

        Ok("Bookmark created".to_string())
    }

    async fn other_topics(
        repository: Arc<dyn TaskRepository + Send + Sync>,
        topic: String,
    ) -> Result<String, TaskError> {
        let result = repository.other_topics(topic).await?;
        Ok(result)
    }

    async fn linkedin_offer_rejection(
        repository: Arc<dyn TaskRepository + Send + Sync>,
        offer: String,
    ) -> Result<String, TaskError> {
        let result = repository.linkedin_offer_rejection(offer).await?;
        Ok(result)
    }
}
