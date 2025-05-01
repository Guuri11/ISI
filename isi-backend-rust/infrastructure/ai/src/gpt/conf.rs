use reqwest::Client;
use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ChatMessage {
    pub role: String,
    pub content: String,
}

#[derive(Debug, Clone)]
pub struct GPTClient {
    client: Client,
    api_key: String,
    base_url: String,
}

impl GPTClient {
    pub fn new(api_key: String, base_url: String) -> Self {
        GPTClient {
            client: Client::new(),
            api_key,
            base_url,
        }
    }

    pub async fn send_chat_request(
        &self,
        messages: &[ChatMessage],
    ) -> Result<String, reqwest::Error> {
        let url = format!("{}/chat/completions", self.base_url);

        let request_body = serde_json::json!({
            "model": "gpt-3.5-turbo",
            "messages": messages,
        });

        let response = self
            .client
            .post(&url)
            .header("Authorization", format!("Bearer {}", self.api_key))
            .json(&request_body)
            .send()
            .await?;

        let response_json: serde_json::Value = response.json().await?;
        Ok(response_json["choices"][0]["message"]["content"]
            .as_str()
            .unwrap_or_default()
            .to_string())
    }
}
