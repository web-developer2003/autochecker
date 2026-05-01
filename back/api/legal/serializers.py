from rest_framework import serializers

from api.localization import LocalizedMixin
from common.legal.models import LegalStatus


class LegalStatusSerializer(LocalizedMixin, serializers.ModelSerializer):
    liens = serializers.SerializerMethodField()
    restrictions = serializers.SerializerMethodField()
    theft_records = serializers.SerializerMethodField()
    registration = serializers.SerializerMethodField()

    class Meta:
        model = LegalStatus
        fields = (
            "id", "guid", "vehicle", "vehicle_check",
            "liens", "liens_status",
            "restrictions", "restrictions_status",
            "theft_records", "theft_status",
            "registration", "registration_status",
        )
        read_only_fields = ("id", "guid")

    def get_liens(self, obj):
        return self.localized(obj, "liens")

    def get_restrictions(self, obj):
        return self.localized(obj, "restrictions")

    def get_theft_records(self, obj):
        return self.localized(obj, "theft_records")

    def get_registration(self, obj):
        return self.localized(obj, "registration")
