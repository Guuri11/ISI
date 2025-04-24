pub mod application {
    pub mod chat {
        pub mod service;
    }
    pub mod command {
        pub mod service;
    }
    pub mod fav {
        pub mod service;
    }
}

pub mod domain {
    pub mod chat {
        pub mod errors;
        pub mod model;
        pub mod repository;
        pub mod use_cases;
    }

    pub mod command {
        pub mod errors;
        pub mod model;
        pub mod repository;
        pub mod use_cases;
        pub mod value_objets;
    }

    pub mod fav {
        pub mod errors;
        pub mod model;
        pub mod repository;
        pub mod use_cases;
    }

    pub mod errors;
}
