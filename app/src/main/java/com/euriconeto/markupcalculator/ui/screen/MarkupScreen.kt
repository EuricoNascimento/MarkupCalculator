package com.euriconeto.markupcalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.euriconeto.markupcalculator.R
import com.euriconeto.markupcalculator.ui.theme.MarkupCalculatorTheme

@Composable
fun MarkupScreen() {
    var index by remember { mutableIntStateOf(1) }
    var columnSize by remember { mutableStateOf(80.dp) }
    var productCost by remember { mutableFloatStateOf(0.0f) }
    var profitMargin by remember { mutableFloatStateOf(0.0f) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .scrollable(state = ScrollableState { it * 2 }, orientation = Orientation.Vertical)
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(5f),
                value = productCost.toString(),
                label = { Text(text = stringResource(id = R.string.product_cost)) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(),
                onValueChange = {
                    productCost = it.toFloatOrNull() ?: 0.0f
                }
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(5f),
                value = profitMargin.toString(),
                label = { Text(text = stringResource(id = R.string.profit_margin)) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(),
                onValueChange = {
                    profitMargin = it.toFloatOrNull() ?: 0.0f
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(columnSize)
                .background(
                    Brush.radialGradient(
                        listOf(MaterialTheme.colorScheme.background, Color.Companion.DarkGray),
                        radius = 1000f
                    ),
                    RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    )
                )
        ) {
            items(index) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    var text by remember { mutableStateOf("") }

                    BasicTextField(
                        value = text,
                        onValueChange = { input -> text = input },
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                            )
                            .fillMaxWidth()
                            .height(48.dp)
                            .weight(8f),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.secondary),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier.padding(15.dp)
                            ) {
                                if (text.isEmpty()) Text(
                                    text = stringResource(id = R.string.insert_value),
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                innerTextField.invoke()
                            }
                        }
                    )
                    IconButton(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .weight(2f),
                        onClick = {
                            if (index == (it + 1) && it < 4) {
                                index++
                                columnSize += 80.dp
                            } else {
                                
                                index--
                                columnSize -= 80.dp
                            }
                        }
                    ) {
                        val icon = if (index == (it + 1) && it < 4) {
                            Icons.Default.Add
                        } else {
                            Icons.Default.Close
                        }

                        val description = if (index == (it + 1) && it < 4) {
                            stringResource(id = R.string.add_field)
                        } else {
                            stringResource(id = R.string.remove_field)
                        }

                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = icon,
                            contentDescription = description,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MarkupCalculatorTheme {
        MarkupScreen()
    }
}