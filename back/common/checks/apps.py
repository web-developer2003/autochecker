from django.apps import AppConfig


class ChecksConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "common.checks"
    verbose_name = "Vehicle Checks"
    label = "checks"
