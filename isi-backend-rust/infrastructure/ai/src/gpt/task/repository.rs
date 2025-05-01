use async_trait::async_trait;
use business::domain::{
    errors::RepositoryError,
    task::{model::AgentResponse, repository::TaskRepository as CoreTaskRepository},
};

use crate::gpt::conf::{ChatMessage, GPTClient};

pub struct TaskRepository {
    ai_client: GPTClient,
}

impl TaskRepository {
    pub fn new(ai_client: GPTClient) -> Self {
        TaskRepository { ai_client }
    }
}

#[async_trait]
impl CoreTaskRepository for TaskRepository {
    async fn get_task_type(&self, request: &String) -> Result<AgentResponse, RepositoryError> {
        let prompt = format!(
            r#"
        Dado el mensaje del usuario, responde en JSON cuál es la tarea y sus parámetros. 
        Tareas posibles:
        - Refactor: si pide refactorizar algo. Parámetros: code, requirements
        - Weather: si pregunta por el tiempo. Parámetros: location (opcional)
        - OpenApp: si pide abrir una aplicación. Parámetros: app_name
        - BookmarkRecommendations: si pide recomendaciones de marcadores. Parámetros: topic
        - LinkedinOfferRejection: si pide rechazar una oferta de trabajo en LinkedIn. Parámetros: offer
        - OtherTopics: Cualquier tema general que no aplique al resto de posibles tareas
        - Unknown: si no se entiende.
        
        Mensaje del usuario: "{}"

        Ejemplo de salida:
        {{"task": "Weather", "parameters": {{"location": "Madrid"}}}}
        {{"task": "Refactor", "parameters": {{"code": "println!(Hola)", "requirements": "Arregla el código"}}}}
        {{"task": "OpenApp", "parameters": {{"app_name": "Spotify"}}}}
        {{"task": "BookmarkRecommendations", "parameters": {{"topic": "programming"}}}}
        {{"task": "LinkedinOfferRejection", "parameters": {{"offer": "job offer"}}}}
        {{"task": "OtherTopics", "parameters": {{"topic": "general"}}}}
        {{"task": "Unknown", "parameters": {{}}}}

        "#,
            request
        );

        let message = ChatMessage {
            role: "user".to_string(),
            content: prompt.to_string(),
        };

        let response = self
            .ai_client
            .send_chat_request(&vec![message])
            .await
            .map_err(|err| RepositoryError::NotFound(err.to_string()))?;

        let parsed: AgentResponse = serde_json::from_str(&response)
            .map_err(|err| RepositoryError::NotFound(err.to_string()))?;

        Ok(parsed)
    }

    async fn code_assistant(
        &self,
        code: String,
        requirements: String,
    ) -> Result<String, RepositoryError> {
        let prompt = format!(
            r#"
        Dado el código y los requisitos (si está vacio limpia el código), refactoriza el código para que cumpla con los requisitos.
        
        Código:
        {}

        Requisitos:
        {}

        "#,
            code, requirements
        );

        let message = ChatMessage {
            role: "user".to_string(),
            content: prompt.to_string(),
        };

        let response = self
            .ai_client
            .send_chat_request(&vec![message])
            .await
            .map_err(|err| RepositoryError::NotFound(err.to_string()))?;

        Ok(response)
    }
}
