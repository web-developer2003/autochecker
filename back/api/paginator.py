from rest_framework import pagination
from rest_framework import serializers


class CustomPagination(pagination.LimitOffsetPagination):
    default_limit = 10
    max_limit = 9999999999999999999999
    min_limit = 1
    min_offset = 0
    max_offset = 9999999999999999999999

    def paginate_queryset(self, queryset, request, view=None):
        p = request.query_params.get("p")
        if p:
            limit = request.query_params.get("limit")
            offset = request.query_params.get("offset")

            if limit:
                limit = int(limit)
                if limit > self.max_limit:
                    raise serializers.ValidationError(
                        {"limit": [f"Limit should be less than or equal to {self.max_limit}"]}
                    )
                elif limit < self.min_limit:
                    raise serializers.ValidationError(
                        {"limit": [f"Limit should be greater than or equal to {self.min_limit}"]}
                    )

            if offset:
                offset = int(offset)
                if offset > self.max_offset:
                    raise serializers.ValidationError(
                        {"offset": [f"Offset should be less than or equal to {self.max_offset}"]}
                    )
                elif offset < self.min_offset:
                    raise serializers.ValidationError(
                        {"offset": [f"Offset should be greater than or equal to {self.min_offset}"]}
                    )
            return super().paginate_queryset(queryset, request, view)
