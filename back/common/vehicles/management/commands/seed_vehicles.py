"""Seed 100 vehicles with mileage, service records, accidents, and photos."""
import io
import random
from datetime import date
from decimal import Decimal

from django.core.files.base import ContentFile
from django.core.management.base import BaseCommand

from common.accidents.models import Accident
from common.accidents.models import AccidentPhoto
from common.accidents.models import InsuranceClaim
from common.checks.models import VehicleCheck
from common.legal.models import LegalStatus
from common.mileage.models import MileageRecord
from common.service.models import ServiceRecord
from common.vehicles.models import Vehicle

RNG = random.Random(42)

# --------------------------------------------------------------------------- #
# Data pools                                                                    #
# --------------------------------------------------------------------------- #

REGIONS = [
    "01", "10", "20", "25", "30",
    "40", "50", "60", "70", "80", "90", "95",
]

MAKES = [
    {
        "uz": "Chevrolet", "ru": "Шевроле", "en": "Chevrolet",
        "models": [
            {"uz": "Cobalt",  "ru": "Кобальт",  "en": "Cobalt",  "years": [2017, 2018, 2019, 2020, 2021, 2022], "engine": "1.5L"},
            {"uz": "Malibu",  "ru": "Малибу",   "en": "Malibu",  "years": [2017, 2018, 2019, 2020, 2021],       "engine": "1.5L"},
            {"uz": "Tracker", "ru": "Трекер",   "en": "Tracker", "years": [2020, 2021, 2022, 2023],             "engine": "1.0L"},
            {"uz": "Equinox", "ru": "Эквинокс", "en": "Equinox", "years": [2019, 2020, 2021, 2022],             "engine": "1.5L"},
            {"uz": "Spark",   "ru": "Спарк",    "en": "Spark",   "years": [2016, 2017, 2018, 2019],             "engine": "0.8L"},
        ],
    },
    {
        "uz": "Kia", "ru": "Киа", "en": "Kia",
        "models": [
            {"uz": "K5",       "ru": "K5",          "en": "K5",       "years": [2020, 2021, 2022, 2023],       "engine": "2.0L"},
            {"uz": "Sportage", "ru": "Спортейдж",   "en": "Sportage", "years": [2018, 2019, 2020, 2021, 2022], "engine": "2.0L"},
            {"uz": "Rio",      "ru": "Рио",         "en": "Rio",      "years": [2016, 2017, 2018, 2019],       "engine": "1.6L"},
            {"uz": "Cerato",   "ru": "Черато",      "en": "Cerato",   "years": [2017, 2018, 2019, 2020],       "engine": "1.6L"},
        ],
    },
    {
        "uz": "Hyundai", "ru": "Хёндай", "en": "Hyundai",
        "models": [
            {"uz": "Tucson",  "ru": "Туксон",  "en": "Tucson",  "years": [2018, 2019, 2020, 2021, 2022], "engine": "2.0L"},
            {"uz": "Sonata",  "ru": "Соната",  "en": "Sonata",  "years": [2017, 2018, 2019, 2020],       "engine": "2.0L"},
            {"uz": "Accent",  "ru": "Акцент",  "en": "Accent",  "years": [2015, 2016, 2017, 2018],       "engine": "1.6L"},
            {"uz": "Elantra", "ru": "Элантра", "en": "Elantra", "years": [2017, 2018, 2019, 2020],       "engine": "1.6L"},
            {"uz": "Creta",   "ru": "Крета",   "en": "Creta",   "years": [2018, 2019, 2020, 2021],       "engine": "1.6L"},
        ],
    },
    {
        "uz": "Toyota", "ru": "Тойота", "en": "Toyota",
        "models": [
            {"uz": "Camry",   "ru": "Камри",   "en": "Camry",   "years": [2017, 2018, 2019, 2020, 2021], "engine": "2.5L"},
            {"uz": "Corolla", "ru": "Королла", "en": "Corolla", "years": [2016, 2017, 2018, 2019, 2020], "engine": "1.6L"},
            {"uz": "RAV4",    "ru": "РАВ4",    "en": "RAV4",    "years": [2018, 2019, 2020, 2021],       "engine": "2.0L"},
        ],
    },
    {
        "uz": "Nexia", "ru": "Нексия", "en": "Nexia",
        "models": [
            {"uz": "N3", "ru": "N3", "en": "N3", "years": [2020, 2021, 2022], "engine": "1.5L"},
        ],
    },
    {
        "uz": "Gentra", "ru": "Джентра", "en": "Gentra",
        "models": [
            {"uz": "Gentra", "ru": "Джентра", "en": "Gentra", "years": [2015, 2016, 2017, 2018, 2019], "engine": "1.5L"},
        ],
    },
]

COLORS = [
    ("Oq", "Белый", "White"),
    ("Qora", "Чёрный", "Black"),
    ("Kumush", "Серебристый", "Silver"),
    ("Kulrang", "Серый", "Gray"),
    ("Qizil", "Красный", "Red"),
    ("Ko'k", "Синий", "Blue"),
    ("Yashil", "Зелёный", "Green"),
    ("Jigarrang", "Коричневый", "Brown"),
]

TRANSMISSIONS = [
    ("Avtomat", "Автомат", "Automatic"),
    ("Mexanik", "Механика", "Manual"),
    ("Variator", "Вариатор", "CVT"),
]

DRIVE_TYPES = [
    ("Oldingi", "Передний", "Front-wheel"),
    ("Orqa", "Задний", "Rear-wheel"),
    ("To'liq", "Полный", "All-wheel"),
]

BODY_TYPES = [
    ("Sedan", "Седан", "Sedan"),
    ("Krossover", "Кроссовер", "Crossover"),
    ("Hetchbek", "Хэтчбек", "Hatchback"),
    ("SUV", "Внедорожник", "SUV"),
]

ENGINE_TYPES = [
    ("Benzin", "Бензин", "Gasoline"),
    ("Dizel", "Дизель", "Diesel"),
    ("Gibrid", "Гибрид", "Hybrid"),
]

DAMAGE_AREAS = [
    ("Old bamper",         "Передний бампер",        "Front bumper"),
    ("Orqa bamper",        "Задний бампер",           "Rear bumper"),
    ("Old eshik (chap)",   "Передняя дверь (лев.)",   "Front door (left)"),
    ("Old eshik (o'ng)",   "Передняя дверь (пр.)",    "Front door (right)"),
    ("Orqa eshik (chap)",  "Задняя дверь (лев.)",     "Rear door (left)"),
    ("Orqa eshik (o'ng)",  "Задняя дверь (пр.)",      "Rear door (right)"),
    ("Kaput",              "Капот",                   "Hood"),
    ("Chap yon",           "Левый бок",               "Left side"),
    ("O'ng yon",           "Правый бок",              "Right side"),
    ("Tomi",               "Крыша",                   "Roof"),
]

ACCIDENT_TYPES = [
    ("To'qnashuv",         "Столкновение",            "Collision"),
    ("Yon ta'sir",         "Боковой удар",            "Side impact"),
    ("Old to'qnashuv",     "Лобовое столкновение",    "Head-on collision"),
    ("Orqa ta'sir",        "Удар сзади",              "Rear impact"),
    ("Parkovka hodisasi",  "Парковочный инцидент",    "Parking incident"),
]

IMPACT_ZONES = [
    ("Old",       "Передняя часть", "Front"),
    ("Orqa",      "Задняя часть",   "Rear"),
    ("Chap yon",  "Левый бок",      "Left side"),
    ("O'ng yon",  "Правый бок",     "Right side"),
]

REPAIR_STATUSES = [
    ("Ta'mirlangan",         "Отремонтировано",            "Repaired"),
    ("Qisman ta'mirlangan",  "Частично отремонтировано",   "Partially repaired"),
    ("Ta'mirlanmagan",       "Не отремонтировано",         "Not repaired"),
]

LOCATIONS = [
    ("Toshkent, Chilonzor tumani",         "Ташкент, Чиланзарский р-н",      "Tashkent, Chilanzar district"),
    ("Toshkent, Yunusobod tumani",         "Ташкент, Юнусабадский р-н",      "Tashkent, Yunusabad district"),
    ("Toshkent, Mirzo Ulug'bek tumani",    "Ташкент, р-н Мирзо Улугбека",    "Tashkent, Mirzo Ulugbek district"),
    ("Toshkent, Shayxontohur tumani",      "Ташкент, Шайхантахурский р-н",   "Tashkent, Shaykhantakhur district"),
    ("Samarqand, Registon ko'chasi",       "Самарканд, ул. Регистан",        "Samarkand, Registan street"),
    ("Buxoro, Ark ko'chasi",               "Бухара, ул. Арк",                "Bukhara, Ark street"),
    ("Andijon, Navoi ko'chasi",            "Андижан, ул. Навои",             "Andijan, Navoi street"),
    ("Farg'ona, Mustaqillik ko'chasi",     "Фергана, ул. Мустакиллик",       "Fergana, Mustaqillik street"),
    ("Namangan, Istiqlol ko'chasi",        "Наманган, ул. Истикляль",        "Namangan, Istiqlol street"),
    ("Toshkent-Samarqand magistral yo'l",  "Трасса Ташкент-Самарканд",       "Tashkent-Samarkand highway"),
    ("Toshkent-Andijon magistral yo'l",    "Трасса Ташкент-Андижан",         "Tashkent-Andijan highway"),
]

SERVICE_TYPES = [
    ("Moy almashtirish",                   "Замена масла",              "Oil change"),
    ("Tormoz nakladkalari almashtirish",   "Замена тормозных колодок",  "Brake pad replacement"),
    ("Shinalar almashtirish",              "Замена шин",                "Tire replacement"),
    ("Havo filtri almashtirish",           "Замена воздушного фильтра", "Air filter replacement"),
    ("To'liq texnik xizmat",              "Полное ТО",                 "Full maintenance"),
    ("Akkumulyator almashtirish",          "Замена аккумулятора",       "Battery replacement"),
    ("Muallaq tizim ta'mirlash",          "Ремонт подвески",           "Suspension repair"),
    ("Dvigatel ta'mirlash",               "Ремонт двигателя",          "Engine repair"),
]

SERVICE_LOCATIONS = [
    ("Chevrolet Toshkent",      "Шевроле Ташкент",        "Chevrolet Tashkent"),
    ("Kia Toshkent",            "Киа Ташкент",            "Kia Tashkent"),
    ("Hyundai Toshkent",        "Хёндай Ташкент",         "Hyundai Tashkent"),
    ("Toyota Toshkent",         "Тойота Ташкент",         "Toyota Tashkent"),
    ("AutoService Pro",         "АвтоСервис Про",         "AutoService Pro"),
    ("AutoBody Master",         "АвтоБоди Мастер",        "AutoBody Master"),
    ("Rapid Servis",            "Рапид Сервис",           "Rapid Service"),
    ("Texnik ko'rik markazi",  "Центр техосмотра",        "Technical inspection center"),
]

MILEAGE_SOURCES = [
    ("Diler markazi",        "Дилерский центр",     "Dealer center"),
    ("Texnik ko'rik",       "Техосмотр",            "Technical inspection"),
    ("Servis markazi",       "Сервисный центр",     "Service center"),
    ("Sotuv vaqtida",        "При продаже",         "At sale"),
    ("Sug'urta tekshiruvi", "Страховая проверка",   "Insurance check"),
]

INSURANCE_PROVIDERS = [
    ("Uzbekinvest",      "Узбекинвест",        "Uzbekinvest"),
    ("Alskom sug'urta",  "Алском страхование", "Alskom Insurance"),
    ("Kafolat sug'urta", "Кафолат страхование","Kafolat Insurance"),
    ("ALFA sug'urta",    "АЛЬФА страхование",  "ALFA Insurance"),
]

CLAIM_STATUSES = [
    ("To'langan",          "Оплачено",        "Paid"),
    ("Ko'rib chiqilmoqda", "На рассмотрении", "Under review"),
]

LETTERS = "ABDEFGHJKLMNPRSTUVXYZ"

# --------------------------------------------------------------------------- #
# Helpers                                                                       #
# --------------------------------------------------------------------------- #

def _gen_plate(used: set) -> str:
    while True:
        region = RNG.choice(REGIONS)
        mid = RNG.choice(LETTERS)
        num = RNG.randint(100, 999)
        end = "".join(RNG.choices(LETTERS, k=2))
        plate = f"{region}{mid}{num}{end}"
        if plate not in used:
            used.add(plate)
            return plate


def _gen_vin(index: int, used: set) -> str:
    chars = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789"
    while True:
        suffix = "".join(RNG.choices(chars, k=8))
        vin = f"UZ2024{index:03d}{suffix}"
        if vin not in used:
            used.add(vin)
            return vin


def _spread_year(start: int, end: int, index: int, total: int) -> int:
    span = max(end - start, 1)
    return min(start + int(index * span / max(total, 1)), end)


def _gen_image(plate: str, severity: str, idx: int) -> bytes:
    try:
        from PIL import Image, ImageDraw  # noqa: PLC0415

        colors = {"minor": (255, 220, 50), "moderate": (255, 140, 0), "major": (200, 50, 50)}
        bg = colors.get(severity, (180, 180, 180))

        img = Image.new("RGB", (640, 480), color=bg)
        draw = ImageDraw.Draw(img)
        # Car silhouette
        draw.rectangle([80, 180, 560, 360], fill=(90, 90, 90))
        draw.rectangle([150, 130, 490, 190], fill=(90, 90, 90))
        draw.ellipse([100, 330, 180, 390], fill=(40, 40, 40))
        draw.ellipse([460, 330, 540, 390], fill=(40, 40, 40))
        # Damage mark
        dmg_color = (50, 0, 0) if severity == "major" else (120, 60, 0)
        draw.ellipse([270, 220, 370, 300], fill=dmg_color)
        # Labels
        draw.rectangle([0, 0, 640, 45], fill=(0, 0, 0))
        draw.text(
            (10, 12),
            f"PLATE: {plate}  |  {severity.upper()}  |  PHOTO #{idx + 1}",
            fill=(255, 255, 255),
        )
        draw.text((10, 455), "AUTOCHECKER - SAMPLE ACCIDENT PHOTO", fill=(0, 0, 0))

        buf = io.BytesIO()
        img.save(buf, format="JPEG", quality=80)
        return buf.getvalue()
    except ImportError:
        # Minimal valid 1×1 PNG
        return (
            b"\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01"
            b"\x08\x02\x00\x00\x00\x90wS\xde\x00\x00\x00\x0cIDATx\x9cc\xf8\x0f\x00"
            b"\x00\x01\x01\x00\x05\x18\xd8N\x00\x00\x00\x00IEND\xaeB`\x82"
        )


# --------------------------------------------------------------------------- #
# Command                                                                       #
# --------------------------------------------------------------------------- #

class Command(BaseCommand):
    help = "Seed 100 vehicles with mileage, service records, accidents, and photos"

    def add_arguments(self, parser):
        parser.add_argument(
            "--clear",
            action="store_true",
            help="Delete previously seeded vehicles (VINs starting with UZ2024) before re-seeding",
        )

    def handle(self, *args, **options):  # noqa: C901, PLR0912, PLR0915
        if options["clear"]:
            deleted, _ = Vehicle.objects.filter(vin__startswith="UZ2024").delete()
            self.stdout.write(f"Cleared {deleted} previously seeded vehicles.")

        used_plates: set = set(Vehicle.objects.values_list("plate", flat=True))
        used_vins: set = set(Vehicle.objects.values_list("vin", flat=True))

        # 55 clean, 30 with accidents, 15 problematic
        profiles = ["clean"] * 55 + ["accidents"] * 30 + ["problematic"] * 15
        RNG.shuffle(profiles)

        created_count = 0

        for i, profile in enumerate(profiles):
            plate = _gen_plate(used_plates)
            vin = _gen_vin(i, used_vins)

            make_data = RNG.choice(MAKES)
            model_data = RNG.choice(make_data["models"])
            year = RNG.choice(model_data["years"])
            color = RNG.choice(COLORS)
            transmission = RNG.choice(TRANSMISSIONS)
            drive_type = RNG.choice(DRIVE_TYPES)
            body_type = RNG.choice(BODY_TYPES)
            engine_type = RNG.choice(ENGINE_TYPES)

            if profile == "clean":
                acc_count = 0
                acc_status = "clean"
                mil_status = "clean"
                leg_status = "clean"
                taxi = False
            elif profile == "accidents":
                acc_count = RNG.randint(1, 2)
                acc_status = "issues_found"
                mil_status = RNG.choice(["clean", "clean", "warning"])
                leg_status = "clean"
                taxi = RNG.random() < 0.1
            else:  # problematic
                acc_count = RNG.randint(3, 5)
                acc_status = "warning"
                mil_status = RNG.choice(["warning", "issues_found"])
                leg_status = RNG.choice(["issues_found", "warning"])
                taxi = RNG.random() < 0.4

            svc_count = RNG.randint(2, 5)

            v, is_new = Vehicle.objects.get_or_create(
                vin=vin,
                defaults={
                    "plate": plate,
                    "make": make_data["uz"], "make_ru": make_data["ru"], "make_en": make_data["en"],
                    "model": model_data["uz"], "model_ru": model_data["ru"], "model_en": model_data["en"],
                    "year": year,
                    "color": color[0], "color_ru": color[1], "color_en": color[2],
                    "body_type": body_type[0], "body_type_ru": body_type[1], "body_type_en": body_type[2],
                    "engine_type": engine_type[0], "engine_type_ru": engine_type[1], "engine_type_en": engine_type[2],
                    "engine_volume": model_data["engine"],
                    "transmission": transmission[0], "transmission_ru": transmission[1], "transmission_en": transmission[2],
                    "drive_type": drive_type[0], "drive_type_ru": drive_type[1], "drive_type_en": drive_type[2],
                },
            )

            if not is_new:
                continue  # already seeded (idempotent)

            check = VehicleCheck.objects.create(
                vehicle=v,
                accidents_count=acc_count,
                accidents_status=acc_status,
                mileage_status=mil_status,
                legal_status=leg_status,
                service_records_count=svc_count,
                taxi_usage=taxi,
            )

            # -- Mileage records --
            km = RNG.randint(100, 1000)
            num_mil = RNG.randint(3, 5)
            for j in range(num_mil):
                rec_year = _spread_year(year, 2025, j, num_mil)
                d = date(rec_year, RNG.randint(1, 12), RNG.randint(1, 28))
                km += RNG.randint(8000, 25000)
                src = RNG.choice(MILEAGE_SOURCES)
                MileageRecord.objects.create(
                    vehicle=v, vehicle_check=check, date=d, mileage_km=km,
                    source=src[0], source_ru=src[1], source_en=src[2],
                )

            # -- Service records --
            for j in range(svc_count):
                rec_year = _spread_year(year, 2025, j, svc_count)
                d = date(rec_year, RNG.randint(1, 12), RNG.randint(1, 28))
                stype = RNG.choice(SERVICE_TYPES)
                sloc = RNG.choice(SERVICE_LOCATIONS)
                cost = Decimal(RNG.randint(3, 50) * 100_000)
                ServiceRecord.objects.create(
                    vehicle=v, vehicle_check=check, date=d, cost=cost,
                    description=stype[0], description_ru=stype[1], description_en=stype[2],
                    location=sloc[0], location_ru=sloc[1], location_en=sloc[2],
                )

            # -- Legal status --
            if leg_status == "clean":
                LegalStatus.objects.create(
                    vehicle=v, vehicle_check=check,
                    liens="Yo'q", liens_ru="Нет", liens_en="None", liens_status="clean",
                    restrictions="Yo'q", restrictions_ru="Нет", restrictions_en="None", restrictions_status="clean",
                    theft_records="Qayd etilmagan", theft_records_ru="Не зафиксировано",
                    theft_records_en="Not reported", theft_status="clean",
                    registration="Faol", registration_ru="Активна",
                    registration_en="Active", registration_status="clean",
                )
            else:
                has_lien = RNG.random() < 0.5
                has_restriction = RNG.random() < 0.3
                LegalStatus.objects.create(
                    vehicle=v, vehicle_check=check,
                    liens="Bank garovi mavjud" if has_lien else "Yo'q",
                    liens_ru="Имеется банковский залог" if has_lien else "Нет",
                    liens_en="Bank lien present" if has_lien else "None",
                    liens_status="issue" if has_lien else "clean",
                    restrictions="Harakatlanish taqiqi" if has_restriction else "Yo'q",
                    restrictions_ru="Запрет на эксплуатацию" if has_restriction else "Нет",
                    restrictions_en="Movement restriction" if has_restriction else "None",
                    restrictions_status="issue" if has_restriction else "clean",
                    theft_records="Qayd etilmagan", theft_records_ru="Не зафиксировано",
                    theft_records_en="Not reported", theft_status="clean",
                    registration="Faol (taksi)" if taxi else "Faol",
                    registration_ru="Активна (такси)" if taxi else "Активна",
                    registration_en="Active (taxi)" if taxi else "Active",
                    registration_status="issue" if taxi else "clean",
                )

            # -- Accidents --
            for j in range(acc_count):
                acc_year = _spread_year(min(year + 1, 2025), 2025, j, max(acc_count, 1))
                acc_date = date(acc_year, RNG.randint(1, 12), RNG.randint(1, 28))

                if profile == "problematic":
                    severity = RNG.choice(["minor", "moderate", "moderate", "major"])
                else:
                    severity = RNG.choice(["minor", "minor", "moderate"])

                area = RNG.choice(DAMAGE_AREAS)
                atype = RNG.choice(ACCIDENT_TYPES)
                loc = RNG.choice(LOCATIONS)
                iz = RNG.choice(IMPACT_ZONES)
                rs = RNG.choice(REPAIR_STATUSES[:2] if profile != "problematic" else REPAIR_STATUSES)
                repair_cost = Decimal(RNG.randint(10, 300) * 100_000)
                airbags = severity == "major" or (severity == "moderate" and RNG.random() < 0.3)
                police_report = (
                    f"PR-{acc_date.year}-{RNG.randint(10000, 99999)}"
                    if severity != "minor"
                    else ""
                )

                accident = Accident.objects.create(
                    vehicle=v, vehicle_check=check, date=acc_date, severity=severity,
                    damage_area=area[0], damage_area_ru=area[1], damage_area_en=area[2],
                    damage_description=f"{atype[0]}. {area[0]} shikastlangan.",
                    damage_description_ru=f"{atype[1]}. Повреждена {area[1].lower()}.",
                    damage_description_en=f"{atype[2]}. {area[2]} damaged.",
                    repair_cost=repair_cost,
                    accident_type=atype[0], accident_type_ru=atype[1], accident_type_en=atype[2],
                    location=loc[0], location_ru=loc[1], location_en=loc[2],
                    impact_zone=iz[0], impact_zone_ru=iz[1], impact_zone_en=iz[2],
                    airbags_deployed=airbags,
                    repair_status=rs[0], repair_status_ru=rs[1], repair_status_en=rs[2],
                    police_report_number=police_report,
                    damage_areas=[area[0]],
                    damage_areas_ru=[area[1]],
                    damage_areas_en=[area[2]],
                )

                # Photos
                for k in range(RNG.randint(1, 3)):
                    try:
                        img_data = _gen_image(plate, severity, k)
                        photo = AccidentPhoto(accident=accident, description=f"Photo {k + 1}")
                        photo.photo.save(
                            f"accident_{plate}_{j + 1}_{k + 1}.jpg",
                            ContentFile(img_data),
                            save=True,
                        )
                    except Exception as exc:  # noqa: BLE001
                        self.stderr.write(f"  Warning: could not save photo for {plate}: {exc}")

                # Insurance claim for moderate / major
                if severity in ("moderate", "major"):
                    provider = RNG.choice(INSURANCE_PROVIDERS)
                    cs = RNG.choice(CLAIM_STATUSES)
                    InsuranceClaim.objects.create(
                        accident=accident,
                        claim_status=cs[0], claim_status_ru=cs[1], claim_status_en=cs[2],
                        provider=provider[0], provider_ru=provider[1], provider_en=provider[2],
                        claim_number=f"INS-{acc_date.year}-{RNG.randint(10000, 99999)}",
                        payout_amount=repair_cost * Decimal("0.8"),
                    )

            created_count += 1
            if created_count % 10 == 0:
                self.stdout.write(f"  Seeded {created_count}/100...")

        self.stdout.write(self.style.SUCCESS(f"Done! Seeded {created_count} new vehicles."))
