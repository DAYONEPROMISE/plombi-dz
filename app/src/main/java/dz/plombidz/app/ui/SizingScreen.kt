package dz.plombidz.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dz.plombidz.app.R
import dz.plombidz.app.sizing.Fixture
import dz.plombidz.app.sizing.Material
import dz.plombidz.app.sizing.SizingEngine
import java.util.Locale

private fun fixtureLabel(f: Fixture): Int = when (f) {
    Fixture.SINK -> R.string.fixture_sink
    Fixture.BASIN -> R.string.fixture_basin
    Fixture.SHOWER -> R.string.fixture_shower
    Fixture.BATH -> R.string.fixture_bath
    Fixture.WC -> R.string.fixture_wc
    Fixture.WASHER -> R.string.fixture_washer
    Fixture.TAP12 -> R.string.fixture_tap12
    Fixture.TAP34 -> R.string.fixture_tap34
}

private fun materialLabel(m: Material): Int = when (m) {
    Material.PER -> R.string.mat_per
    Material.MULTILAYER -> R.string.mat_multi
    Material.COPPER -> R.string.mat_copper
}

private fun fmt(v: Double, digits: Int = 2): String =
    String.format(Locale.FRANCE, "%.${digits}f", v)

@Composable
fun SizingScreen() {
    val counts = remember { mutableStateMapOf<Fixture, Int>() }
    var material by remember { mutableStateOf(Material.MULTILAYER) }
    var lengthText by remember { mutableStateOf("8") }

    val length = lengthText.replace(',', '.').toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val result = SizingEngine.compute(counts.toMap(), material, length)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Fixture.entries.forEach { f ->
            val c = counts[f] ?: 0
            Row(
                Modifier.fillMaxWidth().padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "${stringResource(fixtureLabel(f))} (${fmt(f.baseFlowLs)} ${stringResource(R.string.flow_unit)})",
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(onClick = { counts[f] = (c - 1).coerceAtLeast(0) }) { Text("−") }
                Text("$c", modifier = Modifier.padding(horizontal = 8.dp))
                OutlinedButton(onClick = { counts[f] = c + 1 }) { Text("+") }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.sizing_material), style = MaterialTheme.typography.titleSmall)
        Material.entries.forEach { m ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = material == m, onClick = { material = m })
                Text(stringResource(materialLabel(m)))
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.sizing_length), modifier = Modifier.weight(1f))
            TextField(
                value = lengthText,
                onValueChange = { lengthText = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                modifier = Modifier.width(96.dp),
                singleLine = true,
            )
        }
        Spacer(Modifier.height(4.dp))
        Button(onClick = { counts.clear() }) { Text(stringResource(R.string.sizing_clear)) }
        Spacer(Modifier.height(8.dp))
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Column(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    stringResource(R.string.sizing_reco).uppercase(),
                    style = MaterialTheme.typography.labelMedium
                )
                Text("Ø${result.outerMm} mm", style = MaterialTheme.typography.displaySmall)
                Text(
                    "${stringResource(R.string.sizing_velocity)} : ${fmt(result.velocityMs)} m/s",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "${stringResource(R.string.sizing_total_flow)} : ${fmt(result.rawFlowLs)} ${stringResource(R.string.flow_unit)}"
                )
                Text("${stringResource(R.string.sizing_simult)} : ${fmt(result.simultaneity)}")
                Text(
                    "${stringResource(R.string.sizing_prob_flow)} : ${fmt(result.probableFlowLs)} ${stringResource(R.string.flow_unit)}"
                )
                if (result.upsizedForLength) {
                    Text(stringResource(R.string.sizing_warn_length), color = MaterialTheme.colorScheme.error)
                }
                if (result.overVelocity) {
                    Text(stringResource(R.string.sizing_over_velocity), color = MaterialTheme.colorScheme.error)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.sizing_note), style = MaterialTheme.typography.bodySmall)
    }
}
