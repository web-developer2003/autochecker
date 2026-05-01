from rest_framework import serializers

from api.localization import LocalizedMixin
from common.vehicles.models import SavedVehicle
from common.vehicles.models import Vehicle


class VehicleSerializer(LocalizedMixin, serializers.ModelSerializer):
    make = serializers.SerializerMethodField()
    model = serializers.SerializerMethodField()
    color = serializers.SerializerMethodField()
    body_type = serializers.SerializerMethodField()
    engine_type = serializers.SerializerMethodField()
    transmission = serializers.SerializerMethodField()
    drive_type = serializers.SerializerMethodField()
    image_url = serializers.SerializerMethodField()

    class Meta:
        model = Vehicle
        fields = (
            "id", "guid", "vin", "plate", "make", "model", "year",
            "color", "body_type", "engine_type", "engine_volume",
            "transmission", "drive_type", "image_url", "created_at",
        )
        read_only_fields = ("id", "guid", "created_at")

    def get_make(self, obj):
        return self.localized(obj, "make")

    def get_model(self, obj):
        return self.localized(obj, "model")

    def get_color(self, obj):
        return self.localized(obj, "color")

    def get_body_type(self, obj):
        return self.localized(obj, "body_type")

    def get_engine_type(self, obj):
        return self.localized(obj, "engine_type")

    def get_transmission(self, obj):
        return self.localized(obj, "transmission")

    def get_drive_type(self, obj):
        return self.localized(obj, "drive_type")

    def get_image_url(self, obj):
        if obj.image:
            request = self.context.get("request")
            if request:
                return request.build_absolute_uri(obj.image.url)
            return obj.image.url
        return None


class VehicleShortSerializer(LocalizedMixin, serializers.ModelSerializer):
    display_name = serializers.SerializerMethodField()
    make = serializers.SerializerMethodField()
    model = serializers.SerializerMethodField()

    class Meta:
        model = Vehicle
        fields = ("id", "guid", "make", "model", "year", "vin", "plate", "display_name")

    def get_display_name(self, obj):
        return f"{self.get_make(obj)} {self.get_model(obj)}"

    def get_make(self, obj):
        return self.localized(obj, "make")

    def get_model(self, obj):
        return self.localized(obj, "model")


class SavedVehicleSerializer(serializers.ModelSerializer):
    vehicle = VehicleShortSerializer(read_only=True)

    class Meta:
        model = SavedVehicle
        fields = ("id", "guid", "vehicle", "nickname", "created_at")
        read_only_fields = ("id", "guid", "created_at")


class SavedVehicleCreateSerializer(serializers.Serializer):
    vin = serializers.CharField()
    nickname = serializers.CharField(required=False, default="")

    def create(self, validated_data):
        vehicle = Vehicle.objects.get(vin=validated_data["vin"])
        saved, created = SavedVehicle.objects.get_or_create(
            user=self.context["request"].user,
            vehicle=vehicle,
            defaults={"nickname": validated_data.get("nickname", "")},
        )
        return saved


class VehicleSearchSerializer(serializers.Serializer):
    query = serializers.CharField(help_text="VIN or license plate number")
    search_type = serializers.ChoiceField(
        choices=["vin", "plate"],
        default="plate",
        required=False,
        help_text="Search type: vin or plate number",
    )
    source = serializers.ChoiceField(
        choices=["local", "government", "internet"],
        default="local",
        required=False,
        help_text="Search source: local (our DB), government (gov API), internet (LLM web search)",
    )
