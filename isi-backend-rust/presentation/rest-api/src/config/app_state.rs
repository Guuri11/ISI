use std::sync::Arc;

use ai::gpt::{conf::GPTClient, task::repository::TaskRepository};
use business::domain::{
    chat::use_cases::{ChatUseCases, ChatUseCasesImpl},
    command::use_cases::{CommandUseCases, CommandUseCasesImpl},
    fav::use_cases::{FavUseCases, FavUseCasesImpl},
    task::use_cases::{TaskUseCases, TaskUseCasesImpl},
};
use persistance::postgres::{
    chat::repository::ChatRepository, command::repository::CommandRepository,
    fav::repository::FavRepository,
};
use sqlx::PgPool;

use super::settings::Settings;

#[derive(Clone)]
pub struct AppState {
    pub settings: Settings,
    pub chat_service: Arc<dyn ChatUseCases>,
    pub command_service: Arc<dyn CommandUseCases>,
    pub fav_service: Arc<dyn FavUseCases>,
}

impl AppState {
    pub fn new(pool: PgPool, settings: Settings) -> Self {
        // Repositories
        let chat_repository = Arc::new(ChatRepository::new(pool.clone()));
        let command_repository = Arc::new(CommandRepository::new(pool.clone()));
        let fav_repository = Arc::new(FavRepository::new(pool.clone()));
        let gpt_client = GPTClient::new(settings.ai_api_key.clone(), settings.ai_api_url.clone());
        let task_repository = Arc::new(TaskRepository::new(gpt_client.clone()));

        // Services
        let chat_service: Arc<dyn ChatUseCases> = Arc::new(ChatUseCasesImpl {
            repository: chat_repository,
        });

        let task_service: Arc<dyn TaskUseCases> = Arc::new(TaskUseCasesImpl {
            repository: task_repository,
        });

        let command_service: Arc<dyn CommandUseCases> = Arc::new(CommandUseCasesImpl {
            repository: command_repository,
            task_service: task_service.clone(),
            chat_service: chat_service.clone(),
        });

        let fav_service: Arc<dyn FavUseCases> = Arc::new(FavUseCasesImpl {
            repository: fav_repository,
        });

        Self {
            settings,
            chat_service,
            command_service,
            fav_service,
        }
    }
}
