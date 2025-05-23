use async_trait::async_trait;

use crate::domain::clipboard::errors::ClipboardError;

#[async_trait]
pub trait ClipboardService: Send + Sync {
    async fn paste(&self) -> Result<String, ClipboardError>;
    async fn copy(&self, text: String) -> Result<(), ClipboardError>;
}

pub struct ClipboardServiceImpl;
