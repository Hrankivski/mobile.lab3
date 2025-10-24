package com.example.lab3.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab3.components.NumberField
import com.example.lab3.components.LineChart
import com.example.lab3.components.BarChart
import com.example.lab3.logic.SolarFormulas

@Composable
fun SolarScreen() {
    var Npanels by remember { mutableStateOf("20") }
    var Ppanel by remember { mutableStateOf("330") } // W
    var Hsun by remember { mutableStateOf("4.2") } // h/day
    var eta by remember { mutableStateOf("0.18") } // 18%
    var tariff by remember { mutableStateOf("1.5") } // UAH / kWh
    var expensesPct by remember { mutableStateOf("10") } // %
    var investment by remember { mutableStateOf("100000") } // UAH
    var years by remember { mutableStateOf("10") }
    var degradation by remember { mutableStateOf("0.5") } // % per year

    var monthlyFactorsStr by remember { mutableStateOf("") } // optional custom factors csv
    var resultText by remember { mutableStateOf("") }

    val scroll = rememberScrollState()

    var monthlyGeneration by remember { mutableStateOf(listOf<Double>()) }
    var annualProfits by remember { mutableStateOf(listOf<Double>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Параметри системи", style = MaterialTheme.typography.titleMedium)

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            NumberField("Кількість панелей (шт)", Npanels, { Npanels = it }, modifier = Modifier.weight(1f))
            NumberField("Потужність панелі (Вт)", Ppanel, { Ppanel = it }, modifier = Modifier.weight(1f))
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            NumberField("Еквівалент сонця (год/доб)", Hsun, { Hsun = it }, modifier = Modifier.weight(1f))
            NumberField("Ефективність η (доля)", eta, { eta = it }, modifier = Modifier.weight(1f))
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            NumberField("Тариф (грн/кВт·год)", tariff, { tariff = it }, modifier = Modifier.weight(1f))
            NumberField("Операційні витрати (%)", expensesPct, { expensesPct = it }, modifier = Modifier.weight(1f))
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            NumberField("Інвестиції (грн)", investment, { investment = it }, modifier = Modifier.weight(1f))
            NumberField("Років проєкт (роки)", years, { years = it }, modifier = Modifier.weight(1f))
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            NumberField("Деградація (%/рік)", degradation, { degradation = it }, modifier = Modifier.weight(1f))
            NumberField("Місячні коефіцієнти (csv)", monthlyFactorsStr, { monthlyFactorsStr = it }, modifier = Modifier.weight(1f))
        }

        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                Npanels = "100"
                Ppanel = "330"
                Hsun = "4.5"
                eta = "0.18"
                tariff = "2.5"
                expensesPct = "12"
                investment = "450000"
                years = "12"
                degradation = "0.6"
                monthlyFactorsStr = ""
            }, modifier = Modifier.weight(1f)) {
                Text("Приклад")
            }

            Button(onClick = {
                val n = Npanels.toDoubleOrNull() ?: 0.0
                val p = Ppanel.toDoubleOrNull() ?: 0.0
                val h = Hsun.toDoubleOrNull() ?: 0.0
                val e = eta.toDoubleOrNull() ?: 0.0
                val t = tariff.toDoubleOrNull() ?: 0.0
                val expPct = expensesPct.toDoubleOrNull() ?: 0.0
                val inv = investment.toDoubleOrNull() ?: 0.0
                val yrs = years.toIntOrNull() ?: 1
                val degr = degradation.toDoubleOrNull() ?: 0.0

                val mFactors = parseMonthlyFactors(monthlyFactorsStr).takeIf { it.size == 12 }
                    ?: SolarFormulas.defaultMonthlyFactors

                val dailyBase = SolarFormulas.dailyEnergyKWh(n, p, h, e, 1.0)
                val monthlyList = mFactors.map { factor ->
                    SolarFormulas.monthlyEnergyKWh(dailyBase * factor, 30)
                }

                val yearly = SolarFormulas.yearlyEnergyKWh(dailyBase)
                val revenueYear = SolarFormulas.revenueUAH(yearly, t)
                val expensesYear = SolarFormulas.expensesUAH(revenueYear, expPct)
                val profitYear = SolarFormulas.profitUAH(revenueYear, expensesYear)
                val payback = SolarFormulas.paybackYears(inv, profitYear)

                val yearlyProfitsProjection = SolarFormulas.projectedAnnualProfit(profitYear, yrs, degr)

                monthlyGeneration = monthlyList
                annualProfits = yearlyProfitsProjection

                resultText = buildString {
                    appendLine("Результати:")
                    appendLine("Щоденна генерація ≈ ${"%.2f".format(dailyBase)} кВт·год")
                    appendLine("Річна генерація ≈ ${"%.2f".format(yearly)} кВт·год")
                    appendLine("Річний дохід ≈ ${"%.2f".format(revenueYear)} грн")
                    appendLine("Витрати ≈ ${"%.2f".format(expensesYear)} грн")
                    appendLine("Прибуток ≈ ${"%.2f".format(profitYear)} грн")
                    appendLine("Окупність ≈ ${if (payback.isInfinite()) "∞" else "%.2f".format(payback)} років")
                }
            }, modifier = Modifier.weight(1f)) {
                Text("Розрахувати")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    if (resultText.isEmpty())
                        "Натисніть 'Розрахувати' для перегляду результатів"
                    else resultText
                )
            }
        }

        if (monthlyGeneration.isNotEmpty()) {
            Text("Місячна генерація енергії", style = MaterialTheme.typography.titleMedium)
            LineChart(data = monthlyGeneration, labelX = "Рік", labelY = "кВт·год")
        }

        if (annualProfits.isNotEmpty()) {
            Text("Прогноз прибутку по роках", style = MaterialTheme.typography.titleMedium)
            BarChart(data = annualProfits, labelX = "Роки", labelY = "Прибуток (грн)")
        }
    }
}

private fun parseMonthlyFactors(input: String): List<Double> {
    if (input.isBlank()) return emptyList()
    val tokens = input.split(',', ';', ' ', '\n').mapNotNull { it.trim().takeIf { s -> s.isNotEmpty() } }
    val nums = tokens.mapNotNull { it.toDoubleOrNull() }
    return if (nums.size >= 12) nums.take(12) else nums
}
