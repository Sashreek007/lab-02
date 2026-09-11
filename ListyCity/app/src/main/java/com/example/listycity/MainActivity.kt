package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()

        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(paddingValues = innerPadding)
                    )
                }
            }
        }

    }
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    // asked Claude Chat on how I can store the selected city to delete it
    var selectedCity by remember { mutableStateOf<String?>(null) }
    Column(modifier = modifier.fillMaxSize()) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))



        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    isSelected = city == selectedCity,
                    onClick = { selectedCity = if (selectedCity == city) null else city }
                )
            }
        }

        Button(onClick = {
            if (newCityName.isNotBlank()) {
                onAddCity(newCityName)
                newCityName = ""
            }
        },modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)) {
            Text("Add City")
        }
        Button(
            onClick = {
                selectedCity?.let { onDeleteCity(it) }
                selectedCity = null
            },
            enabled = selectedCity != null,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Delete City")
        }
    }
}

    @Composable
    fun CityRow(city: String, isSelected: Boolean, onClick: () -> Unit) {
        Text(
            text = city,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else Color.Transparent
                )
                .padding(horizontal = 18.dp, vertical = 14.dp)
        )
    }


    class CityRepository {

        private val _cities = mutableStateListOf(
            "Edmonton",
            "Vancouver",
            "Moscow",
            "Sydney",
            "Berlin",
            "Vienna",
            "Tokyo",
            "Beijing",
            "Osaka",
            "New Delhi"
        )

        val cities: List<String>
            get() = _cities

        fun addCity(city: String) {
            _cities.add(city)
        }

        fun deleteCity(city: String) {
            _cities.remove(city)
        }
    }
