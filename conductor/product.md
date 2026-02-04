# Initial Concept
Personal trading engine for quantitative finance.

# Product Definition

## Target Audience
- **Primary User:** Self (Personal quantitative trading).
- **Goal:** To manage and trade personal capital effectively using automated strategies.

## Core Domain
- **Asset Class:** Futures (exclusively).
- **Primary Focus:**
    1. **Backtesting:** Robust framework to validate trend-following strategies against historical data.
    2. **Data Management:** Reliable ingestion and storage of historical and real-time market data.
    3. **Live Execution:** Automated trading system for real-time order execution.

## Strategic Roadmap (Next 3-6 Months)
1.  **Phase 1: Backtesting Engine Validation**
    -   Implement and refine trend-following strategies.
    -   Verify performance using existing CSV/historical data adapters.
2.  **Phase 2: Data Infrastructure**
    -   Implement a PostgreSQL adapter for robust data persistence.
    -   Build data ingestion pipelines to populate the database with historical futures data.
3.  **Phase 3: Live Trading MVP**
    -   Develop the real-time data processing loop.
    -   Integrate with a broker API for automated execution.

## Key Technologies
-   **Language:** Java (Spring Boot)
-   **Database:** PostgreSQL (for time-series and trade data)
-   **Build Tool:** Gradle
