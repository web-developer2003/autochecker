from drf_spectacular.utils import extend_schema
from rest_framework import status
from rest_framework.generics import RetrieveUpdateAPIView
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from api.notifications.serializers import NotificationSettingsSerializer
from api.notifications.serializers import UserPreferencesSerializer
from common.notifications.models import NotificationSettings
from common.notifications.models import UserPreferences


@extend_schema(tags=["Notifications"])
class NotificationSettingsView(RetrieveUpdateAPIView):
    serializer_class = NotificationSettingsSerializer
    permission_classes = [IsAuthenticated]

    def get_object(self):
        obj, _ = NotificationSettings.objects.get_or_create(user=self.request.user)
        return obj


@extend_schema(tags=["Preferences"])
class UserPreferencesView(RetrieveUpdateAPIView):
    serializer_class = UserPreferencesSerializer
    permission_classes = [IsAuthenticated]

    def get_object(self):
        obj, _ = UserPreferences.objects.get_or_create(user=self.request.user)
        return obj
