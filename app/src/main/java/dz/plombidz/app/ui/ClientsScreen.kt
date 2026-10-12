package dz.plombidz.app.ui

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dz.plombidz.app.R
import dz.plombidz.app.data.Client
import dz.plombidz.app.data.PlombiViewModel

@Composable
fun ClientsScreen(vm: PlombiViewModel) {
    val clients by vm.clients.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.clients_add))
            }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
            Spacer(Modifier.height(8.dp))
            if (clients.isEmpty()) {
                PlombiEmptyState(
                    icon = Icons.Default.Person,
                    title = stringResource(R.string.empty_clients_title),
                    hint = stringResource(R.string.empty_clients_hint)
                )
            } else {
                LazyColumn {
                    items(clients, key = { it.id }) { c ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    Text(c.name, style = MaterialTheme.typography.titleSmall)
                                    if (c.phone.isNotBlank()) Text(c.phone)
                                    if (c.address.isNotBlank()) Text(c.address, style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { vm.removeClient(c) }) {
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
        ClientDialog(onDismiss = { showDialog = false }, onSave = {
            vm.saveClient(it)
            showDialog = false
        })
    }
}

@Composable
fun ClientDialog(onDismiss: () -> Unit, onSave: (Client) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.clients_add)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                TextField(name, { name = it }, label = { Text(stringResource(R.string.client_name)) }, singleLine = true)
                TextField(phone, { phone = it }, label = { Text(stringResource(R.string.client_phone)) }, singleLine = true)
                TextField(address, { address = it }, label = { Text(stringResource(R.string.client_address)) }, singleLine = true)
                TextField(notes, { notes = it }, label = { Text(stringResource(R.string.client_notes)) })
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onSave(Client(name = name.trim(), phone = phone.trim(), address = address.trim(), notes = notes.trim())) }
            ) { Text(stringResource(R.string.save)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) } },
    )
}
