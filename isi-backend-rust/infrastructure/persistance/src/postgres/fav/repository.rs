use async_trait::async_trait;
use business::domain::{
    errors::RepositoryError,
    fav::{model::Fav, repository::FavRepository as CoreFavRepository},
};
use sqlx::{Pool, Postgres};
use uuid::Uuid;

use super::data_model::FavDb;

pub struct FavRepository {
    pool: Pool<Postgres>,
}

impl FavRepository {
    pub fn new(pool: Pool<Postgres>) -> Self {
        FavRepository { pool }
    }
}

#[async_trait]
impl CoreFavRepository for FavRepository {
    async fn save(&self, fav: &Fav) -> Result<(), RepositoryError> {
        let fav_db = FavDb::from(fav);

        sqlx::query!(
            r#"
            INSERT INTO favs (id, name, created_at, updated_at, deleted, deleted_at)
            VALUES ($1, $2, $3, $4, $5, $6)
            "#,
            fav_db.id,
            fav_db.name,
            fav_db.created_at,
            fav_db.updated_at,
            fav_db.deleted,
            fav_db.deleted_at,
        )
        .execute(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?;

        Ok(())
    }

    async fn find_all(&self) -> Result<Vec<Fav>, RepositoryError> {
        let results = sqlx::query_as!(
            FavDb,
            r#"
            SELECT id, name, created_at, updated_at, deleted, deleted_at
            FROM favs
            "#,
        )
        .fetch_all(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?;

        results
            .into_iter()
            .map(|fav| Fav::try_from(&fav).map_err(RepositoryError::Persistence))
            .collect()
    }

    async fn find_by_id(&self, id: &Uuid) -> Result<Fav, RepositoryError> {
        sqlx::query_as!(
            FavDb,
            r#"
            SELECT id, name, created_at, updated_at, deleted, deleted_at
            FROM favs
            WHERE id = $1
            "#,
            id
        )
        .fetch_optional(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?
        .ok_or_else(|| RepositoryError::NotFound(format!("Fav with id {} not found", id)))
        .and_then(|fav_db| Fav::try_from(&fav_db).map_err(RepositoryError::Persistence))
    }

    async fn find_by_name(&self, name: &str) -> Result<Fav, RepositoryError> {
        sqlx::query_as!(
            FavDb,
            r#"
            SELECT id, name, created_at, updated_at, deleted, deleted_at
            FROM favs
            WHERE name = $1
            "#,
            name
        )
        .fetch_optional(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?
        .ok_or_else(|| RepositoryError::NotFound(format!("Fav with name {} not found", name)))
        .and_then(|fav_db| Fav::try_from(&fav_db).map_err(RepositoryError::Persistence))
    }

    async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError> {
        let affected = sqlx::query!(
            r#"
            DELETE FROM favs
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
                "Fav with id {} not found",
                id
            )));
        }

        Ok(())
    }

    async fn update(&self, fav: &Fav) -> Result<(), RepositoryError> {
        let fav_db = FavDb::from(fav);

        let result = sqlx::query!(
            r#"
            UPDATE favs
            SET name = $2, created_at = $3, updated_at = $4, deleted = $5, deleted_at = $6
            WHERE id = $1
            "#,
            fav_db.id,
            fav_db.name,
            fav_db.created_at,
            fav_db.updated_at,
            fav_db.deleted,
            fav_db.deleted_at,
        )
        .execute(&self.pool)
        .await;

        match result {
            Ok(affected) => {
                if affected.rows_affected() == 0 {
                    return Err(RepositoryError::NotFound(format!(
                        "Fav with id {} not found",
                        fav.id()
                    )));
                }
                Ok(())
            }
            Err(e) => Err(RepositoryError::DatabaseError(e.to_string())),
        }
    }
}
