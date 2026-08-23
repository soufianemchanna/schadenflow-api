# ==============================================================================
# SchadenFlow — backend shorthand commands
# Run `make help` to list everything.
# ==============================================================================

.DEFAULT_GOAL := help
.PHONY: help up down restart logs ps clean \
        run build test test-unit test-modularity \
        flyway-info flyway-migrate flyway-clean \
        db-shell minio-console \
        fmt tree

# ---- Config -------------------------------------------------------------
COMPOSE       := docker compose
COMPOSE_FILES := -f docker-compose.yml
MVN           := ./mvnw

# ==============================================================================
# Docker
# ==============================================================================

up: ## Start local infra (Postgres + MinIO) in the background
	$(COMPOSE) $(COMPOSE_FILES) up -d

down: ## Stop and remove containers (keeps volumes/data)
	$(COMPOSE) $(COMPOSE_FILES) down

restart: down up ## Restart infra containers

logs: ## Tail logs from all infra containers
	$(COMPOSE) $(COMPOSE_FILES) logs -f

ps: ## Show status of infra containers
	$(COMPOSE) $(COMPOSE_FILES) ps

clean: ## Stop containers AND wipe volumes (destroys local DB/MinIO data)
	$(COMPOSE) $(COMPOSE_FILES) down -v

# ==============================================================================
# Spring Boot
# ==============================================================================

run: ## Run Spring Boot with the dev profile
	$(MVN) spring-boot:run -Dspring-boot.run.profiles=dev

build: ## Compile and package (skip tests for speed)
	$(MVN) clean package -DskipTests

test: ## Run the full test suite
	$(MVN) test

test-unit: ## Run tests excluding ModularityTests (fast feedback loop)
	$(MVN) test -Dtest='!ModularityTests'

test-modularity: ## Run only Spring Modulith boundary verification
	$(MVN) test -Dtest=ModularityTests

# ==============================================================================
# Database (Flyway)
# ==============================================================================

flyway-info: ## Show applied/pending Flyway migrations
	$(MVN) flyway:info

flyway-migrate: ## Apply pending Flyway migrations
	$(MVN) flyway:migrate

flyway-clean: ## Drop all objects in the configured schema (DEV ONLY, destructive)
	$(MVN) flyway:clean

db-shell: ## Open a psql shell into the local Postgres container
	$(COMPOSE) $(COMPOSE_FILES) exec postgres psql -U $${POSTGRES_USER:-postgres} -d $${POSTGRES_DB:-schadenflowdb}

# ==============================================================================
# MinIO
# ==============================================================================

minio-console: ## Print the MinIO console URL (default local port)
	@echo "MinIO console: http://localhost:9001"

# ==============================================================================
# Misc
# ==============================================================================

fmt: ## Format code (if you wire up spotless/google-java-format later)
	$(MVN) spotless:apply

tree: ## Print module structure (top 3 levels), ignoring build artifacts
	@find src/main/java -maxdepth 4 -type d | grep -v target | sort

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
	awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-18s\033[0m %s\n", $$1, $$2}'