use std::sync::Arc;

use ai::gpt::{conf::GPTClient, task::repository::TaskRepository};
use business::domain::{
    chat::use_cases::create::{CreateChatUseCase, CreateChatUseCaseImpl},
    command::use_cases::{
        create::{CreateCommandUseCase, CreateCommandUseCaseImpl},
        delete::{DeleteCommandUseCase, DeleteCommandUseCaseImpl},
        get_by::{GetByCommandUseCase, GetByCommandUseCaseImpl},
    },
    task::use_cases::execute::{ExecuteTaskUseCase, ExecuteTaskUseCaseImpl},
};
use persistance::postgres::{
    chat::repository::ChatRepository, command::repository::CommandRepository,
};
use sqlx::PgPool;

use super::settings::Settings;

#[derive(Clone)]
pub struct AppState {
    pub settings: Settings,
    pub create_command_use_case: Arc<dyn CreateCommandUseCase>,
    pub get_commands_use_case: Arc<dyn GetByCommandUseCase>,
    pub delete_command_use_case: Arc<dyn DeleteCommandUseCase>,
}

impl AppState {
    pub fn new(pool: PgPool, settings: Settings) -> Self {
        // Repositories
        let chat_repository = Arc::new(ChatRepository::new(pool.clone()));
        let command_repository = Arc::new(CommandRepository::new(pool.clone()));
        let gpt_client = GPTClient::new(settings.ai_api_key.clone(), settings.ai_api_url.clone());
        let task_repository = Arc::new(TaskRepository::new(gpt_client.clone()));

        // Services
        let create_chat_use_case: Arc<dyn CreateChatUseCase> = Arc::new(CreateChatUseCaseImpl {
            repository: chat_repository,
        });

        let get_commands_use_case: Arc<dyn GetByCommandUseCase> =
            Arc::new(GetByCommandUseCaseImpl {
                repository: command_repository.clone(),
            });
        let execute_task_use_case: Arc<dyn ExecuteTaskUseCase> = Arc::new(ExecuteTaskUseCaseImpl {
            repository: task_repository,
        });

        let create_command_use_case: Arc<dyn CreateCommandUseCase> =
            Arc::new(CreateCommandUseCaseImpl {
                repository: command_repository.clone(),
                execute_task_use_case: execute_task_use_case.clone(),
                create_chat_use_case: create_chat_use_case.clone(),
            });

        let delete_command_use_case: Arc<dyn DeleteCommandUseCase> =
            Arc::new(DeleteCommandUseCaseImpl {
                repository: command_repository.clone(),
            });

        Self {
            settings,
            create_command_use_case,
            get_commands_use_case,
            delete_command_use_case,
        }
    }
}
