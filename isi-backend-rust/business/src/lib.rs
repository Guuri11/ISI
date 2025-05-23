pub mod application {
    pub mod screenshot {
        pub mod service;
    }

    pub mod clipboard {
        pub mod service;
    }

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
        pub mod executor;
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

    pub mod screenshot {
        pub mod errors;
        pub mod service;
    }

    pub mod clipboard {
        pub mod errors;
        pub mod service;
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
