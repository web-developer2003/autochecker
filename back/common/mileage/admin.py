from django.contrib import admin

from common.mileage.models import MileageRecord


@admin.register(MileageRecord)
class MileageRecordAdmin(admin.ModelAdmin):
    list_display = ["id", "vehicle", "date", "mileage_km", "source"]
    search_fields = ["vehicle__vin"]
    list_filter = ["source"]
