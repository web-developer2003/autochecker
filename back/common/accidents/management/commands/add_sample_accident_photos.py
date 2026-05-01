"""Management command to generate sample accident photos for existing accidents."""
import io
import random

from django.core.files.base import ContentFile
from django.core.management.base import BaseCommand

from common.accidents.models import Accident
from common.accidents.models import AccidentPhoto

try:
    from PIL import Image, ImageDraw, ImageFont
except ImportError:
    Image = None


COLORS = {
    "minor": (255, 200, 50),
    "moderate": (255, 140, 0),
    "major": (220, 50, 50),
}

LABELS = [
    "Front bumper damage",
    "Rear collision",
    "Side impact - driver side",
    "Side impact - passenger side",
    "Hood dent",
    "Fender scratch",
    "Door panel damage",
    "Trunk damage",
    "Windshield crack",
    "Headlight broken",
]


def generate_placeholder_image(accident, label, index):
    """Generate a colored placeholder image with text overlay."""
    if Image is None:
        # Fallback: create a minimal 1x1 PNG
        return _minimal_png()

    width, height = 800, 600
    color = COLORS.get(accident.severity, (150, 150, 150))
    bg = (color[0] // 3, color[1] // 3, color[2] // 3)

    img = Image.new("RGB", (width, height), bg)
    draw = ImageDraw.Draw(img)

    # Draw a simple "car damage" rectangle
    rect_x = random.randint(100, 400)
    rect_y = random.randint(100, 300)
    draw.rectangle(
        [rect_x, rect_y, rect_x + 250, rect_y + 200],
        outline=color, width=4,
    )
    draw.line(
        [rect_x, rect_y, rect_x + 250, rect_y + 200],
        fill=color, width=2,
    )
    draw.line(
        [rect_x + 250, rect_y, rect_x, rect_y + 200],
        fill=color, width=2,
    )

    # Text
    try:
        font = ImageFont.truetype("arial.ttf", 28)
        font_small = ImageFont.truetype("arial.ttf", 18)
    except (OSError, IOError):
        font = ImageFont.load_default()
        font_small = font

    plate = accident.vehicle.plate or accident.vehicle.vin[:10]
    draw.text((30, 30), f"Accident Photo #{index + 1}", fill="white", font=font)
    draw.text((30, 70), f"Vehicle: {plate}", fill=color, font=font_small)
    draw.text((30, 100), f"Severity: {accident.severity.upper()}", fill=color, font=font_small)
    draw.text((30, 130), f"Date: {accident.date}", fill="white", font=font_small)
    draw.text((30, height - 50), label, fill="white", font=font_small)

    buf = io.BytesIO()
    img.save(buf, format="JPEG", quality=85)
    return buf.getvalue()


def _minimal_png():
    """1x1 red PNG fallback when Pillow is not available."""
    import struct
    import zlib

    def chunk(chunk_type, data):
        c = chunk_type + data
        return struct.pack(">I", len(data)) + c + struct.pack(">I", zlib.crc32(c) & 0xFFFFFFFF)

    signature = b"\x89PNG\r\n\x1a\n"
    ihdr = chunk(b"IHDR", struct.pack(">IIBBBBB", 1, 1, 8, 2, 0, 0, 0))
    idat = chunk(b"IDAT", zlib.compress(b"\x00\xff\x00\x00"))
    iend = chunk(b"IEND", b"")
    return signature + ihdr + idat + iend


class Command(BaseCommand):
    help = "Add sample placeholder photos to existing accidents that have no photos."

    def add_arguments(self, parser):
        parser.add_argument(
            "--all",
            action="store_true",
            help="Add photos to ALL accidents (even those that already have photos).",
        )
        parser.add_argument(
            "--count",
            type=int,
            default=2,
            help="Number of photos to generate per accident (default: 2).",
        )

    def handle(self, *args, **options):
        add_to_all = options["all"]
        count = options["count"]

        if add_to_all:
            accidents = Accident.objects.all()
        else:
            accidents = Accident.objects.filter(photos__isnull=True).distinct()

        if not accidents.exists():
            self.stdout.write(self.style.WARNING("No accidents found to add photos to."))
            return

        total = 0
        for accident in accidents:
            for i in range(count):
                label = random.choice(LABELS)
                image_data = generate_placeholder_image(accident, label, i)
                filename = f"sample_{accident.pk}_{i + 1}.jpg"

                photo = AccidentPhoto(
                    accident=accident,
                    description=label,
                )
                photo.photo.save(filename, ContentFile(image_data), save=True)
                total += 1

            self.stdout.write(
                f"  Added {count} photos to Accident #{accident.pk} "
                f"({accident.vehicle.plate or accident.vehicle.vin}) - {accident.severity}"
            )

        self.stdout.write(self.style.SUCCESS(f"\nDone! Added {total} sample photos to {accidents.count()} accidents."))
