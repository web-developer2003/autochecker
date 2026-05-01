from django.contrib import admin

from common.notifications.models import NotificationSettings
from common.notifications.models import UserPreferences


@admin.register(NotificationSettings)
class NotificationSettingsAdmin(admin.ModelAdmin):
    list_display = ["id", "user", "all_notifications", "report_updates", "weekly_summary"]


@admin.register(UserPreferences)
class UserPreferencesAdmin(admin.ModelAdmin):
    list_display = ["id", "user", "theme", "language"]
