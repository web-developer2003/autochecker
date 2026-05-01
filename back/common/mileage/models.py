from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class MileageRecord(BaseModel):
    vehicle = models.ForeignKey(
        "vehicles.Vehicle",
        on_delete=models.CASCADE,
        related_name="mileage_records",
    )
    vehicle_check = models.ForeignKey(
        "checks.VehicleCheck",
        on_delete=models.CASCADE,
        related_name="mileage_records",
        null=True,
        blank=True,
    )
    date = models.DateField()
    mileage_km = models.IntegerField()
    source = models.CharField(max_length=200, blank=True)
    source_ru = models.CharField(max_length=200, blank=True)
    source_en = models.CharField(max_length=200, blank=True)

    class Meta(BaseMeta):
        verbose_name = "Mileage Record"
        verbose_name_plural = "Mileage Records"
        ordering = ["date"]

    def __str__(self):
        return f"{self.vehicle} - {self.date}: {self.mileage_km} km"
