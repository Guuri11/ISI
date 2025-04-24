pub mod postgres {
    pub mod db;
    pub mod fav {
        pub mod data_model;
        pub mod repository;
    }
    pub mod chat {
        pub mod data_model;
        pub mod repository;
    }
    pub mod command {
        pub mod data_model;
        pub mod repository;
    }
}
