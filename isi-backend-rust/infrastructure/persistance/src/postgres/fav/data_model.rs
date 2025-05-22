use business::domain::fav::model::Fav;
use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};
use sqlx::prelude::FromRow;
use uuid::Uuid;

#[derive(Debug, Serialize, Deserialize, FromRow)]
pub struct FavDb {
    pub id: Uuid,
    pub name: String,
    pub created_at: NaiveDateTime,
    pub updated_at: NaiveDateTime,
    pub deleted: bool,
    pub deleted_at: Option<NaiveDateTime>,
}

impl From<&Fav> for FavDb {
    fn from(fav: &Fav) -> Self {
        FavDb {
            id: fav.id(),
            created_at: fav.created_at(),
            updated_at: fav.updated_at(),
            deleted: fav.deleted(),
            deleted_at: fav.deleted_at(),
            name: fav.name().to_owned(),
        }
    }
}

impl TryFrom<&FavDb> for Fav {
    type Error = String;

    fn try_from(db: &FavDb) -> Result<Self, Self::Error> {
        Ok(Fav::from_repository(
            db.id,
            db.created_at,
            db.updated_at,
            db.deleted,
            db.deleted_at,
            db.name.clone(),
        ))
    }
}

impl TryFrom<FavDb> for Fav {
    type Error = String;

    fn try_from(db: FavDb) -> Result<Self, Self::Error> {
        Fav::try_from(&db)
    }
}
