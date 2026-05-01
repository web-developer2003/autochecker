from django.db import models

from common.base import BaseModel
from common.base import BaseMeta


class LegalStatusType(models.TextChoices):
    CLEAN = "clean", "Clean"
    ISSUE = "issue", "Issue Found"


class LegalStatus(BaseModel):
    vehicle = models.ForeignKey(
        "vehicles.Vehicle",
        on_delete=models.CASCADE,
        related_name="legal_statuses",
    )
    vehicle_check = models.ForeignKey(
        "checks.VehicleCheck",
        on_delete=models.CASCADE,
        related_name="legal_statuses",
        null=True,
        blank=True,
    )
    liens = models.CharField(max_length=100, default="None")
    liens_ru = models.CharField(max_length=100, blank=True)
    liens_en = models.CharField(max_length=100, blank=True)
    liens_status = models.CharField(
        max_length=20, choices=LegalStatusType.choices, default=LegalStatusType.CLEAN
    )
    restrictions = models.CharField(max_length=100, default="None")
    restrictions_ru = models.CharField(max_length=100, blank=True)
    restrictions_en = models.CharField(max_length=100, blank=True)
    restrictions_status = models.CharField(
        max_length=20, choices=LegalStatusType.choices, default=LegalStatusType.CLEAN
    )
    theft_records = models.CharField(max_length=100, default="Not reported")
    theft_records_ru = models.CharField(max_length=100, blank=True)
    theft_records_en = models.CharField(max_length=100, blank=True)
    theft_status = models.CharField(
        max_length=20, choices=LegalStatusType.choices, default=LegalStatusType.CLEAN
    )
    registration = models.CharField(max_length=100, default="Active")
    registration_ru = models.CharField(max_length=100, blank=True)
    registration_en = models.CharField(max_length=100, blank=True)
    registration_status = models.CharField(
        max_length=20, choices=LegalStatusType.choices, default=LegalStatusType.CLEAN
    )

    class Meta(BaseMeta):
        verbose_name = "Legal Status"
        verbose_name_plural = "Legal Statuses"

    def __str__(self):
        return f"Legal Status - {self.vehicle}"
