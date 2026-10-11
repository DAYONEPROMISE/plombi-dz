package dz.plombidz.app.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import dz.plombidz.app.R
import dz.plombidz.app.data.Intervention
import dz.plombidz.app.data.PlombiViewModel
import dz.plombidz.app.pdf.FichePdf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JobsScreen(vm: PlombiViewModel) {
    val jobs by vm.jobs.collectAsState()
    val clients by vm.clients.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val dateFmt = remember { SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.jobs_add))
            }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            Text(stringResource(R.string.jobs_title), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            if (jobs.isEmpty()) {
                Text(stringResource(R.string.empty_jobs))
            } else {
                LazyColumn {
                    items(jobs, key = { it.id }) { j ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        j.clientName.ifBlank { stringResource(R.string.no_client) },
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Text(
                                        "${dateFmt.format(Date(j.dateEpoch))} \u2014 ${j.priceDa} DA",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                    if (j.description.isNotBlank()) {
                                        Text(j.description.take(80), style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text(
                                        stringResource(if (j.done) R.string.status_done else R.string.status_open),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }
                                IconButton(onClick = {
                                    val file = FichePdf.build(context, j)
                                    val uri = FileProvider.getUriForFile(
                                        context, "${context.packageName}.fileprovider", file
                                    )
                                    context.startActivity(
                                        Intent.createChooser(
                                            Intent(Intent.ACTION_SEND).apply {
                                                type = "application/pdf"
                                                putExtra(Intent.EXTRA_STREAM, uri)
                                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            },
                                            context.getString(R.string.job_share_pdf),
                                        )
                                    )
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = stringResource(R.string.job_share_pdf))
                                }
                                IconButton(onClick = { vm.removeJob(j) }) {
                                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showDialog) {
        JobDialog(
            clientNames = clients.map { it.name },
            onDismiss = { showDialog = false },
            onSave = {
                vm.saveJob(it)
                showDialog = false
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDialog(
    clientNames: List<String>,
    onDismiss: () -> Unit,
    onSave: (Intervention) -> Unit,
) {
    var client by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var materials by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var done by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.jobs_add)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    TextField(
                        value = client,
                        onValueChange = { client = it },
                        label = { Text(stringResource(R.string.job_client)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        singleLine = true,
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        clientNames.forEach { name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = { client = name; expanded = false },
                            )
                        }
                    }
                }
                TextField(desc, { desc = it }, label = { Text(stringResource(R.string.job_desc)) })
                TextField(materials, { materials = it }, label = { Text(stringResource(R.string.job_materials)) })
                TextField(
                    price, { price = it.filter(Char::isDigit) },
                    label = { Text(stringResource(R.string.job_price)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(done, { done = it })
                    Text(stringResource(R.string.status_done))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    Intervention(
                        clientName = client.trim(),
                        description = desc.trim(),
                        materials = materials.trim(),
                        priceDa = price.toLongOrNull() ?: 0L,
                        done = done,
                    )
                )
            }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
    )
}
