pub mod application {
    pub mod chat {
        pub mod use_cases {
            pub mod create;
            pub mod delete;
            pub mod get_by;
        }
    }
    pub mod command {
        pub mod use_cases {
            pub mod create;
            pub mod delete;
            pub mod get_by;
            pub mod update;
        }
    }
    pub mod fav {
        pub mod use_cases {
            pub mod create;
            pub mod delete;
            pub mod get_by;
            pub mod update;
        }
    }
    pub mod task {
        pub mod use_cases {
            pub mod execute;
        }
    }
}

pub mod domain {
    pub mod chat {
        pub mod errors;
        pub mod model;
        pub mod repository;
        pub mod use_cases {
            pub mod create;
            pub mod delete;
            pub mod get_by;
        }
    }

    pub mod command {
        pub mod errors;
        pub mod model;
        pub mod repository;
        pub mod value_objets;
        pub mod use_cases {
            pub mod create;
            pub mod delete;
            pub mod get_by;
            pub mod update;
        }
    }

    pub mod fav {
        pub mod errors;
        pub mod model;
        pub mod repository;
        pub mod use_cases {
            pub mod create;
            pub mod delete;
            pub mod get_by;
            pub mod update;
        }
    }

    pub mod task {
        pub mod errors;
        pub mod executor;
        pub mod model;
        pub mod repository;
        pub mod use_cases {
            pub mod execute;
        }
    }

    pub mod errors;
    pub mod identifiable_model;
}

#[cfg(test)]
pub mod tests {
    pub mod mocks {
        pub mod mock_chat_repository;
        pub mod mock_command_repository;
        pub mod mock_fav_repository;
    }
}
