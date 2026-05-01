from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin

from common.users.models import User


@admin.register(User)
class UserAdmin(BaseUserAdmin):
    list_display = ["username", "first_name", "last_name", "role", "is_active"]
    list_filter = ["is_active", "role"]
    search_fields = ["username", "first_name", "last_name", "phone"]
    fieldsets = BaseUserAdmin.fieldsets + (
        ("Extra", {"fields": ("phone", "role", "photo", "guid")}),
    )
    readonly_fields = ["guid"]
