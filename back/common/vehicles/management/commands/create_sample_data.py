from datetime import date
from decimal import Decimal

from django.core.management.base import BaseCommand

from common.accidents.models import Accident
from common.accidents.models import InsuranceClaim
from common.checks.models import VehicleCheck
from common.legal.models import LegalStatus
from common.mileage.models import MileageRecord
from common.service.models import ServiceRecord
from common.vehicles.models import Vehicle


class Command(BaseCommand):
    help = "Create sample vehicles with full report data in 3 languages (uz, ru, en)"

    def handle(self, *args, **options):
        self.stdout.write("Creating sample data...")

        # --- Vehicle 1: Chevrolet Malibu (clean) ---
        v1, _ = Vehicle.objects.update_or_create(
            vin="1G1ZD5ST7LF012345",
            defaults={
                "plate": "01A777AA",
                "make": "Chevrolet", "make_ru": "Шевроле", "make_en": "Chevrolet",
                "model": "Malibu", "model_ru": "Малибу", "model_en": "Malibu",
                "year": 2020,
                "color": "Oq", "color_ru": "Белый", "color_en": "White",
                "body_type": "Sedan", "body_type_ru": "Седан", "body_type_en": "Sedan",
                "engine_type": "Benzin", "engine_type_ru": "Бензин", "engine_type_en": "Gasoline",
                "engine_volume": "1.5L",
                "transmission": "Avtomat", "transmission_ru": "Автомат", "transmission_en": "Automatic",
                "drive_type": "Oldingi", "drive_type_ru": "Передний", "drive_type_en": "Front-wheel",
            },
        )
        c1, _ = VehicleCheck.objects.update_or_create(
            vehicle=v1,
            defaults={
                "accidents_count": 0,
                "accidents_status": "clean",
                "mileage_status": "clean",
                "legal_status": "clean",
                "service_records_count": 3,
                "taxi_usage": False,
            },
        )
        # Mileage records
        for d, km, src, src_ru, src_en in [
            (date(2020, 3, 15), 150, "Diler markazi", "Дилерский центр", "Dealer center"),
            (date(2021, 6, 20), 15200, "Texnik ko'rik", "Техосмотр", "Technical inspection"),
            (date(2022, 9, 10), 32400, "Servis markazi", "Сервисный центр", "Service center"),
            (date(2023, 12, 5), 48700, "Texnik ko'rik", "Техосмотр", "Technical inspection"),
            (date(2024, 8, 18), 62300, "Servis markazi", "Сервисный центр", "Service center"),
        ]:
            MileageRecord.objects.update_or_create(
                vehicle=v1, date=d,
                defaults={
                    "vehicle_check": c1, "mileage_km": km,
                    "source": src, "source_ru": src_ru, "source_en": src_en,
                },
            )
        # Service records
        for d, desc, desc_ru, desc_en, loc, loc_ru, loc_en, cost in [
            (date(2021, 6, 20), "Moy almashtirish va filtr", "Замена масла и фильтра",
             "Oil and filter change", "Chevrolet Toshkent", "Шевроле Ташкент",
             "Chevrolet Tashkent", Decimal("350000")),
            (date(2022, 9, 10), "Tormoz nakladkalari almashtirish",
             "Замена тормозных колодок", "Brake pad replacement",
             "AutoService Plus", "АвтоСервис Плюс", "AutoService Plus", Decimal("800000")),
            (date(2024, 8, 18), "To'liq texnik xizmat ko'rsatish",
             "Полное техническое обслуживание", "Full maintenance service",
             "Chevrolet Toshkent", "Шевроле Ташкент", "Chevrolet Tashkent", Decimal("1500000")),
        ]:
            ServiceRecord.objects.update_or_create(
                vehicle=v1, date=d,
                defaults={
                    "vehicle_check": c1,
                    "description": desc, "description_ru": desc_ru, "description_en": desc_en,
                    "location": loc, "location_ru": loc_ru, "location_en": loc_en,
                    "cost": cost,
                },
            )
        # Legal status - clean
        LegalStatus.objects.update_or_create(
            vehicle=v1,
            defaults={
                "vehicle_check": c1,
                "liens": "Yo'q", "liens_ru": "Нет", "liens_en": "None",
                "liens_status": "clean",
                "restrictions": "Yo'q", "restrictions_ru": "Нет", "restrictions_en": "None",
                "restrictions_status": "clean",
                "theft_records": "Qayd etilmagan", "theft_records_ru": "Не зафиксировано",
                "theft_records_en": "Not reported",
                "theft_status": "clean",
                "registration": "Faol", "registration_ru": "Активна",
                "registration_en": "Active",
                "registration_status": "clean",
            },
        )

        # --- Vehicle 2: Kia K5 (with accidents) ---
        v2, _ = Vehicle.objects.update_or_create(
            vin="5XXG34J22NG123456",
            defaults={
                "plate": "01B555BB",
                "make": "Kia", "make_ru": "Киа", "make_en": "Kia",
                "model": "K5", "model_ru": "K5", "model_en": "K5",
                "year": 2022,
                "color": "Qora", "color_ru": "Чёрный", "color_en": "Black",
                "body_type": "Sedan", "body_type_ru": "Седан", "body_type_en": "Sedan",
                "engine_type": "Benzin", "engine_type_ru": "Бензин", "engine_type_en": "Gasoline",
                "engine_volume": "2.5L",
                "transmission": "Avtomat", "transmission_ru": "Автомат", "transmission_en": "Automatic",
                "drive_type": "Oldingi", "drive_type_ru": "Передний", "drive_type_en": "Front-wheel",
            },
        )
        c2, _ = VehicleCheck.objects.update_or_create(
            vehicle=v2,
            defaults={
                "accidents_count": 2,
                "accidents_status": "issues_found",
                "mileage_status": "clean",
                "legal_status": "clean",
                "service_records_count": 4,
                "taxi_usage": False,
            },
        )
        # Accidents
        a1, _ = Accident.objects.update_or_create(
            vehicle=v2, date=date(2023, 5, 12),
            defaults={
                "vehicle_check": c2,
                "severity": "minor",
                "damage_area": "Orqa bamper",
                "damage_area_ru": "Задний бампер",
                "damage_area_en": "Rear bumper",
                "damage_description": "Engil to'qnashuv, bamper tirnalgan",
                "damage_description_ru": "Лёгкое столкновение, бампер поцарапан",
                "damage_description_en": "Light collision, bumper scratched",
                "repair_cost": Decimal("2500000"),
                "accident_type": "To'qnashuv",
                "accident_type_ru": "Столкновение",
                "accident_type_en": "Collision",
                "location": "Toshkent, Chilonzor tumani",
                "location_ru": "Ташкент, Чиланзарский район",
                "location_en": "Tashkent, Chilanzar district",
                "impact_zone": "Orqa",
                "impact_zone_ru": "Задняя часть",
                "impact_zone_en": "Rear",
                "airbags_deployed": False,
                "repair_status": "Ta'mirlangan",
                "repair_status_ru": "Отремонтировано",
                "repair_status_en": "Repaired",
                "damage_areas": ["Orqa bamper", "Orqa panel"],
                "damage_areas_ru": ["Задний бампер", "Задняя панель"],
                "damage_areas_en": ["Rear bumper", "Rear panel"],
            },
        )
        InsuranceClaim.objects.update_or_create(
            accident=a1,
            defaults={
                "claim_status": "To'langan",
                "claim_status_ru": "Оплачено",
                "claim_status_en": "Paid",
                "provider": "Uzbekinvest",
                "provider_ru": "Узбекинвест",
                "provider_en": "Uzbekinvest",
                "claim_number": "UC-2023-00456",
                "payout_amount": Decimal("2000000"),
            },
        )

        a2, _ = Accident.objects.update_or_create(
            vehicle=v2, date=date(2024, 1, 20),
            defaults={
                "vehicle_check": c2,
                "severity": "moderate",
                "damage_area": "Old eshik (chap)",
                "damage_area_ru": "Передняя дверь (левая)",
                "damage_area_en": "Front door (left)",
                "damage_description": "Yon ta'sir, eshik va oyna shikastlangan",
                "damage_description_ru": "Боковой удар, повреждены дверь и стекло",
                "damage_description_en": "Side impact, door and window damaged",
                "repair_cost": Decimal("8500000"),
                "accident_type": "Yon ta'sir",
                "accident_type_ru": "Боковой удар",
                "accident_type_en": "Side impact",
                "location": "Toshkent, Sergeli tumani",
                "location_ru": "Ташкент, Сергелийский район",
                "location_en": "Tashkent, Sergeli district",
                "impact_zone": "Chap yon",
                "impact_zone_ru": "Левый бок",
                "impact_zone_en": "Left side",
                "airbags_deployed": True,
                "repair_status": "Ta'mirlangan",
                "repair_status_ru": "Отремонтировано",
                "repair_status_en": "Repaired",
                "police_report_number": "PR-2024-01234",
                "damage_areas": ["Chap old eshik", "Chap oyna", "Chap ustun"],
                "damage_areas_ru": ["Левая передняя дверь", "Левое стекло", "Левая стойка"],
                "damage_areas_en": ["Left front door", "Left window", "Left pillar"],
            },
        )
        InsuranceClaim.objects.update_or_create(
            accident=a2,
            defaults={
                "claim_status": "To'langan",
                "claim_status_ru": "Оплачено",
                "claim_status_en": "Paid",
                "provider": "Alskom sug'urta",
                "provider_ru": "Алском страхование",
                "provider_en": "Alskom Insurance",
                "claim_number": "AL-2024-00789",
                "payout_amount": Decimal("7000000"),
            },
        )

        # Mileage for v2
        for d, km, src, src_ru, src_en in [
            (date(2022, 1, 10), 50, "Diler markazi", "Дилерский центр", "Dealer center"),
            (date(2023, 3, 15), 18500, "Texnik ko'rik", "Техосмотр", "Technical inspection"),
            (date(2023, 11, 20), 35200, "Servis markazi", "Сервисный центр", "Service center"),
            (date(2024, 7, 8), 51800, "Texnik ko'rik", "Техосмотр", "Technical inspection"),
        ]:
            MileageRecord.objects.update_or_create(
                vehicle=v2, date=d,
                defaults={
                    "vehicle_check": c2, "mileage_km": km,
                    "source": src, "source_ru": src_ru, "source_en": src_en,
                },
            )
        # Service records for v2
        for d, desc, desc_ru, desc_en, loc, loc_ru, loc_en, cost in [
            (date(2022, 6, 15), "Birinchi texnik xizmat", "Первое ТО",
             "First maintenance", "Kia Toshkent", "Киа Ташкент",
             "Kia Tashkent", Decimal("500000")),
            (date(2023, 5, 25), "Orqa bamper almashtirish (avariya)",
             "Замена заднего бампера (авария)", "Rear bumper replacement (accident)",
             "AutoBody Pro", "АвтоБоди Про", "AutoBody Pro", Decimal("2500000")),
            (date(2024, 2, 10), "Eshik va oyna ta'mirlash (avariya)",
             "Ремонт двери и стекла (авария)", "Door and window repair (accident)",
             "AutoBody Pro", "АвтоБоди Про", "AutoBody Pro", Decimal("8500000")),
            (date(2024, 7, 8), "Moy almashtirish", "Замена масла",
             "Oil change", "Kia Toshkent", "Киа Ташкент",
             "Kia Tashkent", Decimal("450000")),
        ]:
            ServiceRecord.objects.update_or_create(
                vehicle=v2, date=d,
                defaults={
                    "vehicle_check": c2,
                    "description": desc, "description_ru": desc_ru, "description_en": desc_en,
                    "location": loc, "location_ru": loc_ru, "location_en": loc_en,
                    "cost": cost,
                },
            )
        # Legal status for v2 - clean
        LegalStatus.objects.update_or_create(
            vehicle=v2,
            defaults={
                "vehicle_check": c2,
                "liens": "Yo'q", "liens_ru": "Нет", "liens_en": "None",
                "liens_status": "clean",
                "restrictions": "Yo'q", "restrictions_ru": "Нет", "restrictions_en": "None",
                "restrictions_status": "clean",
                "theft_records": "Qayd etilmagan", "theft_records_ru": "Не зафиксировано",
                "theft_records_en": "Not reported",
                "theft_status": "clean",
                "registration": "Faol", "registration_ru": "Активна",
                "registration_en": "Active",
                "registration_status": "clean",
            },
        )

        # --- Vehicle 3: Hyundai Sonata (major issues, legal problems) ---
        v3, _ = Vehicle.objects.update_or_create(
            vin="5NPE34AF3HH987654",
            defaults={
                "plate": "40C999CC",
                "make": "Hyundai", "make_ru": "Хёндай", "make_en": "Hyundai",
                "model": "Sonata", "model_ru": "Соната", "model_en": "Sonata",
                "year": 2018,
                "color": "Kumush", "color_ru": "Серебристый", "color_en": "Silver",
                "body_type": "Sedan", "body_type_ru": "Седан", "body_type_en": "Sedan",
                "engine_type": "Benzin", "engine_type_ru": "Бензин", "engine_type_en": "Gasoline",
                "engine_volume": "2.0L",
                "transmission": "Avtomat", "transmission_ru": "Автомат", "transmission_en": "Automatic",
                "drive_type": "Oldingi", "drive_type_ru": "Передний", "drive_type_en": "Front-wheel",
            },
        )
        c3, _ = VehicleCheck.objects.update_or_create(
            vehicle=v3,
            defaults={
                "accidents_count": 3,
                "accidents_status": "warning",
                "mileage_status": "warning",
                "legal_status": "issues_found",
                "service_records_count": 5,
                "taxi_usage": True,
            },
        )
        # Accidents for v3
        Accident.objects.update_or_create(
            vehicle=v3, date=date(2019, 8, 3),
            defaults={
                "vehicle_check": c3,
                "severity": "minor",
                "damage_area": "Old bamper",
                "damage_area_ru": "Передний бампер",
                "damage_area_en": "Front bumper",
                "damage_description": "Engil to'qnashuv parkovkada",
                "damage_description_ru": "Лёгкое столкновение на парковке",
                "damage_description_en": "Light collision in parking lot",
                "repair_cost": Decimal("1500000"),
                "accident_type": "Parkovka hodisasi",
                "accident_type_ru": "Парковочный инцидент",
                "accident_type_en": "Parking incident",
                "location": "Samarqand, Registon maydoni",
                "location_ru": "Самарканд, площадь Регистан",
                "location_en": "Samarkand, Registan square",
                "impact_zone": "Old",
                "impact_zone_ru": "Передняя часть",
                "impact_zone_en": "Front",
                "airbags_deployed": False,
                "repair_status": "Ta'mirlangan",
                "repair_status_ru": "Отремонтировано",
                "repair_status_en": "Repaired",
                "damage_areas": ["Old bamper"],
                "damage_areas_ru": ["Передний бампер"],
                "damage_areas_en": ["Front bumper"],
            },
        )
        Accident.objects.update_or_create(
            vehicle=v3, date=date(2021, 3, 15),
            defaults={
                "vehicle_check": c3,
                "severity": "major",
                "damage_area": "Old qism to'liq",
                "damage_area_ru": "Вся передняя часть",
                "damage_area_en": "Full front end",
                "damage_description": "Jiddiy to'qnashuv, kaput va radiator shikastlangan",
                "damage_description_ru": "Серьёзное столкновение, повреждены капот и радиатор",
                "damage_description_en": "Serious collision, hood and radiator damaged",
                "repair_cost": Decimal("25000000"),
                "accident_type": "Old to'qnashuv",
                "accident_type_ru": "Лобовое столкновение",
                "accident_type_en": "Head-on collision",
                "location": "Toshkent-Samarqand yo'li",
                "location_ru": "Трасса Ташкент-Самарканд",
                "location_en": "Tashkent-Samarkand highway",
                "impact_zone": "Old",
                "impact_zone_ru": "Передняя часть",
                "impact_zone_en": "Front",
                "airbags_deployed": True,
                "repair_status": "Ta'mirlangan",
                "repair_status_ru": "Отремонтировано",
                "repair_status_en": "Repaired",
                "police_report_number": "PR-2021-05678",
                "damage_areas": ["Kaput", "Radiator", "Old bamper", "Old faralar"],
                "damage_areas_ru": ["Капот", "Радиатор", "Передний бампер", "Передние фары"],
                "damage_areas_en": ["Hood", "Radiator", "Front bumper", "Headlights"],
            },
        )
        Accident.objects.update_or_create(
            vehicle=v3, date=date(2023, 11, 28),
            defaults={
                "vehicle_check": c3,
                "severity": "moderate",
                "damage_area": "O'ng yon",
                "damage_area_ru": "Правый бок",
                "damage_area_en": "Right side",
                "damage_description": "Chorrahada yon ta'sir",
                "damage_description_ru": "Боковой удар на перекрёстке",
                "damage_description_en": "Side impact at intersection",
                "repair_cost": Decimal("12000000"),
                "accident_type": "Yon ta'sir",
                "accident_type_ru": "Боковой удар",
                "accident_type_en": "Side impact",
                "location": "Toshkent, Yunusobod tumani",
                "location_ru": "Ташкент, Юнусабадский район",
                "location_en": "Tashkent, Yunusabad district",
                "impact_zone": "O'ng yon",
                "impact_zone_ru": "Правый бок",
                "impact_zone_en": "Right side",
                "airbags_deployed": True,
                "repair_status": "Qisman ta'mirlangan",
                "repair_status_ru": "Частично отремонтировано",
                "repair_status_en": "Partially repaired",
                "police_report_number": "PR-2023-09876",
                "damage_areas": ["O'ng old eshik", "O'ng orqa eshik", "O'ng oyna"],
                "damage_areas_ru": ["Правая передняя дверь", "Правая задняя дверь", "Правое стекло"],
                "damage_areas_en": ["Right front door", "Right rear door", "Right window"],
            },
        )
        # Mileage for v3 (suspicious gaps indicating possible rollback)
        for d, km, src, src_ru, src_en in [
            (date(2018, 6, 1), 100, "Diler markazi", "Дилерский центр", "Dealer center"),
            (date(2019, 5, 10), 45000, "Texnik ko'rik", "Техосмотр", "Technical inspection"),
            (date(2020, 4, 22), 92000, "Servis markazi", "Сервисный центр", "Service center"),
            (date(2021, 8, 15), 78000, "Texnik ko'rik", "Техосмотр", "Technical inspection"),
            (date(2022, 12, 1), 110000, "Servis markazi", "Сервисный центр", "Service center"),
            (date(2024, 3, 20), 135000, "Texnik ko'rik", "Техосмотр", "Technical inspection"),
        ]:
            MileageRecord.objects.update_or_create(
                vehicle=v3, date=d,
                defaults={
                    "vehicle_check": c3, "mileage_km": km,
                    "source": src, "source_ru": src_ru, "source_en": src_en,
                },
            )
        # Service records for v3
        for d, desc, desc_ru, desc_en, loc, loc_ru, loc_en, cost in [
            (date(2019, 5, 10), "Moy almashtirish", "Замена масла",
             "Oil change", "Hyundai Samarqand", "Хёндай Самарканд",
             "Hyundai Samarkand", Decimal("300000")),
            (date(2020, 4, 22), "Tormoz tizimi ta'mirlash", "Ремонт тормозной системы",
             "Brake system repair", "AutoService", "АвтоСервис",
             "AutoService", Decimal("1200000")),
            (date(2021, 8, 15), "Kaput va radiator almashtirish (avariya)",
             "Замена капота и радиатора (авария)",
             "Hood and radiator replacement (accident)",
             "Hyundai Toshkent", "Хёндай Ташкент",
             "Hyundai Tashkent", Decimal("25000000")),
            (date(2022, 12, 1), "To'liq texnik xizmat",
             "Полное техническое обслуживание", "Full maintenance",
             "Hyundai Toshkent", "Хёндай Ташкент",
             "Hyundai Tashkent", Decimal("2000000")),
            (date(2024, 1, 15), "O'ng eshiklar ta'mirlash (avariya)",
             "Ремонт правых дверей (авария)", "Right doors repair (accident)",
             "AutoBody Master", "АвтоБоди Мастер",
             "AutoBody Master", Decimal("12000000")),
        ]:
            ServiceRecord.objects.update_or_create(
                vehicle=v3, date=d,
                defaults={
                    "vehicle_check": c3,
                    "description": desc, "description_ru": desc_ru, "description_en": desc_en,
                    "location": loc, "location_ru": loc_ru, "location_en": loc_en,
                    "cost": cost,
                },
            )
        # Legal status for v3 - has issues
        LegalStatus.objects.update_or_create(
            vehicle=v3,
            defaults={
                "vehicle_check": c3,
                "liens": "Bank garovi mavjud",
                "liens_ru": "Имеется банковский залог",
                "liens_en": "Bank lien present",
                "liens_status": "issue",
                "restrictions": "Yo'q",
                "restrictions_ru": "Нет",
                "restrictions_en": "None",
                "restrictions_status": "clean",
                "theft_records": "Qayd etilmagan",
                "theft_records_ru": "Не зафиксировано",
                "theft_records_en": "Not reported",
                "theft_status": "clean",
                "registration": "Faol (taksi sifatida ro'yxatdan o'tgan)",
                "registration_ru": "Активна (зарегистрирована как такси)",
                "registration_en": "Active (registered as taxi)",
                "registration_status": "issue",
            },
        )

        self.stdout.write(self.style.SUCCESS(
            "Sample data created: 3 vehicles with full reports in uz/ru/en"
        ))
