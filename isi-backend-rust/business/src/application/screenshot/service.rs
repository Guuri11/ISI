use async_trait::async_trait;
use screenshots::Screen;

use crate::domain::screenshot::{
    errors::ScreenshotError,
    service::{ScreenshotService, ScreenshotServiceImpl},
};

#[async_trait]
impl ScreenshotService for ScreenshotServiceImpl {
    async fn shoot(&self, filename: String) -> Result<(), ScreenshotError> {
        let screens = Screen::all().unwrap();

        let screen = screens
            .get(0)
            .ok_or(ScreenshotError::NotFound("No screen found".to_string()))?;

        let image = screen.capture().map_err(|e| {
            ScreenshotError::ServiceError(format!("Failed to capture screen: {}", e))
        })?;

        image
            .save(format!(
                "/home/guuri11/Documents/dev/isi/static/{}",
                filename
            ))
            .map_err(|e| ScreenshotError::ServiceError(format!("Failed to save image: {}", e)))?;

        Ok(())
    }
}
