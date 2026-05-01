from rest_framework import serializers

from api.localization import LocalizedMixin
from api.vehicles.serializers import VehicleSerializer
from common.checks.models import VehicleCheck


class VehicleCheckListSerializer(LocalizedMixin, serializers.ModelSerializer):
    vehicle_name = serializers.SerializerMethodField()
    vehicle_vin = serializers.CharField(source="vehicle.vin")
    vehicle_plate = serializers.CharField(source="vehicle.plate")

    class Meta:
        model = VehicleCheck
        fields = (
            "id", "guid", "vehicle", "vehicle_name", "vehicle_vin", "vehicle_plate",
            "accidents_count", "accidents_status", "mileage_status",
            "legal_status", "service_records_count", "taxi_usage", "created_at",
        )

    def get_vehicle_name(self, obj):
        make = self.localized(obj.vehicle, "make")
        model = self.localized(obj.vehicle, "model")
        return f"{make} {model}"


class VehicleCheckDetailSerializer(serializers.ModelSerializer):
    vehicle = VehicleSerializer(read_only=True)

    class Meta:
        model = VehicleCheck
        fields = (
            "id", "guid", "vehicle",
            "accidents_count", "accidents_status", "mileage_status",
            "legal_status", "service_records_count", "taxi_usage", "created_at",
        )
