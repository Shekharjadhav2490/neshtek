# Neshtek Phase 4 Architecture

## Goal
Evolve Neshtek from a static business website into a technology platform with Neshtek Monitor as the first SaaS product.

## High-level architecture

Website / Neshtek.com
→ Angular Neshtek Monitor dashboard
→ Spring Boot REST API
→ Monitoring scheduler and workers
→ Oracle Database
→ Alerting and incident services
→ Oracle Cloud infrastructure

## Repository structure

- `backend/` — Spring Boot API and monitoring services
- `frontend/` — Angular SaaS dashboard
- `infra/` — deployment and cloud infrastructure
- `docs/` — architecture and product documentation
- existing root HTML/CSS/JS — Neshtek marketing website

## Delivery sequence

1. Backend foundation and database model
2. Monitoring engine and incident lifecycle
3. REST API
4. Angular dashboard
5. Authentication and authorization
6. Alerting
7. Cloud deployment and CI/CD
8. Subscription and monetization
9. AI-assisted monitoring capabilities

## Safety

The existing `main` branch remains the production baseline. Phase 4 work is isolated on `agent/phase4-development` until validated and reviewed.
