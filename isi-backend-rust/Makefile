# Isi - Smart AI Assistant Makefile
#
# Targets principales:
# - build: Compila el proyecto en modo debug
# - run: Ejecuta el proyecto
# - release: Compila el proyecto en modo release
# - test: Ejecuta todos los tests
# - check: Ejecuta clippy y check de formato
# - clean: Limpia archivos generados
# - docs: Genera documentación
# - run-example: Ejecuta la aplicación de ejemplo

# Variables
WORKSPACE_ROOT := $(shell pwd)
CARGO := cargo
DOCKER := docker
DOCKER_COMPOSE := docker compose
RUSTFMT := rustfmt
CLIPPY := clippy
DOMAIN_CRATE := domain
INFRA_CRATE := infra
EXAMPLE_APP := core-usage-example-client
TARGET_CRATE=
REST_API_ENTRY_POINT := rest-api
PLAYGROUND_ENTRY_POINT := playground

# SQLx modo offline/online
SQLX_ONLINE_ENV := SQLX_OFFLINE=false
SQLX_OFFLINE_ENV := SQLX_OFFLINE=true

# Colores para salida
GREEN := \033[0;32m
YELLOW := \033[0;33m
RED := \033[0;31m
NC := \033[0m # No Color



test: test/domain test/infra

test/domain:
test/infra:
test/%:
	@echo "${GREEN}Ejecutando tests $* ...${NC}"
	$(CARGO) test -p $*

domain/testo:
%/testo:
	@echo "${GREEN}Ejecutando tests $* ...${NC}"
	$(CARGO) test  -p $*





.PHONY: all build release check format clippy test test-proptest bench clean docs run-example db-up db-down sqlx-online sqlx-offline test-jamon

# Target por defecto
all: check build test

# Compilación
build:
	@echo "${GREEN}Compilando proyecto en modo debug...${NC}"
	$(CARGO) build

release:
	@echo "${GREEN}Compilando proyecto en modo release...${NC}"
	$(CARGO) build --release

run/rest:
	@echo "${GREEN}Ejecutando proyecto via REST API...${NC}"
	$(CARGO) run --package $(REST_API_ENTRY_POINT) --bin $(REST_API_ENTRY_POINT)

run/playground:
	@echo "${GREEN}Ejecutando proyecto via Playground...${NC}"
	$(CARGO) run --package $(PLAYGROUND_ENTRY_POINT) --bin $(PLAYGROUND_ENTRY_POINT)

# Verificación de código

format:
	@echo "${YELLOW}Verificando formato de código...${NC}"
	$(CARGO) fmt --all -- --check

format-fix:
	@echo "${YELLOW}Aplicando formato a todo el código...${NC}"
	$(CARGO) fmt --all

clippy:
	@echo "${YELLOW}Ejecutando clippy (linter)...${NC}"
	$(CARGO) clippy -- -D warnings

crate/test:
	@echo "${GREEN}Ejecutando tests del crate $(TARGET_CRATE)...${NC}"
	echo "Crate: $(TARGET_CRATE)"
	$(CARGO) test -p $(TARGET_CRATE)

domain:
	$(eval TARGET_CRATE := $(DOMAIN_CRATE))

domain/test: domain
	$(eval TARGET_CRATE := $(DOMAIN_CRATE))
	$(MAKE) crate/test TARGET_CRATE=$(DOMAIN_CRATE)

domain/fmt:
	@echo "${YELLOW}Verificando formato del crate domain...${NC}"
	$(CARGO) fmt --all -- --check -p $(DOMAIN_CRATE)


domain/clippy:
	@echo "${YELLOW}Ejecutando clippy (linter) en el crate domain...${NC}"
	$(CARGO) clippy -- -D warnings -p $(DOMAIN_CRATE)


# Testing
test:
	@echo "${GREEN}Ejecutando todos los tests...${NC}"
	$(CARGO) test

test-domain:
	@echo "${GREEN}Ejecutando tests del crate domain...${NC}"
	$(CARGO) test -p $(DOMAIN_CRATE)

test-infra:
	@echo "${GREEN}Ejecutando tests del crate infra...${NC}"
	$(CARGO) test -p $(INFRA_CRATE)

test-infra-online:
	@echo "${GREEN}Ejecutando tests del crate infra en modo ONLINE...${NC}"
	$(SQLX_ONLINE_ENV) $(CARGO) test -p $(INFRA_CRATE)

test-truck-repo:
	@echo "${GREEN}Ejecutando tests del repositorio de camiones...${NC}"
	$(SQLX_ONLINE_ENV) $(CARGO) test -p $(INFRA_CRATE) --test truck_repository_test

test-proptest:
	@echo "${GREEN}Ejecutando tests basados en propiedades...${NC}"
	$(CARGO) test --test proptest

test-coverage:
	@echo "${GREEN}Generando informe de cobertura de tests...${NC}"
	$(CARGO) install cargo-tarpaulin --locked || true
	$(CARGO) tarpaulin --out Xml --output-dir ./coverage

bench:
	@echo "${YELLOW}Ejecutando benchmarks...${NC}"
	$(CARGO) bench

# Limpieza
clean:
	@echo "${RED}Limpiando archivos generados...${NC}"
	$(CARGO) clean

# Documentación
docs:
	@echo "${GREEN}Generando documentación...${NC}"
	$(CARGO) doc --no-deps --document-private-items --open

# Ejecución de aplicaciones
run-example:
	@echo "${GREEN}Ejecutando aplicación de ejemplo...${NC}"
	$(CARGO) run -p $(EXAMPLE_APP)

# Infraestructura para desarrollo local
db-up:
	@echo "${GREEN}Iniciando base de datos PostgreSQL en contenedor...${NC}"
	$(DOCKER) run --name postgres-isi -e POSTGRES_PASSWORD=postgres \
		-e POSTGRES_USER=postgres -e POSTGRES_DB=isi \
		-p 5432:5432 -d postgres:14

db-down:
	@echo "${RED}Deteniendo y eliminando contenedor de base de datos...${NC}"
	$(DOCKER) stop postgres-isi || true
	$(DOCKER) rm postgres-isi || true

# Configuración inicial para desarrollo
setup-dev:
	@echo "${GREEN}Configurando entorno de desarrollo...${NC}"
	rustup component add rustfmt clippy
	cargo install cargo-audit cargo-outdated sqlx-cli

# Verificación de seguridad
security-check:
	@echo "${YELLOW}Ejecutando auditoría de seguridad...${NC}"
	cargo audit

docker-compose/up:
	@echo "${GREEN}Iniciando contenedores con docker-compose...${NC}"
	cd $(WORKSPACE_ROOT) && $(DOCKER_COMPOSE) up -d

docker-compose/down:
	@echo "${GREEN}Deteniendo contenedores con docker-compose...${NC}"
	cd $(WORKSPACE_ROOT) && $(DOCKER_COMPOSE) down

# SQLx en modo online/offline
sqlx-online:
	@echo "${YELLOW}Cambiando SQLx a modo ONLINE...${NC}"
	@echo 'SQLX_OFFLINE = "false"' > .cargo/config.toml
	@echo "${GREEN}SQLx ahora está en modo ONLINE. Las consultas serán verificadas contra una base de datos real.${NC}"

sqlx-offline:
	@echo "${YELLOW}Cambiando SQLx a modo OFFLINE...${NC}"
	@echo '[env]\nSQLX_OFFLINE = "true"' > .cargo/config.toml
	@echo "${GREEN}SQLx ahora está en modo OFFLINE. Las consultas serán verificadas contra los metadatos guardados.${NC}"

sqlx/prepare: docker-compose/up
	@echo "${GREEN}Preparando caché de sqlx...${NC}"
	cd $(WORKSPACE_ROOT) && $(SQLX_ONLINE_ENV) $(CARGO) sqlx prepare --workspace --database-url postgres://isi:password@localhost:5432/isi

sqlx/check: docker-compose/up
	@echo "${GREEN}Verificando caché de sqlx...${NC}"
	cd $(WORKSPACE_ROOT) && $(SQLX_ONLINE_ENV) $(CARGO) sqlx prepare --workspace --check --database-url postgres://isi:password@localhost:5432/isi

sqlx/migrate: docker-compose/up
	@echo "${GREEN}Ejecutando migraciones de base de datos...${NC}"
	cd $(WORKSPACE_ROOT) && $(SQLX_ONLINE_ENV) $(CARGO) sqlx migrate run --source infrastructure/persistance/src/postgres/migrations --database-url postgres://isi:password@localhost:5432/isi

# Información de ayuda
help:
	@echo "Targets disponibles:"
	@echo "  ${GREEN}build${NC}         - Compila el proyecto en modo debug"
	@echo "  ${GREEN}run${NC}           - Ejecuta el proyecto"
	@echo "  ${GREEN}release${NC}       - Compila el proyecto en modo release"
	@echo "  ${GREEN}check${NC}         - Ejecuta verificaciones de formato y linting"
	@echo "  ${GREEN}format${NC}        - Verifica el formato del código"
	@echo "  ${GREEN}format-fix${NC}    - Aplica formato a todo el código"
	@echo "  ${GREEN}clippy${NC}        - Ejecuta el linter Clippy"
	@echo "  ${GREEN}test${NC}          - Ejecuta todos los tests"
	@echo "  ${GREEN}test-domain${NC}   - Ejecuta los tests del crate domain"
	@echo "  ${GREEN}test-infra${NC}    - Ejecuta los tests del crate infra"
	@echo "  ${GREEN}test-infra-online${NC} - Ejecuta los tests del crate infra en modo ONLINE"
	@echo "  ${GREEN}test-truck-repo${NC}- Ejecuta tests específicos del repositorio de camiones"
	@echo "  ${GREEN}test-proptest${NC} - Ejecuta los tests basados en propiedades"
	@echo "  ${GREEN}test-coverage${NC} - Genera informe de cobertura de tests"
	@echo "  ${GREEN}bench${NC}         - Ejecuta benchmarks"
	@echo "  ${GREEN}clean${NC}         - Limpia archivos generados"
	@echo "  ${GREEN}docs${NC}          - Genera documentación"
	@echo "  ${GREEN}run-example${NC}   - Ejecuta la aplicación de ejemplo"
	@echo "  ${GREEN}db-up${NC}         - Inicia contenedor de PostgreSQL para desarrollo"
	@echo "  ${GREEN}db-down${NC}       - Detiene y elimina el contenedor de PostgreSQL"
	@echo "  ${GREEN}setup-dev${NC}     - Configura herramientas para desarrollo"
	@echo "  ${GREEN}security-check${NC}- Ejecuta auditoría de seguridad"
	@echo "  ${GREEN}sqlx-online${NC}   - Cambia SQLx a modo online (consultas verificadas contra DB real)"
	@echo "  ${GREEN}sqlx-offline${NC}  - Cambia SQLx a modo offline (consultas verificadas contra metadatos)"
	@echo "  ${GREEN}sqlx/prepare${NC}  - Genera metadatos de consultas para modo offline"
	@echo "  ${GREEN}sqlx/check${NC}    - Verifica consultas contra metadatos existentes"
	@echo "  ${GREEN}sqlx/migrate${NC}  - Ejecuta migraciones de base de datos"