package com.example.lab3.logic

object SolarFormulas {

    val defaultMonthlyFactors = listOf(
        0.45, // Січень
        0.55, // Лютий
        0.80, // Березень
        1.00, // Квітень
        1.20, // Травень
        1.35, // Червень
        1.40, // Липень
        1.25, // Серпень
        1.00, // Вересень
        0.75, // Жовтень
        0.55, // Листопад
        0.40  // Грудень
    )

    val daysInMonth = listOf(
        31, 28, 31, 30, 31, 30,
        31, 31, 30, 31, 30, 31
    )
    fun dailyEnergyKWh(
        panelCount: Double,
        powerPerPanel: Double,
        sunHours: Double,
        efficiency: Double,
        scale: Double = 1.0
    ): Double {
        return panelCount * powerPerPanel * sunHours * efficiency * scale / 1000.0
    }

    fun monthlyEnergyKWh(dailyKWh: Double, daysInMonth: Int = 30): Double {
        return dailyKWh * daysInMonth
    }

    fun yearlyEnergyKWh(dailyKWh: Double): Double {
        var totalEnergy = 0.0

        for (i in 0 until 12) {
            val monthlyEnergy = dailyKWh * defaultMonthlyFactors[i] * daysInMonth[i]
            totalEnergy += monthlyEnergy
        }

        return totalEnergy
    }


    fun revenueUAH(yearlyKWh: Double, tariff: Double): Double {
        return yearlyKWh * tariff
    }

    fun expensesUAH(revenue: Double, pct: Double): Double {
        return revenue * pct / 100.0
    }

    fun profitUAH(revenue: Double, expenses: Double): Double {
        return revenue - expenses
    }

    fun paybackYears(investment: Double, yearlyProfit: Double): Double {
        return if (yearlyProfit == 0.0) Double.POSITIVE_INFINITY else investment / yearlyProfit
    }

    fun projectedAnnualProfit(yearlyProfit: Double, years: Int, degradationPct: Double): List<Double> {
        return (0 until years).map { y ->
            yearlyProfit * Math.pow(1 - degradationPct / 100.0, y.toDouble())
        }
    }
}
