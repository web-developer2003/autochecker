from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class ServiceRecord(BaseModel):
    vehicle = models.ForeignKey(
        "vehicles.Vehicle",
        on_delete=models.CASCADE,
        related_name="service_records",
    )
    vehicle_check = models.ForeignKey(
        "checks.VehicleCheck",
        on_delete=models.CASCADE,
        related_name="service_records",
        null=True,
        blank=True,
    )
    date = models.DateField()
    description = models.TextField()
    description_ru = models.TextField(blank=True)
    description_en = models.TextField(blank=True)
    location = models.CharField(max_length=300, blank=True)
    location_ru = models.CharField(max_length=300, blank=True)
    location_en = models.CharField(max_length=300, blank=True)
    cost = models.DecimalField(max_digits=12, decimal_places=2, null=True, blank=True)

    class Meta(BaseMeta):
        verbose_name = "Service Record"
        verbose_name_plural = "Service Records"
        ordering = ["-date"]

    def __str__(self):
        return f"{self.vehicle} - {self.date}: {self.description[:50]}"
