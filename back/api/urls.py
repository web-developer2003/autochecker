from django.urls import path
from rest_framework_simplejwt.views import TokenRefreshView

from api.auth.views import LoginAPIView
from api.auth.views import LogoutView
from api.auth.views import RegisterAPIView
from api.notifications.views import NotificationSettingsView
from api.notifications.views import UserPreferencesView
from api.profile.views import ChangePasswordView
from api.profile.views import ProfileView

urlpatterns = [
    # Auth
    path("auth/login/", LoginAPIView.as_view(), name="login"),
    path("auth/logout/", LogoutView.as_view(), name="logout"),
    path("auth/token/refresh/", TokenRefreshView.as_view(), name="token_refresh"),
    path("auth/register/", RegisterAPIView.as_view(), name="register"),

    # Profile
    path("profile/", ProfileView.as_view(), name="profile"),
    path("profile/change-password/", ChangePasswordView.as_view(), name="change-password"),

    # Settings
    path("settings/notifications/", NotificationSettingsView.as_view(), name="notification-settings"),
    path("settings/preferences/", UserPreferencesView.as_view(), name="user-preferences"),
]
