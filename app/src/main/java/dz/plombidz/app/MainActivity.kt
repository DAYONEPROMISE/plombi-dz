package dz.plombidz.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dz.plombidz.app.data.PlombiViewModel
import dz.plombidz.app.ui.ClientsScreen
import dz.plombidz.app.ui.JobsScreen
import dz.plombidz.app.ui.PlombiHeader
import dz.plombidz.app.ui.PlombiTheme
import dz.plombidz.app.ui.PressureScreen
import dz.plombidz.app.ui.SectionLabel
import dz.plombidz.app.ui.SizingScreen
import dz.plombidz.app.ui.TankScreen
import dz.plombidz.app.util.LocaleHelper

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.apply(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlombiTheme {
                PlombiNav(onSwitchLang = { lang ->
                    LocaleHelper.set(this, lang)
                    recreate()
                })
            }
        }
    }
}

@Composable
fun PlombiNav(onSwitchLang: (String) -> Unit) {
    val nav = rememberNavController()
    val vm: PlombiViewModel = viewModel()
    val backStack = nav.currentBackStackEntryAsState().value
    val route = backStack?.destination?.route ?: "home"
    val title = when (route) {
        "sizing" -> stringResource(R.string.sizing_title)
        "tank" -> stringResource(R.string.tank_title)
        "pressure" -> stringResource(R.string.press_title)
        "clients" -> stringResource(R.string.clients_title)
        "jobs" -> stringResource(R.string.jobs_title)
        else -> stringResource(R.string.home_title)
    }
    Scaffold(
        topBar = { PlombiHeader(title = title) },
        bottomBar = { BottomTabs(nav, route) }
    ) { inner ->
        NavHost(
            nav,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .consumeWindowInsets(inner),
        ) {
            composable("home") { HomeScreen(nav, onSwitchLang) }
            composable("sizing") { SizingScreen() }
            composable("tank") { TankScreen() }
            composable("pressure") { PressureScreen() }
            composable("clients") { ClientsScreen(vm) }
            composable("jobs") { JobsScreen(vm) }
        }
    }
}

@Composable
fun BottomTabs(nav: NavHostController, route: String) {
    val tabs = listOf(
        "home" to R.string.nav_home,
        "sizing" to R.string.nav_sizing,
        "clients" to R.string.nav_clients,
        "jobs" to R.string.nav_jobs,
    )
    NavigationBar {
        tabs.forEach { (r, label) ->
                    NavigationBarItem(
                        selected = route == r,
                        onClick = { if (route != r) nav.navigate(r) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        icon = {
                            Icon(
                                when (r) {
                                    "home" -> Icons.Default.Home
                                    "sizing" -> Icons.Default.Build
                                    "clients" -> Icons.Default.Person
                                    else -> Icons.Default.Assignment
                                },
                                contentDescription = stringResource(label),
                            )
                        },
                        label = { Text(stringResource(label)) },
                    )
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController, onSwitchLang: (String) -> Unit) {
    data class ToolCard(val route: String, val icon: ImageVector, val title: Int, val desc: Int)
    val cards = listOf(
        ToolCard("sizing", Icons.Default.Build, R.string.home_go_sizing, R.string.home_go_sizing_d),
        ToolCard("tank", Icons.Default.WaterDrop, R.string.home_go_tank, R.string.home_go_tank_d),
        ToolCard("pressure", Icons.Default.Speed, R.string.home_go_pressure, R.string.home_go_pressure_d),
        ToolCard("clients", Icons.Default.Person, R.string.home_go_clients, R.string.home_go_clients_d),
        ToolCard("jobs", Icons.Default.Assignment, R.string.home_go_jobs, R.string.home_go_jobs_d),
    )
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.home_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.home_sub), style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(12.dp))
        SectionLabel(stringResource(R.string.tools_section))
        Spacer(Modifier.height(4.dp))
        cards.forEach { card ->
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { nav.navigate(card.route) }
            ) {
                Row(
                    Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = if (card.route == "tank" || card.route == "jobs")
                            MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ) {
                        Icon(
                            card.icon,
                            contentDescription = null,
                            tint = if (card.route == "tank" || card.route == "jobs")
                                MaterialTheme.colorScheme.onTertiaryContainer
                            else MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(10.dp).size(24.dp)
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(card.title), style = MaterialTheme.typography.titleSmall)
                        Text(
                            stringResource(card.desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.lang_title), style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSwitchLang("fr") }) {
                Text(stringResource(R.string.lang_fr))
            }
            Button(onClick = { onSwitchLang("ar") }) {
                Text(stringResource(R.string.lang_ar))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.disclaimer), style = MaterialTheme.typography.bodySmall)
    }
}
