package com.euriconeto.markupcalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.euriconeto.markupcalculator.R
import com.euriconeto.markupcalculator.ui.MarkupRoute
import com.euriconeto.markupcalculator.ui.viewModel.MarkupViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MarkupScreen(
    viewModel: MarkupViewModel = koinViewModel(),
    markup: MarkupRoute
) {
    viewModel.initialize(markup.markupId)
    val validation by viewModel.validation.collectAsState()
    val loading by viewModel.loading.collectAsState()
    if (loading.value && validation.value) {
        val strokeWidth = 5.dp
        val color = MaterialTheme.colorScheme.primary

        CircularProgressIndicator(
            modifier = Modifier.drawBehind {
                drawCircle(
                    color = color,
                    radius = size.width / 2 - strokeWidth.toPx() / 2,
                    style = Stroke(strokeWidth.toPx())
                )
            },
            strokeWidth = strokeWidth
        )
    }

    if (!loading.value && validation.value) {
        val context = LocalContext.current
        val mkd by viewModel.mkd.collectAsState()
        val mkm by viewModel.mkm.collectAsState()
        val sellingPrice by viewModel.sellingPrice.collectAsState()
        val productCost by viewModel.productCost.collectAsState().value
        val profitMargin by viewModel.profitMargin.collectAsState().value
        val expenses by viewModel.expenses.collectAsState()
        var openSaveDialog = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(state = ScrollableState { it * 2 }, orientation = Orientation.Vertical),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.calculate_markup),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineLarge
                )

                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(5f),
                        value = productCost,
                        label = { Text(text = stringResource(id = R.string.product_cost)) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(),
                        onValueChange = {
                            if (it.isEmpty() || it.isBlank()) {
                                viewModel.setProductCost("")
                                return@OutlinedTextField
                            }

                            val value = it.replace(",", ".").toFloatOrNull()
                            if (value != null) {
                                viewModel.setProductCost(value.toString())
                            }
                        }
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    OutlinedTextField(
                        modifier = Modifier.weight(5f),
                        value = profitMargin,
                        label = { Text(text = stringResource(id = R.string.profit_margin)) },
                        textStyle = MaterialTheme.typography.bodyLarge,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions(),
                        onValueChange = {
                            if (it.isEmpty() || it.isBlank()) {
                                viewModel.setProfitMargin("")
                                return@OutlinedTextField
                            }

                            val value = it.replace(",", ".").toFloatOrNull()
                            if (value != null) {
                                viewModel.setProfitMargin(value.toString())
                            }
                        }
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                    MaterialTheme.colorScheme.onTertiaryContainer
                                ),
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
                    items(4) { index ->
                        var text by remember { mutableStateOf("") }

                        if (expenses.value.isNotEmpty() && expenses.value.keys.contains(index)) {
                            text = expenses.value[index].toString()
                        }

                        BasicTextField(
                            value = text,
                            onValueChange = {
                                if (it.isEmpty()) {
                                    text = it
                                    viewModel.updateMarkup(index, 0.0f)
                                    return@BasicTextField
                                }

                                val value = it.replace(",", ".")
                                if (value.toFloatOrNull() != null) {
                                    text = value
                                    viewModel.updateMarkup(index, text.toFloat())
                                    return@BasicTextField
                                }
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                    if (text.isEmpty()) {
                                        Text(
                                            text = stringResource(id = R.string.insert_value),
                                            color = MaterialTheme.colorScheme.secondary,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        viewModel.updateMarkup(index, 0.0f)
                                    }
                                    innerTextField.invoke()
                                }
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1.5f)
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.product_value)
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                text = sellingPrice.value,
                                style = TextStyle.Default.copy(fontSize = 64.sp)
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.markup_multiplier)
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                text = mkm.value,
                                style = TextStyle(fontSize = 24.sp)
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.markup_divider)
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                text = mkd.value,
                                style = TextStyle(fontSize = 24.sp)
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            val productCostValue = productCost.replace(",", ".").toFloatOrNull()
                            val profitMarginValue = profitMargin.replace(",", ".").toFloatOrNull()

                            if (productCostValue != null && profitMarginValue != null) {
                                viewModel.calculateMarkup(
                                    context,
                                    productCostValue,
                                    profitMarginValue
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = stringResource(id = R.string.calculate)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val productCostValue = productCost.replace(",", ".").toFloatOrNull()
                                val profitMarginValue =
                                    profitMargin.replace(",", ".").toFloatOrNull()

                                if (profitMarginValue != null && productCostValue != null) {
                                    viewModel.calculateMarkup(
                                        context,
                                        productCostValue,
                                        profitMarginValue
                                    )
                                    openSaveDialog.value = true
                                } else {
                                    openSaveDialog.value = false
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = stringResource(id = R.string.save_markup)
                            )
                        }
                        Button(
                            onClick = {
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = stringResource(id = R.string.clear_screen)
                            )
                        }
                    }
                }
                when {
                    openSaveDialog.value -> SaveDialog(
                        onConfirmation = { markupName ->
                            val productCostValue = productCost.replace(",", ".").toFloatOrNull()
                            val profitMarginValue = profitMargin.replace(",", ".").toFloatOrNull()
                            if (productCostValue != null && profitMarginValue != null) {
                                viewModel.saveMarkup(
                                    markupName,
                                    profitMarginValue,
                                    productCostValue
                                )
                                openSaveDialog.value = false
                            }
                        },
                        onDismissRequest = { openSaveDialog.value = false },
                    )
                }
            }
        }
    }
}

@Composable
fun SaveDialog(
    onConfirmation: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var nameMarkup by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(stringResource(R.string.save_question_text))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    value = nameMarkup,
                    onValueChange = { nameMarkup = it }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            if (nameMarkup.isNotEmpty() && nameMarkup.isNotBlank()) {
                                onConfirmation(nameMarkup)
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
