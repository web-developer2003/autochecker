from rest_framework import serializers

from common.notifications.models import NotificationSettings
from common.notifications.models import UserPreferences


class NotificationSettingsSerializer(serializers.ModelSerializer):
    class Meta:
        model = NotificationSettings
        fields = (
            "id", "all_notifications", "promotions", "report_updates",
            "weekly_summary", "news_and_tips",
        )
        read_only_fields = ("id",)


class UserPreferencesSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserPreferences
        fields = ("id", "theme", "language", "text_size", "accent_color")
        read_only_fields = ("id",)
