# Product Guidelines

## Architectural Principles
- **Hexagonal Architecture (Ports & Adapters):** Maintain a strict separation between core business logic and external concerns (database, broker APIs, UI).
- **Core Decoupling:** The `core` module must remain agnostic of specific technology implementations.

## Testing Strategy
- **Behavior-Driven TDD:** Primary testing should target the core's public ports to ensure full business logic coverage rather than class-level unit tests.
- **Mathematical Validation:** For complex financial calculations (e.g., standard deviation, risk targets), unit tests are required for individual classes to ensure absolute precision.
- **Integration Coverage:** Adapters (e.g., PostgreSQL, CSV) must have dedicated integration tests.

## Development Standards
- **Maintainability & Extensibility:** Prioritize clean, modular code over clever optimizations. The system must be easy to extend with new strategies or asset classes.
- **Self-Documenting Code:** Use expressive naming for variables, methods, and classes to convey intent. Minimize comments unless explaining complex financial "why" logic.

## Operational Guidelines
- **Minimalist Logging:** Focus logging on errors and critical system failures to maintain a high signal-to-noise ratio.
- **Data Integrity:** Strict validation of incoming market data before ingestion or calculation.
