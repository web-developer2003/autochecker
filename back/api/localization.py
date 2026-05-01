class LocalizedMixin:
    """Serializer mixin: picks _uz/_ru/_en field based on Accept-Language header."""

    def get_lang(self):
        request = self.context.get("request")
        if request:
            lang = request.headers.get("Accept-Language", "uz")[:2].lower()
            if lang in ("uz", "ru", "en"):
                return lang
        return "uz"

    def localized(self, obj, field_name):
        """Return the localized value, falling back to the default (uz) field."""
        lang = self.get_lang()
        if lang == "uz":
            return getattr(obj, field_name, "")
        value = getattr(obj, f"{field_name}_{lang}", "")
        if not value:
            value = getattr(obj, field_name, "")
        return value
