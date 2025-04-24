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
}

impl From<&Fav> for FavDb {
    fn from(fav: &Fav) -> Self {
        FavDb {
            id: fav.id(),
            name: fav.name().clone(),
            created_at: fav.created_at(),
            updated_at: fav.updated_at(),
        }
    }
}

impl TryFrom<&FavDb> for Fav {
    type Error = String;

    fn try_from(db: &FavDb) -> Result<Self, Self::Error> {
        Ok(Fav::from_repository(
            db.id,
            db.name.clone(),
            db.created_at,
            db.updated_at,
        ))
    }
}

impl TryFrom<FavDb> for Fav {
    type Error = String;

    fn try_from(db: FavDb) -> Result<Self, Self::Error> {
        Fav::try_from(&db)
    }
}
