pub mod config {
    pub mod app_state;
    pub mod settings;
    pub mod tracing;
}

pub mod router {
    pub mod core;
    pub mod exceptions;
    pub mod command {
        pub mod controller;
        pub mod dto;
    }
}
