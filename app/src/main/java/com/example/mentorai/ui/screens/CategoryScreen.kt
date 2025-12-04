package com.example.mentorai.ui.screens

import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import com.example.mentorai.data.model.Category
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info

val categories = listOf(
    Category("Mimarlık", Icons.Filled.Architecture),
    Category("Tadilat", Icons.Filled.Build),
    Category("Finans", Icons.Filled.AttachMoney),
    Category("Eğitim", Icons.Filled.School),
    Category("Yazılım", Icons.Filled.Computer),
    Category("Sağlıklı Yaşam", Icons.Filled.HealthAndSafety),
    Category("İş & Üretkenlik", Icons.Filled.Work),
    Category("Genel", Icons.Filled.Info)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Kategori Seçimi") })
        },
        content = { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(category = category) {
                        navController.navigate("chat/${category.name}")
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
