from django.contrib import admin

from common.checks.models import VehicleCheck


@admin.register(VehicleCheck)
class VehicleCheckAdmin(admin.ModelAdmin):
    list_display = ["id", "vehicle", "user", "accidents_count", "accidents_status", "mileage_status", "legal_status", "created_at"]
    list_filter = ["accidents_status", "mileage_status", "legal_status"]
    search_fields = ["vehicle__vin", "user__username"]
