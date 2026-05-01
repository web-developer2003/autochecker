from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class ThemeChoice(models.TextChoices):
    LIGHT = "light", "Light"
    DARK = "dark", "Dark"
    SYSTEM = "system", "System"


class NotificationSettings(BaseModel):
    user = models.OneToOneField(
        "users.User",
        on_delete=models.CASCADE,
        related_name="notification_settings",
    )
    # Push notifications
    all_notifications = models.BooleanField(default=True)
    promotions = models.BooleanField(default=False)
    report_updates = models.BooleanField(default=True)
    # Email alerts
    weekly_summary = models.BooleanField(default=True)
    news_and_tips = models.BooleanField(default=False)

    class Meta(BaseMeta):
        verbose_name = "Notification Settings"
        verbose_name_plural = "Notification Settings"

    def __str__(self):
        return f"Notifications - {self.user.username}"


class UserPreferences(BaseModel):
    user = models.OneToOneField(
        "users.User",
        on_delete=models.CASCADE,
        related_name="preferences",
    )
    theme = models.CharField(
        max_length=10, choices=ThemeChoice.choices, default=ThemeChoice.SYSTEM
    )
    language = models.CharField(max_length=10, default="en")
    text_size = models.IntegerField(default=16)
    accent_color = models.CharField(max_length=7, default="#FF3B30")

    class Meta(BaseMeta):
        verbose_name = "User Preferences"
        verbose_name_plural = "User Preferences"

    def __str__(self):
        return f"Preferences - {self.user.username}"
