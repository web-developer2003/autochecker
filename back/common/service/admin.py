from django.contrib import admin

from common.service.models import ServiceRecord


@admin.register(ServiceRecord)
class ServiceRecordAdmin(admin.ModelAdmin):
    list_display = ["id", "vehicle", "date", "description", "location", "cost"]
    search_fields = ["vehicle__vin", "description", "location"]
