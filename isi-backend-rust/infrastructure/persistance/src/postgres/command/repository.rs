use async_trait::async_trait;
use business::domain::{
    command::{model::Command, repository::CommandRepository as CoreCommandRepository},
    errors::RepositoryError,
};
use sqlx::{Pool, Postgres};
use uuid::Uuid;

use super::data_model::CommandDb;

pub struct CommandRepository {
    pool: Pool<Postgres>,
}

impl CommandRepository {
    pub fn new(pool: Pool<Postgres>) -> Self {
        CommandRepository { pool }
    }
}

#[async_trait]
impl CoreCommandRepository for CommandRepository {
    async fn save(&self, command: &Command) -> Result<(), RepositoryError> {
        let command_db = CommandDb::from(command);

        sqlx::query!(
            r#"
            INSERT INTO commands (id, content, log, message_type, chat_id, fav_name, created_at, updated_at, deleted, deleted_at)
            VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
            "#,
            command_db.id,
            command_db.content,
            command_db.log,
            command_db.message_type as _,
            command_db.chat_id,
            command_db.fav_name,
            command_db.created_at,
            command_db.updated_at,
            command_db.deleted,
            command_db.deleted_at,
        )
        .execute(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?;

        Ok(())
    }

    async fn find_all(&self) -> Result<Vec<Command>, RepositoryError> {
        let results = sqlx::query_as!(
            CommandDb,
            r#"
            SELECT id, log, content, message_type as "message_type: _", chat_id, fav_name, created_at, updated_at, deleted, deleted_at
            FROM commands
            "#,
        )
        .fetch_all(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?;

        results
            .into_iter()
            .map(|command_db| Command::try_from(&command_db).map_err(RepositoryError::Persistence))
            .collect()
    }

    async fn find_by_id(&self, id: &Uuid) -> Result<Command, RepositoryError> {
        sqlx::query_as!(
            CommandDb,
            r#"
            SELECT id, log, content, message_type as "message_type: _", chat_id, fav_name, created_at, updated_at, deleted, deleted_at
            FROM commands
            WHERE id = $1
            "#,
            id
        )
        .fetch_optional(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?
        .ok_or_else(|| RepositoryError::NotFound(format!("Command with id {} not found", id)))
        .and_then(|command_db| Command::try_from(&command_db).map_err(RepositoryError::Persistence))
    }

    async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError> {
        let affected = sqlx::query!(
            r#"
            DELETE FROM commands
            WHERE id = $1
            "#,
            id
        )
        .execute(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?
        .rows_affected();

        if affected == 0 {
            return Err(RepositoryError::NotFound(format!(
                "Command with id {} not found",
                id
            )));
        }

        Ok(())
    }

    async fn update(&self, command: &Command) -> Result<(), RepositoryError> {
        let command_db = CommandDb::from(command);

        let result = sqlx::query!(
            r#"
            UPDATE commands
            SET log = $2, content = $3, message_type = $4, chat_id = $5, fav_name = $6, created_at = $7, updated_at = $8, deleted = $9, deleted_at = $10
            WHERE id = $1
            "#,
            command_db.id,
            command_db.log,
            command_db.content,
            command_db.message_type as _,
            command_db.chat_id,
            command_db.fav_name,
            command_db.created_at,
            command_db.updated_at,
            command_db.deleted,
            command_db.deleted_at,
        )
        .execute(&self.pool)
        .await;

        match result {
            Ok(affected) => {
                if affected.rows_affected() == 0 {
                    return Err(RepositoryError::NotFound(format!(
                        "Command with id {} not found",
                        command.id()
                    )));
                }
                Ok(())
            }
            Err(e) => Err(RepositoryError::DatabaseError(e.to_string())),
        }
    }
}
