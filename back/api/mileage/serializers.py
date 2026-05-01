from rest_framework import serializers

from api.localization import LocalizedMixin
from common.mileage.models import MileageRecord


class MileageRecordSerializer(LocalizedMixin, serializers.ModelSerializer):
    source = serializers.SerializerMethodField()

    class Meta:
        model = MileageRecord
        fields = ("id", "guid", "vehicle", "vehicle_check", "date", "mileage_km", "source")
        read_only_fields = ("id", "guid")

    def get_source(self, obj):
        return self.localized(obj, "source")
