# Phase 4C.1 — OCI Staging Deployment

## Target
Deploy Neshtek Monitor as a private/staging application on an Oracle Cloud Compute VM using Docker Compose and an Oracle database.

## Architecture

Internet/VPN -> OCI Compute VM -> Nginx -> Angular frontend -> `/api/*` -> Spring Boot -> Oracle Database

## Prerequisites

- OCI tenancy with permission to create/use a Compute VM and networking resources
- Ubuntu 24.04 LTS or Oracle Linux 9 VM
- Public or private DNS name for staging
- Oracle Autonomous Database/Oracle Database connection details
- Git access to this repository

## Recommended VM starting point

For a staging MVP, start with a small Ampere/ARM or AMD VM appropriate to the OCI free/low-cost resources available in the tenancy. Size up after observing CPU, memory and monitoring workload.

## Deployment steps

1. Create an OCI VCN/subnet and Compute VM.
2. Allow inbound TCP 80 for staging. Allow TCP 443 when TLS is configured. Keep port 8080 private; Nginx is the public application entry point.
3. Install Docker Engine and Docker Compose plugin.
4. Clone the repository and checkout `phase4c/1-oci-deployment`.
5. Create `.env` on the VM from `.env.example` and enter the Oracle connection values. Do not commit this file.
6. Run:

```bash
docker compose -f docker-compose.staging.yml up -d --build
```

7. Check containers:

```bash
docker compose -f docker-compose.staging.yml ps
docker compose -f docker-compose.staging.yml logs --tail=100 backend
```

8. Verify the application through the VM/staging hostname.
9. Verify `/actuator/health` through the backend/private path before exposing a public health endpoint.
10. Create a monitor and test 200, 502 and recovery behavior.

## Database

The backend is configured to validate the schema rather than create it automatically. Flyway migrations must be allowed to run against the staging database. Use a dedicated application schema/user and least-privilege database grants.

## Security before public launch

- Do not expose port 8080 publicly.
- Do not commit `.env` or credentials.
- Configure TLS/HTTPS.
- Add authentication and authorization.
- Configure secret management (OCI Vault is preferred for production).
- Restrict Oracle network access to the application subnet where possible.
- Add backups and monitoring.

## DNS

Create a staging hostname such as `staging-monitor.neshtek.com` pointing to the OCI load balancer/public IP used for the staging entry point. Use HTTPS before sharing the URL externally.

## Rollback

Keep the previous image/tag available. For a simple staging deployment, checkout the previous commit and rebuild; for production, move to immutable image tags and a controlled deployment pipeline.
