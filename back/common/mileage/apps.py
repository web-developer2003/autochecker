from django.apps import AppConfig


class MileageConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "common.mileage"
    verbose_name = "Mileage"
    label = "mileage"
