use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Deserialize, Serialize)]
pub enum TaskType {
    Refactor,
    Weather,
    OpenApp,
    BookmarkRecommendations,
    OtherTopics,
    LinkedinOfferRejection,
}

#[derive(Debug, Deserialize)]
pub struct AgentResponse {
    pub task: TaskType,
    pub parameters: serde_json::Value,
}

#[derive(Debug, Clone)]
pub struct Task {
    pub id: String,
    pub task_type: TaskType,
    pub payload: String,
}
