from rest_framework import serializers

from api.localization import LocalizedMixin
from common.service.models import ServiceRecord


class ServiceRecordSerializer(LocalizedMixin, serializers.ModelSerializer):
    description = serializers.SerializerMethodField()
    location = serializers.SerializerMethodField()

    class Meta:
        model = ServiceRecord
        fields = ("id", "guid", "vehicle", "vehicle_check", "date", "description", "location", "cost")
        read_only_fields = ("id", "guid")

    def get_description(self, obj):
        return self.localized(obj, "description")

    def get_location(self, obj):
        return self.localized(obj, "location")
