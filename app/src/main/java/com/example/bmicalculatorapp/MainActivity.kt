package com.example.bmicalculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmicalculatorapp.ui.theme.BMICalculatorAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BMICalculatorAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BMICalculator()
                }
            }
        }
    }
}

private fun calculateBMI(
    weight: Double,
    height: Double,
): String{
    val bmi = weight / (height * height)
    val roundedResult = String.format("%.2f", bmi)
    return "$roundedResult kg/m²"
}

private fun getStatus(
    bmi: Double,
): String{
    return when {
        bmi < 18.5 -> "Underweight"
        bmi in 18.5..24.9 -> "Normal Weight"
        bmi in 25.0..29.9 -> "Overweight"
        bmi in 30.0..34.9 -> "Obese Class I"
        bmi in 35.0..39.9 -> "Obese Class II"
        bmi >= 40 -> "Obese Class III"
        else -> error("Invalid parameters")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMICalculator() {
    var weight by remember{ mutableStateOf("") }
    var height by remember{ mutableStateOf("") }
    var result by remember{ mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    var weightError by remember { mutableStateOf(false) }
    var heightError by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    var dialogMessage = ""




    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "BMI Calculator",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Cyan,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.bmi),
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clip(shape)
                    .border(
                        width = 2.dp,
                        color = Color.Cyan,
                        shape = RoundedCornerShape(size = 40.dp)
                    )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(id = R.string.weight),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        end = 4.dp,
                        top = 8.dp
                    )
                )
                EditTextField(
                    label = R.string.label_weight,
                    suffix = R.string.kg,
                    leadingIcon = R.drawable.weight,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    value = weight,
                    onValueChange = { newValue ->
                        weight = newValue.filter { it.isDigit() || it == '.'}
                    },
                    isError = weightError
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(id = R.string.height),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        end = 4.dp,
                        top = 8.dp
                    )
                )
                EditTextField(
                    label = R.string.label_height,
                    suffix = R.string.m,
                    leadingIcon = R.drawable.height,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    value = height,
                    onValueChange = { newValue ->
                        height = newValue.filter { it.isDigit() || it == '.'}
                    },
                    isError = heightError
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "BMI:     $result",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            end = 20.dp,
                            top = 8.dp
                        )
                        .align(Alignment.Start)
                )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Status: $status",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(
                        end = 20.dp,
                        top = 8.dp
                    )
                    .align(Alignment.Start)
            )
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text(text = "Error") },
                    text = { Text(text = dialogMessage) },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                            onClick = { showErrorDialog = false },
                        ) {
                            Text(
                                text = "OK",
                                style = TextStyle(color = Color.Black)
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(start = 68.dp),
                    onClick = {
                        val weightDouble = weight.toDoubleOrNull()
                        val heightDouble = height.toDoubleOrNull()

                        if (weight.isEmpty() && height.isEmpty()) {
                            dialogMessage = "Please enter both weight and height."
                            weightError = true
                            heightError = true
                            showErrorDialog = true
                        } else if (weight.isEmpty()) {
                            dialogMessage = "Please enter your weight."
                            weightError = true
                            showErrorDialog = true
                        } else if (height.isEmpty()) {
                            dialogMessage = "Please enter your height."
                            heightError = true
                            showErrorDialog = true
                        } else if (weightDouble == null || heightDouble == null || weightDouble == 0.0 || heightDouble == 0.0) {
                            if (weightDouble == 0.0 && heightDouble == 0.0) {
                                dialogMessage = "Both weight and height cannot be zero."
                            } else if (weightDouble == 0.0) {
                                dialogMessage = "Weight cannot be zero."
                            } else if (heightDouble == 0.0) {
                                dialogMessage = "Height cannot be zero."
                            }
                            showErrorDialog = true
                        }  else {
                            weightError = false
                            heightError = false
                            result = calculateBMI(weightDouble, heightDouble)
                            status = getStatus(result.split(" ")[0].toDoubleOrNull() ?: 0.0)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.calculate),
                        style = TextStyle(color = Color.Black)
                    )
                }
                Spacer(modifier = Modifier.width(60.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        weight = ""
                        height = ""
                        result = ""
                        status = ""
                        weightError = false
                        heightError = false
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.clear),
                        style = TextStyle(color = Color.Black)
                    )
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextField(
    @StringRes label: Int,
    @StringRes suffix: Int,
   // @StringRes supportingText: Int,
    @DrawableRes leadingIcon: Int,
    isError: Boolean,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value ,
        onValueChange = onValueChange,
        singleLine = true,
        label = {Text(text = stringResource(id = label))},
        leadingIcon = {Icon(painter = painterResource(id = leadingIcon), contentDescription = null)},
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.Cyan.copy(alpha = 0.3F)
        ),
        isError = isError,
        trailingIcon = { Text(text = stringResource(id = suffix)) },
    )
}
@Preview(showBackground = true)
@Composable
fun BMICalculatorPreview() {
    BMICalculatorAppTheme {
        BMICalculator()
    }
}