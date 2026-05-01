from django.contrib import admin

from common.accidents.models import Accident
from common.accidents.models import AccidentPhoto
from common.accidents.models import InsuranceClaim


class AccidentPhotoInline(admin.TabularInline):
    model = AccidentPhoto
    extra = 0


class InsuranceClaimInline(admin.StackedInline):
    model = InsuranceClaim
    extra = 0


@admin.register(Accident)
class AccidentAdmin(admin.ModelAdmin):
    list_display = ["id", "vehicle", "date", "severity", "damage_area", "repair_cost"]
    list_filter = ["severity"]
    search_fields = ["vehicle__vin", "damage_area"]
    inlines = [AccidentPhotoInline, InsuranceClaimInline]
