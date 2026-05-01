from rest_framework import serializers

from api.localization import LocalizedMixin
from common.accidents.models import Accident
from common.accidents.models import AccidentPhoto
from common.accidents.models import InsuranceClaim


class AccidentPhotoSerializer(serializers.ModelSerializer):
    photo_url = serializers.SerializerMethodField()

    class Meta:
        model = AccidentPhoto
        fields = ("id", "guid", "photo_url", "description")
        read_only_fields = ("id", "guid")

    def get_photo_url(self, obj):
        if obj.photo:
            request = self.context.get("request")
            if request:
                return request.build_absolute_uri(obj.photo.url)
            return obj.photo.url
        return None


class InsuranceClaimSerializer(LocalizedMixin, serializers.ModelSerializer):
    claim_status = serializers.SerializerMethodField()
    provider = serializers.SerializerMethodField()

    class Meta:
        model = InsuranceClaim
        fields = ("id", "guid", "claim_status", "provider", "claim_number", "payout_amount")
        read_only_fields = ("id", "guid")

    def get_claim_status(self, obj):
        return self.localized(obj, "claim_status")

    def get_provider(self, obj):
        return self.localized(obj, "provider")


class AccidentListSerializer(LocalizedMixin, serializers.ModelSerializer):
    damage_area = serializers.SerializerMethodField()
    accident_type = serializers.SerializerMethodField()
    thumbnail = serializers.SerializerMethodField()

    class Meta:
        model = Accident
        fields = (
            "id", "guid", "date", "severity", "damage_area",
            "repair_cost", "accident_type", "thumbnail",
        )

    def get_damage_area(self, obj):
        return self.localized(obj, "damage_area")

    def get_accident_type(self, obj):
        return self.localized(obj, "accident_type")

    def get_thumbnail(self, obj):
        photo = obj.photos.first()
        if photo and photo.photo:
            request = self.context.get("request")
            if request:
                return request.build_absolute_uri(photo.photo.url)
            return photo.photo.url
        return None


class AccidentDetailSerializer(LocalizedMixin, serializers.ModelSerializer):
    photos = AccidentPhotoSerializer(many=True, read_only=True)
    insurance_claim = InsuranceClaimSerializer(read_only=True)
    damage_area = serializers.SerializerMethodField()
    damage_description = serializers.SerializerMethodField()
    accident_type = serializers.SerializerMethodField()
    location = serializers.SerializerMethodField()
    impact_zone = serializers.SerializerMethodField()
    repair_status = serializers.SerializerMethodField()
    damage_areas = serializers.SerializerMethodField()

    class Meta:
        model = Accident
        fields = (
            "id", "guid", "vehicle", "vehicle_check", "date", "severity",
            "damage_area", "damage_description", "repair_cost",
            "accident_type", "location", "impact_zone",
            "airbags_deployed", "repair_status", "police_report_number",
            "damage_areas", "photos", "insurance_claim", "created_at",
        )

    def get_damage_area(self, obj):
        return self.localized(obj, "damage_area")

    def get_damage_description(self, obj):
        return self.localized(obj, "damage_description")

    def get_accident_type(self, obj):
        return self.localized(obj, "accident_type")

    def get_location(self, obj):
        return self.localized(obj, "location")

    def get_impact_zone(self, obj):
        return self.localized(obj, "impact_zone")

    def get_repair_status(self, obj):
        return self.localized(obj, "repair_status")

    def get_damage_areas(self, obj):
        lang = self.get_lang()
        if lang == "uz":
            return obj.damage_areas
        value = getattr(obj, f"damage_areas_{lang}", [])
        if not value:
            value = obj.damage_areas
        return value


class AccidentSummarySerializer(serializers.Serializer):
    total_accidents = serializers.IntegerField()
    major_count = serializers.IntegerField()
    minor_count = serializers.IntegerField()
    moderate_count = serializers.IntegerField()
    total_damage_cost = serializers.DecimalField(max_digits=12, decimal_places=2)


class AccidentReportSerializer(serializers.Serializer):
    plate_number = serializers.CharField(
        max_length=20,
        help_text="Vehicle license plate number",
    )
    photo = serializers.ImageField(
        help_text="Photo of the accident",
    )
    description = serializers.CharField(
        required=False,
        default="",
        help_text="Description of the accident",
    )
    severity = serializers.ChoiceField(
        choices=["minor", "moderate", "major"],
        default="minor",
        required=False,
        help_text="Severity level of the accident",
    )
    location = serializers.CharField(
        required=False,
        default="",
        help_text="Location where the accident occurred",
    )
