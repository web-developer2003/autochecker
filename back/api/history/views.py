from drf_spectacular.utils import extend_schema
from rest_framework.permissions import IsAuthenticated
from rest_framework.viewsets import ReadOnlyModelViewSet

from api.history.serializers import CheckHistorySerializer
from api.paginator import CustomPagination
from common.history.models import CheckHistory


@extend_schema(tags=["Check History"])
class CheckHistoryViewSet(ReadOnlyModelViewSet):
    serializer_class = CheckHistorySerializer
    permission_classes = [IsAuthenticated]
    pagination_class = CustomPagination
    lookup_field = "guid"

    def get_queryset(self):
        return CheckHistory.objects.filter(
            user=self.request.user
        ).select_related("vehicle", "vehicle_check").order_by("-created_at")
