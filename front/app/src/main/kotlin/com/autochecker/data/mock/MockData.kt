package com.autochecker.data.mock

import com.autochecker.data.model.*

object MockData {
    val user = User(
        id = 1,
        guid = "mock-user-guid",
        username = "aziz@example.com",
        firstName = "Aziz",
        lastName = "Karimov",
        email = "aziz@example.com",
        phone = "+998 90 123 45 67",
    )

    val sampleVehicle = Vehicle(
        id = 1,
        guid = "mock-vehicle-1",
        vin = "WBAPH5C55BA123456",
        plate = "01 A 123 AA",
        make = "Chevrolet",
        model = "Malibu",
        year = 2020,
        color = "Qora",
        bodyType = "Sedan",
        engineType = "Benzin",
        engineVolume = "1.5L Turbo",
        transmission = "Avtomat",
        driveType = "Old",
        isSaved = true,
    )

    val sampleVehicle2 = Vehicle(
        id = 2,
        guid = "mock-vehicle-2",
        vin = "JN1TBNT30Z0000001",
        plate = "01 B 456 BB",
        make = "Chevrolet",
        model = "Cobalt",
        year = 2022,
        color = "Oq",
        bodyType = "Sedan",
        engineType = "Benzin",
        engineVolume = "1.5L",
        transmission = "Mexanik",
        driveType = "Old",
        isSaved = true,
    )

    val sampleVehicle3 = Vehicle(
        id = 3,
        guid = "mock-vehicle-3",
        vin = "XTAKS015LM1234567",
        plate = "40 C 789 CC",
        make = "Kia",
        model = "K5",
        year = 2023,
        color = "Kumush",
        bodyType = "Sedan",
        engineType = "Benzin",
        engineVolume = "2.0L",
        transmission = "Avtomat",
        driveType = "Old",
    )

    val accidents = listOf(
        Accident(
            id = 1,
            guid = "mock-accident-1",
            date = "2022-03-15",
            location = "Toshkent, Chilonzor tumani",
            damageDescription = "Orqa qismiga urilish. Old oyna va bamper shikastlangan.",
            severity = AccidentSeverity.MODERATE,
            damageAreas = listOf("Orqa bamper", "Orqa oyna", "Bagaj qopqog'i"),
            repairCost = "4500000",
            repairStatus = "Ta'mirlangan",
            policeReportNumber = "AV-2022-03156",
        ),
        Accident(
            id = 2,
            guid = "mock-accident-2",
            date = "2023-08-22",
            location = "Samarqand, Registon ko'chasi",
            damageDescription = "Yengil to'qnashuv. Old bamper tirnalgan.",
            severity = AccidentSeverity.MINOR,
            damageAreas = listOf("Old bamper"),
            repairCost = "800000",
            repairStatus = "Ta'mirlangan",
            policeReportNumber = "AV-2023-08445",
        ),
    )

    val mileageRecords = listOf(
        MileageRecord(date = "2020-06-01", mileageKm = 0, source = "Zavoddan chiqish"),
        MileageRecord(date = "2021-03-10", mileageKm = 15200, source = "Servis markazi"),
        MileageRecord(date = "2022-01-20", mileageKm = 32400, source = "Texnik ko'rik"),
        MileageRecord(date = "2022-09-15", mileageKm = 48700, source = "Servis markazi"),
        MileageRecord(date = "2023-06-01", mileageKm = 67300, source = "Texnik ko'rik"),
        MileageRecord(date = "2024-01-10", mileageKm = 82100, source = "Sug'urta tekshiruvi"),
    )

    val serviceRecords = listOf(
        ServiceRecord(date = "2021-03-10", description = "Moy almashtirish, filtrlar", location = "Chevrolet Toshkent", cost = "350000"),
        ServiceRecord(date = "2022-01-20", description = "Moy, tormoz suyuqligi, svechalar", location = "Chevrolet Toshkent", cost = "800000"),
        ServiceRecord(date = "2022-09-15", description = "Katta texnik xizmat", location = "AutoService Pro", cost = "1500000"),
        ServiceRecord(date = "2023-06-01", description = "Tormoz kolodkalari almashtirish", location = "AutoService Pro", cost = "600000"),
    )

    val legalStatus = LegalStatus(
        liens = "Yo'q",
        liensStatus = "clean",
        restrictions = "Yo'q",
        restrictionsStatus = "clean",
        theftRecords = "Qayd etilmagan",
        theftStatus = "clean",
        registration = "Faol",
        registrationStatus = "clean",
    )

    val vehicleReport = VehicleReport(
        check = VehicleCheck(
            id = 1,
            guid = "mock-check-1",
            vehicle = sampleVehicle,
            accidentsCount = 2,
            accidentsStatus = "issues_found",
            mileageStatus = "clean",
            legalStatus = "clean",
            serviceRecordsCount = 4,
            taxiUsage = false,
        ),
        accidentSummary = AccidentSummary(
            totalAccidents = 2,
            majorCount = 0,
            minorCount = 1,
            moderateCount = 1,
            totalDamageCost = "5300000",
        ),
        accidents = accidents,
        mileageRecords = mileageRecords,
        legalStatus = legalStatus,
        serviceRecords = serviceRecords,
    )

    val searchHistory = listOf(
        SearchHistoryItem(
            id = 1,
            guid = "mock-history-1",
            vehicle = VehicleShort(
                id = 1, guid = "mock-vehicle-1", make = "Chevrolet",
                model = "Malibu", year = 2020, vin = sampleVehicle.vin,
                plate = "01 A 123 AA", displayName = "Chevrolet Malibu",
            ),
            createdAt = "2024-01-20",
        ),
        SearchHistoryItem(
            id = 2,
            guid = "mock-history-2",
            vehicle = VehicleShort(
                id = 2, guid = "mock-vehicle-2", make = "Chevrolet",
                model = "Cobalt", year = 2022, vin = sampleVehicle2.vin,
                plate = "01 B 456 BB", displayName = "Chevrolet Cobalt",
            ),
            createdAt = "2024-01-18",
        ),
        SearchHistoryItem(
            id = 3,
            guid = "mock-history-3",
            vehicle = VehicleShort(
                id = 3, guid = "mock-vehicle-3", make = "Kia",
                model = "K5", year = 2023, vin = sampleVehicle3.vin,
                plate = "40 C 789 CC", displayName = "Kia K5",
            ),
            createdAt = "2024-01-15",
        ),
    )

    val savedVehicles = listOf(sampleVehicle, sampleVehicle2)
}
