use serde::Deserialize;
use std::env;

#[derive(Default, Clone, Debug, Deserialize)]
pub struct Settings {
    pub database_url: String,

    pub service_host: String,
    pub service_port: u16,
}

impl Settings {
    pub fn from_env() -> Result<Self, env::VarError> {
        dotenvy::dotenv().ok();

        Ok(Self {
            database_url: env::var("DB_URL")?,

            service_host: env::var("SERVICE_IP")?,
            service_port: env::var("SERVICE_PORT")?.parse::<u16>().unwrap_or(8080),
        })
    }
}
