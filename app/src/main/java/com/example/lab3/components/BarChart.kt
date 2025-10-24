package com.example.lab3.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun BarChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    labelX: String = "Рік",
    labelY: String = "Прибуток (грн)"
) {
    if (data.isEmpty()) return

    val barColor = MaterialTheme.colorScheme.primary
    val axisColor = Color.Gray
    val gridColor = Color.DarkGray

    val textPaint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        textSize = 24f
        isAntiAlias = true
        textAlign = android.graphics.Paint.Align.CENTER
    }

    Canvas(modifier = modifier.fillMaxWidth().height(240.dp)) {
        val paddingLeft = 80f
        val paddingBottom = 60f
        val paddingTop = 40f
        val w = size.width - paddingLeft * 1.2f
        val h = size.height - (paddingTop + paddingBottom)

        val maxV = data.maxOrNull() ?: 1.0
        val range = if (maxV == 0.0) 1.0 else maxV

        val gridLines = 4
        for (i in 0..gridLines) {
            val y = paddingTop + i * (h / gridLines)
            drawLine(gridColor, Offset(paddingLeft, y), Offset(paddingLeft + w, y), 1f)
        }

        drawLine(axisColor, Offset(paddingLeft, paddingTop), Offset(paddingLeft, paddingTop + h), 2f)
        drawLine(axisColor, Offset(paddingLeft, paddingTop + h), Offset(paddingLeft + w, paddingTop + h), 2f)

        drawContext.canvas.nativeCanvas.apply {
            save()
            rotate(-90f, 20f, paddingTop + h / 2)
            drawText(labelY, 20f, paddingTop + h / 2, textPaint)
            restore()
        }

        val barWidth = w / (data.size * 1.8f)
        val gap = barWidth * 0.8f

        data.forEachIndexed { i, v ->
            val barHeight = (v / range * h).toFloat()
            val left = paddingLeft + i * (barWidth + gap)
            val top = paddingTop + (h - barHeight)

            drawRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

            drawContext.canvas.nativeCanvas.apply {
                save()
                rotate(-90f, left + barWidth / 2, top + barHeight / 2)
                drawText(v.roundToInt().toString(), left + barWidth / 2, top + barHeight / 2 + textPaint.textSize / 2, textPaint)
                restore()
            }
        }

        drawContext.canvas.nativeCanvas.drawText(labelX, paddingLeft + w / 2, size.height - 10f, textPaint)
    }
}
