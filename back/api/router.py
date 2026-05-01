from rest_framework.routers import DefaultRouter

from api.accidents.views import AccidentViewSet
from api.checks.views import VehicleCheckViewSet
from api.history.views import CheckHistoryViewSet
from api.legal.views import LegalStatusViewSet
from api.mileage.views import MileageRecordViewSet
from api.service.views import ServiceRecordViewSet
from api.vehicles.views import SavedVehicleViewSet
from api.vehicles.views import VehicleViewSet

router = DefaultRouter()

router.register("vehicle", VehicleViewSet)
router.register("saved-vehicle", SavedVehicleViewSet, basename="saved-vehicle")
router.register("check", VehicleCheckViewSet)
router.register("accident", AccidentViewSet)
router.register("mileage", MileageRecordViewSet)
router.register("legal-status", LegalStatusViewSet)
router.register("service-record", ServiceRecordViewSet)
router.register("history", CheckHistoryViewSet, basename="history")

app_name = "api"
urlpatterns = router.urls
