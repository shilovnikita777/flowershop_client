package com.example.flowershop.presentation.screens.common

import android.util.TypedValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.flowershop.R


@Composable
fun CustomTextField(
    value: String,
    placeholder: String,
    iconId: Int,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
        .padding(top = 10.dp),
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(15.dp),
        leadingIcon = {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = iconId
                    ),
                contentDescription = "",
                modifier = Modifier.padding(start = 19.dp, end = 15.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(

        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.subtitle1
            .copy(
                color = if (isError) MaterialTheme.colors.onError
                          else MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Medium
            ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            errorBorderColor = MaterialTheme.colors.onError,
            cursorColor = MaterialTheme.colors.onBackground,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,

        ),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondary
            )
        },
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
    )
}

@Composable
fun CustomPasswordTextField(
    value: String,
    placeholder: String,
    isPasswordHidden: Boolean,
    isError: Boolean = false,
    modifier: Modifier = Modifier
        .padding(top = 10.dp),
    onValueChange: (String) -> Unit,
    onPasswordHiddenChange: (Boolean) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        shape = RoundedCornerShape(15.dp),
        leadingIcon = {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = R.drawable.lock
                    ),
                contentDescription = "",
                modifier = Modifier.padding(start = 19.dp, end = 15.dp)
            )
        },
        trailingIcon = {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = if (isPasswordHidden)
                                 R.drawable.hidetrue
                             else
                                 R.drawable.hidefalse
                    ),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 15.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onPasswordHiddenChange(isPasswordHidden)
                    }
            )
        },
        visualTransformation = if (isPasswordHidden)
                                   PasswordVisualTransformation()
                               else
                                   VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrect = false
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.subtitle1
            .copy(
                color = if (isError) MaterialTheme.colors.onError
                        else MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.Medium
            ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            errorBorderColor = MaterialTheme.colors.onError,
            cursorColor = MaterialTheme.colors.onBackground,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
            ),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.secondary
            )
        },
        isError = isError,
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
    )
}

@Composable
fun h2(text: String, color: Color = MaterialTheme.colors.primary, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.h2,
        color = color,
        modifier = modifier
    )
}

@Composable fun Separator(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                color = Color(0xFFECECEC)
            )
    )
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}