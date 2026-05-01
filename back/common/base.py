import uuid
from django.db import models
from django.utils import timezone


class BaseModel(models.Model):
    guid = models.UUIDField(unique=True, default=uuid.uuid4, editable=False)
    created_at = models.DateTimeField(default=timezone.now)
    updated_at = models.DateTimeField(auto_now=True)
    is_active = models.BooleanField(default=True)
    recorder = models.ForeignKey(
        "users.User",
        related_name="recorder%(class)s",
        on_delete=models.SET_NULL,
        null=True,
        blank=True,
    )

    class Meta:
        abstract = True


class BaseMeta:
    ordering = ["-id"]
