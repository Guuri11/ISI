use std::sync::Arc;

use ai::gpt::{conf::GPTClient, task::repository::TaskRepository};
use business::domain::{
    chat::use_cases::{ChatUseCases, ChatUseCasesImpl},
    command::{
        use_cases::{CommandUseCases, CommandUseCasesImpl},
        value_objets::MessageType,
    },
    task::use_cases::{TaskUseCases, TaskUseCasesImpl},
};
use persistance::postgres::{
    chat::repository::ChatRepository,
    command::repository::CommandRepository,
    db::{DatabaseConfig, create_postgres_pool},
};
use playground::config::settings::Settings;
use tracing::error;

#[tokio::main]
async fn main() {
    if let Err(err) = run().await {
        error!("Error al ejecutar el servidor: {}", err);
    }
}

async fn run() -> Result<(), Box<dyn std::error::Error>> {
    println!("Iniciando el servidor...");
    let settings = Settings::from_env()?;
    let pool = create_postgres_pool(&DatabaseConfig::new(settings.database_url.clone())).await?;
    // Repositories
    let chat_repository = Arc::new(ChatRepository::new(pool.clone()));
    let command_repository = Arc::new(CommandRepository::new(pool.clone()));
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

    let result = command_service
        .register_command(
            "Cómo aprovecharías la fuerza de un cequia para generar energia hidraulica casera en casa".to_string(),
            None,
            MessageType::User,
            None,
        )
        .await
        .unwrap();

    println!("Resultado: {:?}", result);

    Ok(())
}
