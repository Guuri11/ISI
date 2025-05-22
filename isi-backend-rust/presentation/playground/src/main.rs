#[tokio::main]
async fn main() {
    if let Err(err) = run().await {
        tracing::error!("Error al ejecutar el servidor: {}", err);
    }
}

async fn run() -> Result<(), Box<dyn std::error::Error>> {
    println!("Iniciando el servidor...");
    Ok(())
}
