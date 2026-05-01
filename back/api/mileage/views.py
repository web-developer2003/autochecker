from drf_spectacular.utils import extend_schema
from rest_framework.viewsets import ReadOnlyModelViewSet

from api.mileage.serializers import MileageRecordSerializer
from api.paginator import CustomPagination
from common.mileage.models import MileageRecord


@extend_schema(tags=["Mileage"])
class MileageRecordViewSet(ReadOnlyModelViewSet):
    queryset = MileageRecord.objects.select_related("vehicle").all()
    serializer_class = MileageRecordSerializer
    pagination_class = CustomPagination
    lookup_field = "guid"
    filterset_fields = ["vehicle"]
