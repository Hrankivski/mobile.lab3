package com.example.lab3.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun NumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    error: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { raw ->
                val cleaned = raw.replace(',', '.')
                    .filterIndexed { index, ch ->
                        ch.isDigit() || (ch == '.' && cleanedWouldAllowDot(raw, index))
                    }
                val normalized = normalizeNumberString(cleaned)
                onValueChange(normalized)
            },
            label = { Text(label) },
            singleLine = true,
            trailingIcon = if (suffix != null) { { Text(suffix) } } else null,
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        if (!error.isNullOrEmpty()) {
            Text(text = error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }
    }
}

private fun cleanedWouldAllowDot(raw: String, index: Int): Boolean {
    return true
}

private fun normalizeNumberString(s: String): String {
    if (s.isEmpty()) return s
    val parts = s.split('.')
    return if (parts.size <= 2) s else {
        val first = parts.first()
        val decimals = parts.drop(1).joinToString("").take(8) // limit decimals length
        if (decimals.isEmpty()) first else "$first.$decimals"
    }
}
