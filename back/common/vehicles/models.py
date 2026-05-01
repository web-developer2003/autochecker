from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class Vehicle(BaseModel):
    make = models.CharField(max_length=100)
    make_ru = models.CharField(max_length=100, blank=True)
    make_en = models.CharField(max_length=100, blank=True)
    model = models.CharField(max_length=100)
    model_ru = models.CharField(max_length=100, blank=True)
    model_en = models.CharField(max_length=100, blank=True)
    year = models.IntegerField(null=True, blank=True)
    vin = models.CharField(max_length=50, unique=True, db_index=True)
    plate = models.CharField(max_length=20, blank=True, db_index=True)
    color = models.CharField(max_length=50, blank=True)
    color_ru = models.CharField(max_length=50, blank=True)
    color_en = models.CharField(max_length=50, blank=True)
    body_type = models.CharField(max_length=50, blank=True)
    body_type_ru = models.CharField(max_length=50, blank=True)
    body_type_en = models.CharField(max_length=50, blank=True)
    engine_type = models.CharField(max_length=50, blank=True)
    engine_type_ru = models.CharField(max_length=50, blank=True)
    engine_type_en = models.CharField(max_length=50, blank=True)
    engine_volume = models.CharField(max_length=20, blank=True)
    transmission = models.CharField(max_length=50, blank=True)
    transmission_ru = models.CharField(max_length=50, blank=True)
    transmission_en = models.CharField(max_length=50, blank=True)
    drive_type = models.CharField(max_length=50, blank=True)
    drive_type_ru = models.CharField(max_length=50, blank=True)
    drive_type_en = models.CharField(max_length=50, blank=True)
    image = models.ImageField(upload_to="vehicles/", blank=True, null=True)

    class Meta(BaseMeta):
        verbose_name = "Vehicle"
        verbose_name_plural = "Vehicles"

    def __str__(self):
        return f"{self.year} {self.make} {self.model}" if self.year else f"{self.make} {self.model}"


class SavedVehicle(BaseModel):
    user = models.ForeignKey(
        "users.User",
        on_delete=models.CASCADE,
        related_name="saved_vehicles",
    )
    vehicle = models.ForeignKey(
        Vehicle,
        on_delete=models.CASCADE,
        related_name="saved_by",
    )
    nickname = models.CharField(max_length=100, blank=True)

    class Meta(BaseMeta):
        verbose_name = "Saved Vehicle"
        verbose_name_plural = "Saved Vehicles"
        unique_together = ("user", "vehicle")

    def __str__(self):
        return f"{self.user.username} - {self.vehicle}"
