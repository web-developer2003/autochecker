from django.contrib.auth import authenticate
from django.contrib.auth import get_user_model
from django.contrib.auth.hashers import make_password
from rest_framework import serializers
from rest_framework_simplejwt.tokens import RefreshToken

User = get_user_model()


class LogOutSerializer(serializers.Serializer):
    refresh = serializers.CharField(write_only=True, max_length=255, required=True)


class UserLoginSerializer(serializers.ModelSerializer):
    user_id = serializers.IntegerField(read_only=True)
    user_guid = serializers.CharField(read_only=True)
    role = serializers.CharField(read_only=True)
    username = serializers.CharField(max_length=255)
    password = serializers.CharField(max_length=128, write_only=True)
    access = serializers.CharField(read_only=True)
    refresh = serializers.CharField(read_only=True)

    def validate(self, data):
        username = data["username"]
        password = data["password"]

        user = authenticate(username=username, password=password)

        if user is None:
            raise serializers.ValidationError("Invalid credentials")
        try:
            refresh = RefreshToken.for_user(user)
            return {
                "refresh": str(refresh),
                "access": str(refresh.access_token),
                "user_id": user.id,
                "user_guid": user.guid,
                "username": user.username,
                "role": user.role,
            }
        except User.DoesNotExist:
            raise serializers.ValidationError("User does not exist")

    class Meta:
        model = User
        fields = ("user_id", "user_guid", "role", "username", "password", "access", "refresh")
        read_only_fields = ("user_id", "role", "access", "refresh")


class UserRegisterSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True, required=True, min_length=8)
    password_confirm = serializers.CharField(write_only=True, required=True)
    access = serializers.CharField(read_only=True)
    refresh = serializers.CharField(read_only=True)

    class Meta:
        model = User
        fields = (
            "id", "guid", "first_name", "last_name",
            "email", "password", "password_confirm",
            "access", "refresh",
        )
        read_only_fields = ("id", "guid", "access", "refresh")

    def validate(self, data):
        if data["password"] != data["password_confirm"]:
            raise serializers.ValidationError({"password_confirm": "Passwords do not match"})
        if User.objects.filter(email=data["email"]).exists():
            raise serializers.ValidationError({"email": "User with this email already exists"})
        return data

    def create(self, validated_data):
        validated_data.pop("password_confirm")
        validated_data["username"] = validated_data["email"]
        validated_data["password"] = make_password(validated_data["password"])
        user = User.objects.create(**validated_data)
        return user

    def to_representation(self, instance):
        data = super().to_representation(instance)
        refresh = RefreshToken.for_user(instance)
        data["access"] = str(refresh.access_token)
        data["refresh"] = str(refresh)
        return data
