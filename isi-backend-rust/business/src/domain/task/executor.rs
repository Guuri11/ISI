use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::fav::use_cases::create::CreateFavUseCase;

/// This module defines the TaskExecutor trait and its implementation.
/// The TaskExecutor trait provides methods for executing various tasks.
/// Each method takes a string input and returns a Result with either a string output or a TaskError.
/// The TaskExecutorImpl struct implements the TaskExecutor trait.
/// The methods in TaskExecutorImpl provide basic implementations for the tasks.
/// The methods include refactoring code, getting weather information, opening an app,
/// providing bookmark recommendations, handling other topics, and rejecting LinkedIn offers.
/// The methods return formatted strings based on the input parameters.
// The TaskError type is used to handle errors that may occur during task execution.
use super::{errors::TaskError, repository::TaskRepository};

#[async_trait]
pub trait TaskExecutor {
    async fn refactor(
        repository: Arc<dyn TaskRepository + Send + Sync>,
        code: String,
        requirements: String,
    ) -> Result<String, TaskError>;
    async fn weather(location: String) -> Result<String, TaskError>;
    async fn open_app(app_name: String) -> Result<String, TaskError>;
    async fn bookmark_recommendations(
        topic: String,
        fav_create_use_case: Arc<dyn CreateFavUseCase + Send + Sync>,
    ) -> Result<String, TaskError>;
    async fn other_topics(
        repository: Arc<dyn TaskRepository + Send + Sync>,
        topic: String,
    ) -> Result<String, TaskError>;
    async fn linkedin_offer_rejection(
        repository: Arc<dyn TaskRepository + Send + Sync>,
        offer: String,
    ) -> Result<String, TaskError>;
}
