use serde::Deserialize;
use std::env;

#[derive(Default, Clone, Debug, Deserialize)]
pub struct Settings {
    pub database_url: String,

    pub service_host: String,
    pub service_port: u16,

    pub ai_api_key: String,
    pub ai_api_url: String,

    pub env: String,
}

impl Settings {
    pub fn from_env() -> Result<Self, env::VarError> {
        dotenvy::dotenv().ok();

        Ok(Self {
            database_url: env::var("DB_URL")?,

            service_host: env::var("SERVICE_IP")?,
            service_port: env::var("SERVICE_PORT")?.parse::<u16>().unwrap_or(8080),

            ai_api_key: env::var("AI_API_KEY")?,
            ai_api_url: env::var("AI_API_URL")?,

            env: env::var("ENV").unwrap_or_else(|_| "development".to_string()),
        })
    }
}
