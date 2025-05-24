## 📁 src/infrastructure

Adapters and connectors.

- `repositories/`: concrete implementation of the contracts defined in `domain/repositories`.
- This is where the logic interacting with APIs, SDKs, storage, etc., goes.
- **Never** business logic here, only pure calls to external services.
