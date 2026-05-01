from django.db.models import Sum
from django.utils import timezone
from drf_spectacular.utils import extend_schema
from rest_framework import status
from rest_framework.decorators import action
from rest_framework.parsers import FormParser
from rest_framework.parsers import MultiPartParser
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.viewsets import ReadOnlyModelViewSet

from api.accidents.serializers import AccidentDetailSerializer
from api.accidents.serializers import AccidentListSerializer
from api.accidents.serializers import AccidentReportSerializer
from api.accidents.serializers import AccidentSummarySerializer
from api.paginator import CustomPagination
from common.accidents.models import Accident
from common.accidents.models import AccidentPhoto
from common.checks.models import CheckStatus
from common.checks.models import VehicleCheck
from common.vehicles.models import Vehicle


@extend_schema(tags=["Accidents"])
class AccidentViewSet(ReadOnlyModelViewSet):
    queryset = Accident.objects.select_related(
        "vehicle", "insurance_claim"
    ).prefetch_related("photos").all()
    pagination_class = CustomPagination
    lookup_field = "guid"
    filterset_fields = ["vehicle", "severity"]

    def get_serializer_class(self):
        if self.action == "retrieve":
            return AccidentDetailSerializer
        if self.action == "report":
            return AccidentReportSerializer
        return AccidentListSerializer

    @action(detail=False, methods=["get"], url_path="summary/(?P<vehicle_guid>[^/.]+)")
    def summary(self, request, vehicle_guid=None):
        accidents = Accident.objects.filter(vehicle__guid=vehicle_guid)
        total_damage = accidents.aggregate(total=Sum("repair_cost"))["total"] or 0

        data = {
            "total_accidents": accidents.count(),
            "major_count": accidents.filter(severity="major").count(),
            "minor_count": accidents.filter(severity="minor").count(),
            "moderate_count": accidents.filter(severity="moderate").count(),
            "total_damage_cost": total_damage,
        }
        return Response(AccidentSummarySerializer(data).data)

    @action(
        detail=False,
        methods=["post"],
        parser_classes=[MultiPartParser, FormParser],
        permission_classes=[IsAuthenticated],
    )
    def report(self, request):
        serializer = AccidentReportSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        plate = serializer.validated_data["plate_number"].strip().upper()
        photo = serializer.validated_data["photo"]
        description = serializer.validated_data.get("description", "")
        severity = serializer.validated_data.get("severity", "minor")
        location = serializer.validated_data.get("location", "")

        # Find or create vehicle by plate number
        vehicle = Vehicle.objects.filter(plate__iexact=plate).first()
        created = False
        if not vehicle:
            vehicle = Vehicle.objects.create(
                plate=plate,
                vin=f"UNKNOWN-{plate}",
                make="Noma'lum",
                make_ru="Неизвестно",
                make_en="Unknown",
                model="Noma'lum",
                model_ru="Неизвестно",
                model_en="Unknown",
                recorder=request.user,
            )
            created = True

        # Create accident record
        accident = Accident.objects.create(
            vehicle=vehicle,
            date=timezone.now().date(),
            severity=severity,
            damage_description=description,
            damage_description_ru=description,
            damage_description_en=description,
            location=location,
            location_ru=location,
            location_en=location,
            damage_area="Noma'lum",
            damage_area_ru="Неизвестно",
            damage_area_en="Unknown",
            recorder=request.user,
        )

        # Attach photo
        AccidentPhoto.objects.create(
            accident=accident,
            photo=photo,
            description=description,
            recorder=request.user,
        )

        # Create or update VehicleCheck so the search endpoint returns a valid check_guid
        total_accidents = Accident.objects.filter(vehicle=vehicle).count()
        if total_accidents >= 3:
            acc_status = CheckStatus.ISSUES_FOUND
        elif total_accidents >= 1:
            acc_status = CheckStatus.WARNING
        else:
            acc_status = CheckStatus.CLEAN

        check, created_check = VehicleCheck.objects.get_or_create(
            vehicle=vehicle,
            defaults={
                "accidents_count": total_accidents,
                "accidents_status": acc_status,
                "recorder": request.user,
            },
        )
        if not created_check:
            check.accidents_count = total_accidents
            check.accidents_status = acc_status
            check.save(update_fields=["accidents_count", "accidents_status"])

        return Response(
            {
                "message": "Avariya muvaffaqiyatli ro'yxatga olindi",
                "message_ru": "Авария успешно зарегистрирована",
                "message_en": "Accident reported successfully",
                "accident_guid": str(accident.guid),
                "vehicle_guid": str(vehicle.guid),
                "vehicle_created": created,
            },
            status=status.HTTP_201_CREATED,
        )
