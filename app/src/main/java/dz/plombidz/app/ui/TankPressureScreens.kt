package dz.plombidz.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dz.plombidz.app.R
import dz.plombidz.app.sizing.TankPressure
import java.util.Locale

private fun num(s: String): Double = s.replace(',', '.').toDoubleOrNull() ?: 0.0
private fun fmt(v: Double): String = String.format(Locale.FRANCE, "%.2f", v)

@Composable
private fun NumRow(label: String, value: String, onChange: (String) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        TextField(
            value = value,
            onValueChange = { onChange(it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }) },
            modifier = Modifier.width(110.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )
    }
}

@Composable
fun TankScreen() {
    var persons by remember { mutableStateOf("5") }
    var days by remember { mutableStateOf("2") }
    var litres by remember { mutableStateOf("150") }
    var height by remember { mutableStateOf("6") }

    val vol = TankPressure.tankVolumeLitres(
        num(persons).toInt(), num(days).toInt(), num(litres).toInt()
    )
    val p = TankPressure.staticPressureBar(num(height))

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Text(stringResource(R.string.tank_title), style = MaterialTheme.typography.headlineSmall)
        NumRow(stringResource(R.string.tank_persons), persons) { persons = it }
        NumRow(stringResource(R.string.tank_days), days) { days = it }
        NumRow(stringResource(R.string.tank_use_l), litres) { litres = it }
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth()) {
            Text(
                "${stringResource(R.string.tank_result)} : $vol L",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(Modifier.height(8.dp))
        NumRow(stringResource(R.string.tank_height), height) { height = it }
        Card(Modifier.fillMaxWidth()) {
            Text(
                "${stringResource(R.string.tank_pressure_result)} ${fmt(p)} bar",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.tank_note), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun PressureScreen() {
    var height by remember { mutableStateOf("10") }
    val p = TankPressure.staticPressureBar(num(height))
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
    ) {
        Text(stringResource(R.string.press_title), style = MaterialTheme.typography.headlineSmall)
        NumRow(stringResource(R.string.press_height), height) { height = it }
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth()) {
            Text(
                "${stringResource(R.string.press_result_static)} ${fmt(p)} bar",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.press_result_min), style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(4.dp))
        Text(stringResource(R.string.press_note), style = MaterialTheme.typography.bodySmall)
    }
}
