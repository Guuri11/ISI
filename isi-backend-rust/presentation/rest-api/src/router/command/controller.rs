use axum::extract::{Path, State};
use axum::http::StatusCode;
use axum::response::IntoResponse;
use axum::routing::{delete, get, post};
use axum::{Json, Router};
use business::domain::command::value_objets::{ChatId, MessageType};
use business::domain::task::model::TaskType;
use utoipa::OpenApi;
use uuid::Uuid;

use crate::config::app_state::AppState;
use crate::router::command::dto::{CommandInputDTO, CommandOutputDTO};
use crate::router::exceptions::RestError;

#[derive(OpenApi)]
#[openapi(
    paths(
        register_command_handler,
        delete_command_handler,
        get_commands_handler,
    ),
    components(schemas(CommandInputDTO, CommandOutputDTO)),
    tags(
        (name = "Commands", description = "Command management endpoints")
    ),
)]
pub struct CommandApiDoc;

pub fn command_routes() -> Router<AppState> {
    Router::new()
        .route("/", post(register_command_handler))
        .route("/", get(get_commands_handler))
        .route("/{id}", delete(delete_command_handler))
}

#[utoipa::path(
    get,
    path = "/commands",
    responses((status = 200, description = "Commands found", body = Vec<CommandOutputDTO>)),
    tag = "Commands"
)]
async fn get_commands_handler(
    State(app_state): State<AppState>,
) -> Result<impl IntoResponse, RestError> {
    let commands = app_state.command_service.get_commands().await?;
    Ok(Json(commands).into_response())
}

#[utoipa::path(
    post,
    path = "/commands",
    responses((status = 200, description = "Command registered", body = CommandOutputDTO)),
    request_body = CommandInputDTO,
    tag = "Commands"
)]
async fn register_command_handler(
    State(app_state): State<AppState>,
    Json(payload): Json<CommandInputDTO>,
) -> Result<impl IntoResponse, RestError> {
    let command = app_state
        .command_service
        .register_command(
            payload.request,
            ChatId::new(payload.chat_id),
            MessageType::from(payload.message_type),
            payload.task.map(|task_dto| TaskType::from(task_dto)),
        )
        .await?;

    Ok(Json(CommandOutputDTO::from_command(&command)))
}

#[utoipa::path(
    delete,
    path = "/commands/{id}",
    responses((status = 204, description = "Command deleted")),
    params(
        ("id", description = "Command ID", example = "550e8400-e29b-41d4-a716-446655440000")
    ),
    tag = "Commands"
)]
async fn delete_command_handler(
    State(app_state): State<AppState>,
    Path(id): Path<Uuid>,
) -> Result<impl IntoResponse, RestError> {
    app_state.command_service.delete_command(&id).await?;
    Ok(StatusCode::NO_CONTENT.into_response())
}
