from drf_spectacular.utils import extend_schema
from rest_framework.viewsets import ReadOnlyModelViewSet

from api.paginator import CustomPagination
from api.service.serializers import ServiceRecordSerializer
from common.service.models import ServiceRecord


@extend_schema(tags=["Service Records"])
class ServiceRecordViewSet(ReadOnlyModelViewSet):
    queryset = ServiceRecord.objects.select_related("vehicle").all()
    serializer_class = ServiceRecordSerializer
    pagination_class = CustomPagination
    lookup_field = "guid"
    filterset_fields = ["vehicle"]
