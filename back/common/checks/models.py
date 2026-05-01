from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class CheckStatus(models.TextChoices):
    CLEAN = "clean", "Clean"
    ISSUES_FOUND = "issues_found", "Issues Found"
    WARNING = "warning", "Warning"


class VehicleCheck(BaseModel):
    vehicle = models.ForeignKey(
        "vehicles.Vehicle",
        on_delete=models.CASCADE,
        related_name="checks",
    )
    user = models.ForeignKey(
        "users.User",
        on_delete=models.SET_NULL,
        null=True,
        blank=True,
        related_name="vehicle_checks",
    )

    # Summary results
    accidents_count = models.IntegerField(default=0)
    accidents_status = models.CharField(
        max_length=20, choices=CheckStatus.choices, default=CheckStatus.CLEAN
    )
    mileage_status = models.CharField(
        max_length=20, choices=CheckStatus.choices, default=CheckStatus.CLEAN
    )
    legal_status = models.CharField(
        max_length=20, choices=CheckStatus.choices, default=CheckStatus.CLEAN
    )
    service_records_count = models.IntegerField(default=0)
    taxi_usage = models.BooleanField(default=False)

    class Meta(BaseMeta):
        verbose_name = "Vehicle Check"
        verbose_name_plural = "Vehicle Checks"

    def __str__(self):
        return f"Check #{self.id} - {self.vehicle}"
