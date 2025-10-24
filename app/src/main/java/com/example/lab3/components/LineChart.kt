package com.example.lab3.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun LineChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    labelX: String = "X",
    labelY: String = "Y"
) {
    if (data.isEmpty()) return

    val lineColor = MaterialTheme.colorScheme.primary
    val axisColor = Color.Gray
    val gridColor = Color.DarkGray
    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 24f
    }

    Canvas(modifier = modifier.fillMaxWidth().height(240.dp)) {
        val padding = 80f
        val w = size.width - padding * 1.5f
        val h = size.height - padding * 1.5f

        var maxV = data.maxOrNull() ?: 1.0
        var minV = data.minOrNull() ?: 0.0
        var range = maxV - minV

        // якщо значення майже однакові – збільшити діапазон
        if (range < 1e-3) {
            val avg = (maxV + minV) / 2
            maxV = avg * 1.1
            minV = avg * 0.9
            range = maxV - minV
        }

        // Малюємо координатну сітку
        for (i in 0..4) {
            val y = padding + i * (h / 4)
            drawLine(gridColor, Offset(padding, y), Offset(padding + w, y), 1f)
        }

        // Осі
        drawLine(axisColor, Offset(padding, padding), Offset(padding, padding + h), 2f)
        drawLine(axisColor, Offset(padding, padding + h), Offset(padding + w, padding + h), 2f)

        // Підпис шкали Y
        val stepValue = range / 4
        for (i in 0..4) {
            val value = maxV - i * stepValue
            drawContext.canvas.nativeCanvas.drawText(
                "${value.roundToInt()}",
                20f,
                padding + i * (h / 4),
                textPaint
            )
        }

        val points = data.mapIndexed { i, v ->
            val x = padding + (i.toFloat() / (data.size - 1)) * w
            val y = padding + (1f - ((v - minV) / range).toFloat()) * h
            Offset(x, y)
        }

        // Лінія
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (p in points.drop(1)) lineTo(p.x, p.y)
        }
        drawPath(path, lineColor, style = Stroke(width = 3f))

        // Точки + значення
        points.forEachIndexed { i, p ->
            drawCircle(color = lineColor, radius = 5f, center = p)
            drawContext.canvas.nativeCanvas.drawText(
                data[i].roundToInt().toString(),
                p.x,
                p.y - 12f,
                textPaint
            )
        }

        // Підписи осей
        drawContext.canvas.nativeCanvas.drawText(labelX, padding + w / 2, size.height - 10f, textPaint)
        drawContext.canvas.nativeCanvas.drawText(labelY, 10f, padding + h / 2, textPaint)
    }
}
