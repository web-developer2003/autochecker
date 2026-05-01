from django.apps import AppConfig


class HistoryConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "common.history"
    verbose_name = "Check History"
    label = "history"
