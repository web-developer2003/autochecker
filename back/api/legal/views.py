from drf_spectacular.utils import extend_schema
from rest_framework.viewsets import ReadOnlyModelViewSet

from api.legal.serializers import LegalStatusSerializer
from api.paginator import CustomPagination
from common.legal.models import LegalStatus


@extend_schema(tags=["Legal Status"])
class LegalStatusViewSet(ReadOnlyModelViewSet):
    queryset = LegalStatus.objects.select_related("vehicle").all()
    serializer_class = LegalStatusSerializer
    pagination_class = CustomPagination
    lookup_field = "guid"
    filterset_fields = ["vehicle"]
