from django.contrib.auth import get_user_model
from rest_framework import generics
from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework_simplejwt.tokens import RefreshToken

from api.auth.serializers import LogOutSerializer
from api.auth.serializers import UserLoginSerializer
from api.auth.serializers import UserRegisterSerializer

User = get_user_model()


class LoginAPIView(generics.CreateAPIView):
    serializer_class = UserLoginSerializer
    queryset = User.objects.all()
    permission_classes = [AllowAny]
    authentication_classes = []

    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)
        return Response(serializer.data, status=status.HTTP_200_OK)


class RegisterAPIView(generics.CreateAPIView):
    serializer_class = UserRegisterSerializer
    queryset = User.objects.all()
    permission_classes = [AllowAny]
    authentication_classes = []


class LogoutView(generics.CreateAPIView):
    serializer_class = LogOutSerializer
    permission_classes = [IsAuthenticated]

    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data)
        serializer.is_valid(raise_exception=True)
        try:
            token = RefreshToken(serializer.validated_data.get("refresh"))
            token.blacklist()
        except Exception:
            return Response(
                {"message": "Token is invalid or blacklisting failed"},
                status=status.HTTP_400_BAD_REQUEST,
            )
        return Response({"message": "Successfully logged out"}, status=status.HTTP_200_OK)
