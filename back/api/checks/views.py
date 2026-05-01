from django.db.models import Sum
from drf_spectacular.utils import extend_schema
from rest_framework import status
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.viewsets import ReadOnlyModelViewSet

from api.accidents.serializers import AccidentListSerializer
from api.accidents.serializers import AccidentSummarySerializer
from api.checks.serializers import VehicleCheckDetailSerializer
from api.checks.serializers import VehicleCheckListSerializer
from api.legal.serializers import LegalStatusSerializer
from api.mileage.serializers import MileageRecordSerializer
from api.paginator import CustomPagination
from api.service.serializers import ServiceRecordSerializer
from common.accidents.models import Accident
from common.checks.models import VehicleCheck
from common.legal.models import LegalStatus
from common.mileage.models import MileageRecord
from common.service.models import ServiceRecord


@extend_schema(tags=["Vehicle Checks"])
class VehicleCheckViewSet(ReadOnlyModelViewSet):
    queryset = VehicleCheck.objects.select_related("vehicle").all()
    pagination_class = CustomPagination
    lookup_field = "guid"

    def get_serializer_class(self):
        if self.action == "retrieve":
            return VehicleCheckDetailSerializer
        return VehicleCheckListSerializer

    @action(detail=True, methods=["get"], url_path="full-report")
    def full_report(self, request, guid=None):
        check = self.get_object()
        vehicle = check.vehicle

        accidents = Accident.objects.filter(vehicle=vehicle).order_by("-date")
        mileage = MileageRecord.objects.filter(vehicle=vehicle).order_by("date")
        legal = LegalStatus.objects.filter(vehicle=vehicle).order_by("-id").first()
        services = ServiceRecord.objects.filter(vehicle=vehicle).order_by("-date")

        # Accident summary
        total_damage = accidents.aggregate(total=Sum("repair_cost"))["total"] or 0
        accident_summary = {
            "total_accidents": accidents.count(),
            "major_count": accidents.filter(severity="major").count(),
            "minor_count": accidents.filter(severity="minor").count(),
            "moderate_count": accidents.filter(severity="moderate").count(),
            "total_damage_cost": total_damage,
        }

        ctx = {"request": request}

        return Response({
            "check": VehicleCheckDetailSerializer(check, context=ctx).data,
            "accident_summary": AccidentSummarySerializer(accident_summary).data,
            "accidents": AccidentListSerializer(accidents, many=True, context=ctx).data,
            "mileage_records": MileageRecordSerializer(mileage, many=True, context=ctx).data,
            "legal_status": LegalStatusSerializer(legal, context=ctx).data if legal else None,
            "service_records": ServiceRecordSerializer(services, many=True, context=ctx).data,
        })
