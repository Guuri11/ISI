use async_trait::async_trait;

use crate::domain::screenshot::errors::ScreenshotError;

#[async_trait]
pub trait ScreenshotService: Send + Sync {
    async fn shoot(&self, filename: String) -> Result<(), ScreenshotError>;
}

pub struct ScreenshotServiceImpl;
