from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class CheckHistory(BaseModel):
    user = models.ForeignKey(
        "users.User",
        on_delete=models.CASCADE,
        related_name="check_history",
    )
    vehicle = models.ForeignKey(
        "vehicles.Vehicle",
        on_delete=models.CASCADE,
        related_name="check_history",
    )
    vehicle_check = models.ForeignKey(
        "checks.VehicleCheck",
        on_delete=models.SET_NULL,
        null=True,
        blank=True,
        related_name="history_entries",
    )

    class Meta(BaseMeta):
        verbose_name = "Check History"
        verbose_name_plural = "Check Histories"

    def __str__(self):
        return f"{self.user.username} checked {self.vehicle} at {self.created_at}"
