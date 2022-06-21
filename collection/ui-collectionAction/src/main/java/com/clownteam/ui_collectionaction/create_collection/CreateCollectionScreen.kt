package com.clownteam.ui_collectionaction.create_collection

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.clownteam.components.DefaultButton
import com.clownteam.components.header.DefaultHeader
import com.clownteam.core.domain.EventHandler
import com.clownteam.ui_collectionaction.R
import kotlinx.coroutines.flow.collect

@Composable
fun CreateCollectionScreen(
    state: CreateCollectionState,
    eventHandler: EventHandler<CreateCollectionEvent>,
    viewModel: CreateCollectionViewModel,
    onBack: () -> Unit,
    onSuccessCreate: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.createResults.collect { event ->
            when (event) {
                CreateCollectionViewModel.CreateCollectionResult.Failed -> {
                    Toast.makeText(context, "Ошибка при отправке данных", Toast.LENGTH_SHORT).show()
                }

                CreateCollectionViewModel.CreateCollectionResult.NetworkError -> {
                    Toast.makeText(context, "Ошибка сети", Toast.LENGTH_SHORT).show()
                }

                CreateCollectionViewModel.CreateCollectionResult.Success -> {
                    Toast.makeText(context, "Успешное создание", Toast.LENGTH_SHORT).show()
                    onSuccessCreate()
                }

                CreateCollectionViewModel.CreateCollectionResult.Unauthorized -> {
                    Toast.makeText(context, "Требуется авторизация", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var title by rememberSaveable { mutableStateOf("") }

    when (state) {
        CreateCollectionState.Loading -> {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        CreateCollectionState.Idle -> {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (header, label, textField, createButton) = createRefs()

                Box(modifier = Modifier.fillMaxWidth().constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    DefaultHeader(
                        stringResource(R.string.create_collection_title_text),
                        onArrowClick = { onBack() }
                    )
                }

                Text(
                    "Введите название новой подборки",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(label) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(textField.top)
                    }.padding(bottom = 75.dp)
                )

                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Название",
                                style = LocalTextStyle.current.copy(
                                    textAlign = TextAlign.Center,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                            )
                        }
                    },
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.constrainAs(textField) {
                        top.linkTo(header.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }.fillMaxWidth().padding(horizontal = 26.dp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                )

                DefaultButton(
                    text = stringResource(R.string.create),
                    onClick = {
                        eventHandler.obtainEvent(
                            CreateCollectionEvent.CreateCollection(title)
                        )
                    },
                    modifier = Modifier.constrainAs(createButton) {
                        top.linkTo(textField.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            }
        }
    }
}