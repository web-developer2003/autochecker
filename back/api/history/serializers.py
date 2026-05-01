from rest_framework import serializers

from api.vehicles.serializers import VehicleShortSerializer
from common.history.models import CheckHistory


class CheckHistorySerializer(serializers.ModelSerializer):
    vehicle = VehicleShortSerializer(read_only=True)

    class Meta:
        model = CheckHistory
        fields = ("id", "guid", "vehicle", "vehicle_check", "created_at")
        read_only_fields = ("id", "guid", "created_at")
