from django.contrib.auth.models import AbstractUser
from django.contrib.auth.models import UserManager as DjangoUserManager
from django.db import models

from common.base import BaseModel


class Role(models.IntegerChoices):
    ADMIN = 1, "Admin"
    USER = 2, "User"
    MODERATOR = 3, "Moderator"


class UserManager(DjangoUserManager):
    pass


class User(AbstractUser, BaseModel):
    phone = models.CharField(max_length=20, blank=True)
    role = models.IntegerField(choices=Role.choices, default=Role.USER)
    photo = models.ImageField(upload_to="users/", blank=True, null=True)

    objects = UserManager()

    class Meta:
        ordering = ["-id"]

    def __str__(self):
        return self.username
