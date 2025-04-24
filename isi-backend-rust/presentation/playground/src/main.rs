use std::sync::Arc;

use business::domain::command::use_cases::{CommandUseCases, CommandUseCasesImpl};
use persistance::postgres::{
    command::repository::CommandRepository,
    db::{DatabaseConfig, create_postgres_pool},
};
use playground::config::settings::Settings;

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let settings = Settings::from_env()?;
    let pool = create_postgres_pool(&DatabaseConfig::new(settings.database_url.clone())).await?;

    let repository = CommandRepository::new(pool.clone());
    let repository_arc = Arc::new(repository);

    let usecase = CommandUseCasesImpl {
        repository: repository_arc,
    };

    let service = Arc::new(usecase);

    let result = service.get_commands().await?;

    // print total commands
    println!("Total commands: {}", result.len());
    Ok(())
}
