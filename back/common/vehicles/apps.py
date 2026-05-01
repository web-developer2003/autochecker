from django.apps import AppConfig


class VehiclesConfig(AppConfig):
    default_auto_field = "django.db.models.BigAutoField"
    name = "common.vehicles"
    verbose_name = "Vehicles"
    label = "vehicles"
