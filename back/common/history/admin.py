from django.contrib import admin

from common.history.models import CheckHistory


@admin.register(CheckHistory)
class CheckHistoryAdmin(admin.ModelAdmin):
    list_display = ["id", "user", "vehicle", "created_at"]
    search_fields = ["user__username", "vehicle__vin"]
