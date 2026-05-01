from django.contrib import admin

from common.legal.models import LegalStatus


@admin.register(LegalStatus)
class LegalStatusAdmin(admin.ModelAdmin):
    list_display = ["id", "vehicle", "liens_status", "restrictions_status", "theft_status", "registration_status"]
    search_fields = ["vehicle__vin"]
