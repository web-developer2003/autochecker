from django.contrib import admin

from common.vehicles.models import SavedVehicle
from common.vehicles.models import Vehicle


@admin.register(Vehicle)
class VehicleAdmin(admin.ModelAdmin):
    list_display = ["id", "make", "model", "year", "vin", "plate", "is_active"]
    search_fields = ["vin", "plate", "make", "model"]
    list_filter = ["make", "year", "is_active"]


@admin.register(SavedVehicle)
class SavedVehicleAdmin(admin.ModelAdmin):
    list_display = ["id", "user", "vehicle", "nickname"]
    search_fields = ["user__username", "vehicle__vin"]
