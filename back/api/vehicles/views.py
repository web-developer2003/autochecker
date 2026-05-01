from django.conf import settings
from drf_spectacular.utils import extend_schema
from rest_framework import status
from rest_framework.decorators import action
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.viewsets import ModelViewSet

from api.paginator import CustomPagination
from api.vehicles.serializers import SavedVehicleCreateSerializer
from api.vehicles.serializers import SavedVehicleSerializer
from api.vehicles.serializers import VehicleSearchSerializer
from api.vehicles.serializers import VehicleSerializer
from common.checks.models import VehicleCheck
from common.history.models import CheckHistory
from common.vehicles.models import SavedVehicle
from common.vehicles.models import Vehicle


def _gemini_internet_search(query: str, search_type: str) -> dict:
    """Search the internet for a vehicle using Gemini with Google Search grounding."""
    api_key = getattr(settings, "GEMINI_API_KEY", "")
    if not api_key:
        return {
            "error": True,
            "message": "GEMINI_API_KEY sozlanmagan",
            "message_ru": "GEMINI_API_KEY не настроен",
            "message_en": "GEMINI_API_KEY is not configured",
        }

    try:
        from google import genai  # noqa: PLC0415
        from google.genai import types  # noqa: PLC0415
    except ImportError:
        return {
            "error": True,
            "message": "google-genai paketi o'rnatilmagan",
            "message_ru": "Пакет google-genai не установлен",
            "message_en": "google-genai package is not installed",
        }

    label = "VIN kodi" if search_type == "vin" else "davlat raqami"
    prompt = (
        f'Avtomobil {label}: "{query}". '
        "Internetdan ushbu avtomobil haqida quyidagi ma'lumotlarni toping va qisqacha taqdim eting:\n"
        "1. Avtomobil markasi, modeli, yili (agar topilsa)\n"
        "2. Avariya tarixi (ochiq manbalardan)\n"
        "3. Mulkdorlik tarixi yoki egalar soni (agar ma'lum bo'lsa)\n"
        "4. Boshqa e'tiborga loyiq ma'lumotlar (o'g'irlik, garov va h.k.)\n\n"
        "Agar hech qanday ma'lumot topilmasa, shuni aniq ayt. "
        "Javobni o'zbek tilida ber."
    )

    try:
        client = genai.Client(api_key=api_key)
        response = client.models.generate_content(
            model="gemini-2.5-flash",
            contents=prompt,
            config=types.GenerateContentConfig(
                tools=[types.Tool(google_search=types.GoogleSearch())],
            ),
        )
        return {"error": False, "summary": response.text}
    except Exception as exc:  # noqa: BLE001
        return {
            "error": True,
            "message": f"Qidiruv xatosi: {exc}",
            "message_ru": f"Ошибка поиска: {exc}",
            "message_en": f"Search error: {exc}",
        }


@extend_schema(tags=["Vehicles"])
class VehicleViewSet(ModelViewSet):
    queryset = Vehicle.objects.all()
    serializer_class = VehicleSerializer
    pagination_class = CustomPagination
    lookup_field = "guid"

    @action(detail=False, methods=["post"])
    def search(self, request):
        serializer = VehicleSearchSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        query = serializer.validated_data["query"].strip()
        source = serializer.validated_data.get("source", "local")
        search_type = serializer.validated_data.get("search_type", "plate")

        if source == "internet":
            result = _gemini_internet_search(query, search_type)
            if result.get("error"):
                return Response(
                    {
                        "message": result.get("message", "Xatolik yuz berdi"),
                        "message_ru": result.get("message_ru", "Произошла ошибка"),
                        "message_en": result.get("message_en", "An error occurred"),
                        "results": [],
                        "source": "internet",
                    },
                    status=status.HTTP_200_OK,
                )
            return Response(
                {
                    "internet_summary": result["summary"],
                    "source": "internet",
                },
                status=status.HTTP_200_OK,
            )

        # source == "local" — search our own database
        if search_type == "vin":
            vehicles = Vehicle.objects.filter(vin__icontains=query)
        else:
            vehicles = Vehicle.objects.filter(plate__icontains=query)

        if not vehicles.exists():
            return Response(
                {
                    "message": "Avtomobil topilmadi",
                    "message_ru": "Автомобиль не найден",
                    "message_en": "No vehicle found",
                    "results": [],
                    "source": source if source in ("local", "government") else "local",
                },
                status=status.HTTP_200_OK,
            )

        vehicle = vehicles.first()
        check = VehicleCheck.objects.filter(vehicle=vehicle).order_by("-id").first()

        # Create check history if user is authenticated
        if request.user.is_authenticated:
            CheckHistory.objects.create(
                user=request.user,
                vehicle=vehicle,
                vehicle_check=check,
            )

        result = {
            "vehicle": VehicleSerializer(vehicle, context={"request": request}).data,
            "check_guid": str(check.guid) if check else None,
            "source": source if source in ("local", "government") else "local",
        }
        return Response(result, status=status.HTTP_200_OK)


@extend_schema(tags=["Saved Vehicles"])
class SavedVehicleViewSet(ModelViewSet):
    serializer_class = SavedVehicleSerializer
    permission_classes = [IsAuthenticated]
    pagination_class = CustomPagination
    lookup_field = "guid"

    def get_queryset(self):
        return SavedVehicle.objects.filter(
            user=self.request.user
        ).select_related("vehicle")

    def get_serializer_class(self):
        if self.action == "create":
            return SavedVehicleCreateSerializer
        return SavedVehicleSerializer

    def create(self, request, *args, **kwargs):
        create_serializer = SavedVehicleCreateSerializer(
            data=request.data, context=self.get_serializer_context()
        )
        create_serializer.is_valid(raise_exception=True)
        saved_vehicle = create_serializer.save()
        response_serializer = SavedVehicleSerializer(
            saved_vehicle, context=self.get_serializer_context()
        )
        return Response(response_serializer.data, status=status.HTTP_201_CREATED)
