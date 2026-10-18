# MediFind Seed Data Script (Step 0)
$baseUrl = "http://localhost:8080/api"

Write-Host "--- Seeding Medicine Classes ---"
$mcNames = @("Antibiotics", "Painkillers", "Antimalarials", "Vitamins")
$mcMap = @{}
foreach ($name in $mcNames) {
    $res = Invoke-RestMethod -Uri "$baseUrl/medicine-classes/save" -Method Post -ContentType "application/json" -Body (@{name=$name} | ConvertTo-Json)
    Write-Host "Saved MedicineClass: $name -> $res"
}
$allMc = Invoke-RestMethod -Uri "$baseUrl/medicine-classes/all" -Method Get
foreach ($mc in $allMc) {
    $mcMap[$mc.name] = $mc
}

Write-Host "`n--- Seeding Service Areas ---"
$saData = @(
    @{ areaNumber = 1; name = "Nyarugenge Central" },
    @{ areaNumber = 2; name = "Gasabo North" },
    @{ areaNumber = 3; name = "Kicukiro South" },
    @{ areaNumber = 4; name = "Remera Free Zone" }
)
$saMap = @{}
foreach ($sa in $saData) {
    $res = Invoke-RestMethod -Uri "$baseUrl/service-areas/save" -Method Post -ContentType "application/json" -Body ($sa | ConvertTo-Json)
    Write-Host "Saved ServiceArea: $($sa.areaNumber) -> $res"
}
$allSa = Invoke-RestMethod -Uri "$baseUrl/service-areas/all" -Method Get
foreach ($sa in $allSa) {
    $saMap[$sa.areaNumber] = $sa
}

Write-Host "`n--- Seeding Pharmacies ---"
# Pharmacy 1: holds 2 medicine classes (Antibiotics, Painkillers), service area 1
$p1 = @{
    name = "Pharmacie Conseil"
    street = "KN 4 Ave"
    phone = "+250788111222"
    serviceArea = @{ id = $saMap[1].id }
    medicineClasses = @( @{ id = $mcMap["Antibiotics"].id }, @{ id = $mcMap["Painkillers"].id } )
}
$res1 = Invoke-RestMethod -Uri "$baseUrl/pharmacies/save" -Method Post -ContentType "application/json" -Body ($p1 | ConvertTo-Json)
Write-Host "Saved Pharmacy 1 -> $res1"

# Pharmacy 2: holds 1 medicine class (Antimalarials), service area 2
$p2 = @{
    name = "Kigali Pharma"
    street = "KG 9 Ave"
    phone = "+250788333444"
    serviceArea = @{ id = $saMap[2].id }
    medicineClasses = @( @{ id = $mcMap["Antimalarials"].id } )
}
$res2 = Invoke-RestMethod -Uri "$baseUrl/pharmacies/save" -Method Post -ContentType "application/json" -Body ($p2 | ConvertTo-Json)
Write-Host "Saved Pharmacy 2 -> $res2"

# Pharmacy 3: service area 3, holds Antibiotics
$p3 = @{
    name = "LifeCare Pharmacy"
    street = "KK 15 Rd"
    phone = "+250788555666"
    serviceArea = @{ id = $saMap[3].id }
    medicineClasses = @( @{ id = $mcMap["Antibiotics"].id } )
}
$res3 = Invoke-RestMethod -Uri "$baseUrl/pharmacies/save" -Method Post -ContentType "application/json" -Body ($p3 | ConvertTo-Json)
Write-Host "Saved Pharmacy 3 -> $res3"

$allPharmacies = Invoke-RestMethod -Uri "$baseUrl/pharmacies/all" -Method Get
$pharmMap = @{}
foreach ($p in $allPharmacies) {
    $pharmMap[$p.name] = $p
}

Write-Host "`n--- Seeding Customers ---"
$customers = @(
    @{ firstName = "Aline"; lastName = "Umutoni"; nationalId = "1199780054321098"; phone = "+250788000111" },
    @{ firstName = "Jean"; lastName = "Umutoni"; nationalId = "1199580012345678"; phone = "+250788000222" },
    @{ firstName = "Clarisse"; lastName = "Mugisha"; nationalId = "1199980076543210"; phone = "+250788000333" },
    @{ firstName = "Eric"; lastName = "Gasana"; nationalId = "1199480098765432"; phone = "+250788000444" },
    @{ firstName = "Diane"; lastName = "Kwizera"; nationalId = "1200080055443322"; phone = "+250788000555" }
)
$custMap = @{}
foreach ($c in $customers) {
    $res = Invoke-RestMethod -Uri "$baseUrl/customers/save" -Method Post -ContentType "application/json" -Body ($c | ConvertTo-Json)
    Write-Host "Saved Customer: $($c.firstName) $($c.lastName) -> $res"
}
$allCust = Invoke-RestMethod -Uri "$baseUrl/customers/all" -Method Get
foreach ($c in $allCust) {
    $custMap[$c.nationalId] = $c
}

Write-Host "`n--- Seeding Medicine Requests ---"
# 10 requests spread across October & November 2026.
# Customer 1 (Aline Umutoni) has at least 3 requests.
# Pharmacy 1 has two requests on 2026-10-20, one DECLINED.
# At least 2 requests FULFILLED.
# All 4 statuses used: SUBMITTED, MATCHED, FULFILLED, DECLINED.

$requests = @(
    # Customer 1, Pharmacy 1, 2026-10-20, DECLINED
    @{
        medicineName = "Amoxicillin 500mg"
        requestDate = "2026-10-20"
        status = "DECLINED"
        customer = @{ id = $custMap["1199780054321098"].id }
        pharmacy = @{ id = $pharmMap["Pharmacie Conseil"].id }
    },
    # Customer 1, Pharmacy 1, 2026-10-20, SUBMITTED (second on 2026-10-20)
    @{
        medicineName = "Paracetamol 500mg"
        requestDate = "2026-10-20"
        status = "SUBMITTED"
        customer = @{ id = $custMap["1199780054321098"].id }
        pharmacy = @{ id = $pharmMap["Pharmacie Conseil"].id }
    },
    # Customer 1, Pharmacy 2, 2026-10-22, FULFILLED (third request for Customer 1)
    @{
        medicineName = "Coartem 20/120mg"
        requestDate = "2026-10-22"
        status = "FULFILLED"
        customer = @{ id = $custMap["1199780054321098"].id }
        pharmacy = @{ id = $pharmMap["Kigali Pharma"].id }
    },
    # Customer 2, Pharmacy 3, 2026-10-25, FULFILLED (second FULFILLED request)
    @{
        medicineName = "Azithromycin 250mg"
        requestDate = "2026-10-25"
        status = "FULFILLED"
        customer = @{ id = $custMap["1199580012345678"].id }
        pharmacy = @{ id = $pharmMap["LifeCare Pharmacy"].id }
    },
    # Customer 2, Pharmacy 1, 2026-10-28, MATCHED
    @{
        medicineName = "Ibuprofen 400mg"
        requestDate = "2026-10-28"
        status = "MATCHED"
        customer = @{ id = $custMap["1199580012345678"].id }
        pharmacy = @{ id = $pharmMap["Pharmacie Conseil"].id }
    },
    # Customer 3, Pharmacy 2, 2026-11-02, SUBMITTED
    @{
        medicineName = "Artemether 80mg"
        requestDate = "2026-11-02"
        status = "SUBMITTED"
        customer = @{ id = $custMap["1199980076543210"].id }
        pharmacy = @{ id = $pharmMap["Kigali Pharma"].id }
    },
    # Customer 3, Pharmacy 3, 2026-11-05, MATCHED
    @{
        medicineName = "Ciprofloxacin 500mg"
        requestDate = "2026-11-05"
        status = "MATCHED"
        customer = @{ id = $custMap["1199980076543210"].id }
        pharmacy = @{ id = $pharmMap["LifeCare Pharmacy"].id }
    },
    # Customer 4, Pharmacy 1, 2026-11-10, SUBMITTED
    @{
        medicineName = "Diclofenac 50mg"
        requestDate = "2026-11-10"
        status = "SUBMITTED"
        customer = @{ id = $custMap["1199480098765432"].id }
        pharmacy = @{ id = $pharmMap["Pharmacie Conseil"].id }
    },
    # Customer 5, Pharmacy 2, 2026-11-15, DECLINED
    @{
        medicineName = "Quinine 300mg"
        requestDate = "2026-11-15"
        status = "DECLINED"
        customer = @{ id = $custMap["1200080055443322"].id }
        pharmacy = @{ id = $pharmMap["Kigali Pharma"].id }
    },
    # Customer 5, Pharmacy 3, 2026-11-20, SUBMITTED
    @{
        medicineName = "Amoxicillin 250mg"
        requestDate = "2026-11-20"
        status = "SUBMITTED"
        customer = @{ id = $custMap["1200080055443322"].id }
        pharmacy = @{ id = $pharmMap["LifeCare Pharmacy"].id }
    }
)

foreach ($r in $requests) {
    $res = Invoke-RestMethod -Uri "$baseUrl/medicine-requests/save" -Method Post -ContentType "application/json" -Body ($r | ConvertTo-Json)
    Write-Host "Saved MedicineRequest: $($r.medicineName) ($($r.status)) on $($r.requestDate) -> $res"
}

Write-Host "`n=== Seed Data Complete! ==="
