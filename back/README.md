# AutoChecker Backend

Django REST Framework API for the AutoChecker vehicle history platform.

## Requirements

- Python 3.11+
- PostgreSQL 14+
- Redis 6+
- (Optional) Docker + Docker Compose

---

## Running with Docker (recommended)

```bash
cd back

# Copy and configure env files
cp .env .env.local   # or edit .env directly

# Start all services (Django, Postgres, Redis, Celery)
docker compose -f local.yml up --build
```

The API will be available at `http://localhost:8000`.

---

## Running locally (without Docker)

### 1. Create and activate a virtual environment

```bash
python -m venv venv
# Windows
venv\Scripts\activate
# macOS/Linux
source venv/bin/activate
```

### 2. Install dependencies

```bash
pip install -r requirements/local.txt
```

### 3. Configure environment variables

Create a `.env` file in the `back/` directory (or export variables directly):

```env
DATABASE_URL=postgres://autochecker:autochecker@localhost:5432/autochecker
CELERY_BROKER_URL=redis://localhost:6379/0
DJANGO_SECRET_KEY=your-secret-key-here
DJANGO_DEBUG=True
DJANGO_SETTINGS_MODULE=config.settings.local
DJANGO_READ_DOT_ENV_FILE=True
USE_DOCKER=no
GEMINI_API_KEY=your-gemini-api-key
```

### 4. Set up the database

Make sure PostgreSQL is running and the database exists:

```bash
createdb autochecker
```

Then run migrations:

```bash
python manage.py migrate
```

### 5. Create a superuser (optional)

```bash
python manage.py createsuperuser
```

### 6. Run the development server

```bash
python manage.py runserver
```

The API will be available at `http://localhost:8000`.

---

## Key endpoints

| Endpoint | Description |
|---|---|
| `POST /api/auth/login/` | JWT login |
| `POST /api/auth/register/` | Register user |
| `POST /api/vehicle/search/` | Search by plate or VIN |
| `POST /api/accident/report/` | Report an accident (multipart) |
| `GET /api/schema/` | OpenAPI schema |
| `GET /api/schema/swagger-ui/` | Swagger UI |

---

## Project structure

```
back/
├── config/          # Django settings (base, local, production)
├── common/          # Django apps (models, admin)
│   ├── vehicles/
│   ├── accidents/
│   ├── checks/
│   ├── history/
│   └── ...
├── api/             # DRF views + serializers
│   ├── vehicles/
│   ├── accidents/
│   └── ...
├── requirements/
│   ├── base.txt
│   └── local.txt
└── local.yml        # Docker Compose for local dev
```

---

## Running Celery (background tasks)

```bash
# In a separate terminal
celery -A config.celery_app worker --loglevel=info

# Beat scheduler (periodic tasks)
celery -A config.celery_app beat --loglevel=info
```
