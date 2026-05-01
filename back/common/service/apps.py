from django.apps import AppConfig


class ServiceConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "common.service"
    verbose_name = "Service Records"
    label = "service"
