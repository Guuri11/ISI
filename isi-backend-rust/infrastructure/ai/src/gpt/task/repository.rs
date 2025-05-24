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
        - BookmarkRecommendations: si pide recomendaciones de marcadores, o guardar cualquier recurso. Parámetros: topic
        - LinkedinOfferRejection: si pide rechazar una oferta de trabajo en LinkedIn. Parámetros: offer
        - OtherTopics: No aplica al resto de tareas posibles. Simplemente responde a la preguntar del usuario. Parámetros: answer
        - Unknown: si no se entiende.
        
        Mensaje del usuario: "{}"

        Ejemplo de salida:
        {{"task": "Weather", "parameters": {{"location": "Madrid"}}}}
        {{"task": "Refactor", "parameters": {{"code": "println!(Hola)", "requirements": "Arregla el código"}}}}
        {{"task": "OpenApp", "parameters": {{"app_name": "Spotify"}}}}
        {{"task": "BookmarkRecommendations", "parameters": {{"topic": "programming"}}}}
        {{"task": "LinkedinOfferRejection", "parameters": {{"offer": "job offer"}}}}
        {{"task": "OtherTopics", "parameters": {{"answer": "RESPUESTA_A_LA_PREGUNTA_DEL_USUARIO"}}}}
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

        println!("Parsed: {:?}", parsed);

        Ok(parsed)
    }

    async fn code_assistant(
        &self,
        code: String,
        requirements: String,
    ) -> Result<String, RepositoryError> {
        let prompt = format!(
            r#"
        Dado el código y los requisitos (si no hubiera tan solo aplica mejoras de legibilidad), refactoriza el código para que cumpla con los requisitos.
        
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

    async fn other_topics(&self, topic: String) -> Result<String, RepositoryError> {
        let prompt = format!(
            r#"
        Dado el tema, dame una respuesta.
        
        Tema:
        {}

        "#,
            topic
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

    async fn linkedin_offer_rejection(&self, offer: String) -> Result<String, RepositoryError> {
        let prompt = format!(
            r#"
        Dado el perfil de un software engineer, contesta a la oferta de trabajo de forma profesional,
        rechazando la oferta pero dejando abierta la posibilidad a futuras colaboraciones. 
        Responde en inglés o castellano según lo que te diga el usuario.
        Oferta:
        {}
        "#,
            offer
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
