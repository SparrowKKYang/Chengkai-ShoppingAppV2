package com.example.shoppingappv2

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppingappv2.ui.theme.ShoppingAppV2Theme

data class Product(val name: String, val price: String, val description: String)

val products = listOf(
    Product("Product A", "$100", "This is a great product A."),
    Product("Product B", "$150", "This is product B with more features."),
    Product("Product C", "$200", "Premium product C.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingAppV2Theme {
                val selectedProduct = remember { mutableStateOf<Product?>(null) }
                ShoppingApp(products = products, selectedProduct = selectedProduct)
            }
        }
    }
}

@Composable
fun ProductList(products: List<Product>, onProductClick: (Product) -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().weight(9f), // Makes LazyColumn take available space
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product) },
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = product.name, style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isLandscape) {
                            Text(text = product.price, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isLandscape) {
                Text(
                    text = "Select a product to view details.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ProductDetails(selectedProduct: Product?, onBackToListClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (selectedProduct != null) {
            Text(text = selectedProduct.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = selectedProduct.price, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = selectedProduct.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Back to product list button
            Button(onClick = onBackToListClick) {
                Text(text = "Back to Product List")
            }
        }
    }
}

@Composable
fun ShoppingApp(products: List<Product>, selectedProduct: MutableState<Product?>) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Landscape mode
        Row(modifier = Modifier.fillMaxSize()) {
            ProductList(products = products) { product ->
                selectedProduct.value = product
            }
            ProductDetails(
                selectedProduct = selectedProduct.value,
                onBackToListClick = { selectedProduct.value = null }
            )
        }
    } else {
        // Portrait Mode
        if (selectedProduct.value == null) {
            ProductList(products = products) { product ->
                selectedProduct.value = product
            }
        } else {
            ProductDetails(
                selectedProduct = selectedProduct.value,
                onBackToListClick = { selectedProduct.value = null }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShoppingAppV2Theme {
        val selectedProduct = remember { mutableStateOf<Product?>(null) }
        ShoppingApp(products = products, selectedProduct = selectedProduct)
    }
}
