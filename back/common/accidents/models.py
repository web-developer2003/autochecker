from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class SeverityLevel(models.TextChoices):
    MINOR = "minor", "Minor"
    MODERATE = "moderate", "Moderate"
    MAJOR = "major", "Major"


class Accident(BaseModel):
    vehicle = models.ForeignKey(
        "vehicles.Vehicle",
        on_delete=models.CASCADE,
        related_name="accidents",
    )
    vehicle_check = models.ForeignKey(
        "checks.VehicleCheck",
        on_delete=models.CASCADE,
        related_name="accidents",
        null=True,
        blank=True,
    )
    date = models.DateField()
    severity = models.CharField(
        max_length=20, choices=SeverityLevel.choices, default=SeverityLevel.MINOR
    )
    damage_area = models.CharField(max_length=200)
    damage_area_ru = models.CharField(max_length=200, blank=True)
    damage_area_en = models.CharField(max_length=200, blank=True)
    damage_description = models.TextField(blank=True)
    damage_description_ru = models.TextField(blank=True)
    damage_description_en = models.TextField(blank=True)
    repair_cost = models.DecimalField(max_digits=12, decimal_places=2, null=True, blank=True)
    accident_type = models.CharField(max_length=200, blank=True)
    accident_type_ru = models.CharField(max_length=200, blank=True)
    accident_type_en = models.CharField(max_length=200, blank=True)
    location = models.CharField(max_length=300, blank=True)
    location_ru = models.CharField(max_length=300, blank=True)
    location_en = models.CharField(max_length=300, blank=True)
    impact_zone = models.CharField(max_length=100, blank=True)
    impact_zone_ru = models.CharField(max_length=100, blank=True)
    impact_zone_en = models.CharField(max_length=100, blank=True)
    airbags_deployed = models.BooleanField(default=False)
    # Repair status
    repair_status = models.CharField(max_length=100, blank=True)
    repair_status_ru = models.CharField(max_length=100, blank=True)
    repair_status_en = models.CharField(max_length=100, blank=True)
    # Police report
    police_report_number = models.CharField(max_length=50, blank=True)
    # Damage areas as JSON list (for multiple areas)
    damage_areas = models.JSONField(default=list, blank=True)
    damage_areas_ru = models.JSONField(default=list, blank=True)
    damage_areas_en = models.JSONField(default=list, blank=True)

    class Meta(BaseMeta):
        verbose_name = "Accident"
        verbose_name_plural = "Accidents"

    def __str__(self):
        return f"{self.vehicle} - {self.date} ({self.severity})"


class AccidentPhoto(BaseModel):
    accident = models.ForeignKey(
        Accident,
        on_delete=models.CASCADE,
        related_name="photos",
    )
    photo = models.ImageField(upload_to="accidents/photos/")
    description = models.CharField(max_length=200, blank=True)

    class Meta(BaseMeta):
        verbose_name = "Accident Photo"
        verbose_name_plural = "Accident Photos"


class InsuranceClaim(BaseModel):
    accident = models.OneToOneField(
        Accident,
        on_delete=models.CASCADE,
        related_name="insurance_claim",
    )
    claim_status = models.CharField(max_length=50)
    claim_status_ru = models.CharField(max_length=50, blank=True)
    claim_status_en = models.CharField(max_length=50, blank=True)
    provider = models.CharField(max_length=200)
    provider_ru = models.CharField(max_length=200, blank=True)
    provider_en = models.CharField(max_length=200, blank=True)
    claim_number = models.CharField(max_length=100)
    payout_amount = models.DecimalField(max_digits=12, decimal_places=2, null=True, blank=True)

    class Meta(BaseMeta):
        verbose_name = "Insurance Claim"
        verbose_name_plural = "Insurance Claims"

    def __str__(self):
        return f"{self.claim_number} - {self.provider}"
