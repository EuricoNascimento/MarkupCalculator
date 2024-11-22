package com.euriconeto.markupcalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.euriconeto.markupcalculator.R
import com.euriconeto.markupcalculator.service.model.Markup
import com.euriconeto.markupcalculator.ui.MarkupRoute
import com.euriconeto.markupcalculator.ui.RouterSet
import com.euriconeto.markupcalculator.ui.theme.MarkupCalculatorTheme
import com.euriconeto.markupcalculator.ui.viewModel.MarkupListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MarkupListScreen(
    viewModel: MarkupListViewModel = koinViewModel(),
    navController: NavHostController
) {
    viewModel.initialize()

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
        val markupList by viewModel.markupList.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Markups Salvos",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.padding(16.dp))
            LazyColumn(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(markupList.value) { markup ->
                    var isExpanded by remember {
                        mutableStateOf(false)
                    }

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = MaterialTheme.shapes.large
                            )
                            .clickable {
                                isExpanded = !isExpanded
                            }
                    ) {
                        Text(
                            text = markup.name,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.divider),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(
                                    text = markup.markupDivider.toString(),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            Column(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.multiplier),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(
                                    text = markup.markupMultiplier.toString(),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        if (isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                isExpanded = !isExpanded
                                                navController.navigate(MarkupRoute(markup.id))
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = stringResource(id = R.string.edit_markup))
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = stringResource(id = R.string.edit_markup)
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                isExpanded = !isExpanded
                                                viewModel.deleteMarkup(markup.id)
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = stringResource(id = R.string.remove_markup))
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = stringResource(id = R.string.remove_markup)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}