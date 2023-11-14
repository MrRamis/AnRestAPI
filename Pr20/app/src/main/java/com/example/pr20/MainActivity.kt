package com.example.pr20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.pr20.model.RepositoryCats
import com.example.pr20.model.ScheduleDTO
import com.example.pr20.model.Status
import com.example.pr20.ui.theme.Pr20Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val repository = RepositoryCats()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pr20Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Cats(repository,lifecycleScope)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cats(repository: RepositoryCats, lifecycleScope: LifecycleCoroutineScope) {
    Column {
        var type by rememberSaveable { mutableStateOf("") }
        var amount by rememberSaveable { mutableStateOf("") }
        var data by rememberSaveable { mutableStateOf(listOf<ScheduleDTO>()) }
        var status by rememberSaveable { mutableStateOf(Status.None) }

        TextField(type, { type = it }, label = { Text(text = "тип") })
        TextField(amount, { amount = it }, label = { Text(text = "Количество") })
        Button(onClick = {
            val amountInt = amount.toIntOrNull()?:1
            val flow = repository.getFacts(type,amountInt)
            lifecycleScope.launch{
                flow.collect{
                    status = it.first
                    val list = it.second
                    if (list != null){
                        data = list
                    }
                }
            }
        }) {
            Text(text = "Получить")
        }
        Text(
            when(status){
                Status.Error ->"Ошибка"
                Status.None ->""
                Status.Woiting ->"Ожидание"
                Status.OK ->"Загрузка завершина"
            }
        )
        LazyColumn{
            items(data){
                Row{
                    Text(it.text, modifier = Modifier.width(300.dp))
                    Text(it.type)
                }
            }
        }
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Pr20Theme {
        Greeting("Android")
    }
}