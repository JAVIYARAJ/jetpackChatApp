package com.example.jetpackdesign.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.jetpackdesign.R
import com.example.jetpackdesign.Routes
import com.example.jetpackdesign.util.Util.Companion.isEmailValid

@Composable
fun LoginScreen(controller: NavHostController) {

    var email by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var password by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var isPasswordShow by remember {
        mutableStateOf(true)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }


    var emailErrorText by remember {
        mutableStateOf("")
    }
    var isEmailErrorShow by remember {
        mutableStateOf(emailErrorText.isNotEmpty())
    }

    var passwordErrorText by remember {
        mutableStateOf("")
    }
    var isPasswordErrorShow by remember {
        mutableStateOf(passwordErrorText.isNotEmpty())
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            val defaultMargin = 20.dp

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val (imageKey, appIconKey, titleKey, subTitleKey, loaderKey) = createRefs()
                Image(painter = painterResource(id = R.drawable.ic_login_scrren_icon),
                    contentDescription = "login image",
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f,
                    modifier = Modifier
                        .constrainAs(imageKey) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        }
                        .fillMaxHeight()
                        .fillMaxWidth())
                Image(painter = painterResource(id = R.drawable.ic_app_icon),
                    contentDescription = "app icon",
                    modifier = Modifier
                        .constrainAs(appIconKey) {
                            start.linkTo(parent.start, margin = defaultMargin)
                            top.linkTo(parent.top, margin = 50.dp)
                        }
                        .size(50.dp)
                        .clip(RoundedCornerShape(5.dp)))
                Text(text = "Welcome",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.constrainAs(titleKey) {
                        start.linkTo(parent.start, margin = defaultMargin)
                        top.linkTo(appIconKey.bottom, margin = defaultMargin)
                    })
                Text(text = "Sign in to continue",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.constrainAs(subTitleKey) {
                        start.linkTo(parent.start, margin = defaultMargin)
                        top.linkTo(titleKey.bottom, margin = 10.dp)
                    })
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.constrainAs(loaderKey) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                    })
                }
            }
        }

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .then(Modifier.padding(top = 50.dp))
            ) {
                Text(
                    text = "Enter your email",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomTextFiled(value = email, onValueChange = {
                    email = it
                    emailErrorText = if (it.text.isEmailValid() && it.text.isNotEmpty()) {
                        ""
                    } else {
                        "Email is Not Valid"
                    }
                }, isPassword = false, isPasswordShow = false, onIconChange = {

                }, isValidValue = isEmailErrorShow)
                ErrorText(error = emailErrorText)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Enter your password",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(5.dp))
                CustomTextFiled(value = password, onValueChange = {
                    password = it
                    passwordErrorText = if (it.text.isNotEmpty()) {
                        ""
                    } else {
                        "password is Not Valid"
                    }
                }, isPassword = true, isPasswordShow = isPasswordShow, onIconChange = {
                    isPasswordShow = !isPasswordShow
                }, isValidValue = isPasswordErrorShow)
                ErrorText(error = passwordErrorText)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        isEmailErrorShow = email.text.isEmpty()
                        isPasswordErrorShow = password.text.isEmpty()
                        emailErrorText = if (isEmailErrorShow) {
                            "Email is not valid"
                        } else {
                            ""
                        }
                        passwordErrorText = if (isPasswordErrorShow) {
                            "Password is not valid"
                        } else {
                            ""
                        }

                        if (emailErrorText.isEmpty() && passwordErrorText.isEmpty()) {
                            isLoading = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                controller.navigate(Routes.HomeRoute.route) {
                                    popUpTo(Routes.LoginRoute.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                                isLoading = false
                            }, 3000)

                        }
                    }, shape = RoundedCornerShape(10.dp), modifier = Modifier.height(50.dp)
                ) {

                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextFiled(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isPasswordShow: Boolean = false,
    onIconChange: () -> Unit,
    isPassword: Boolean = false,
    isValidValue: Boolean = true
) {

    val toggleValue = if (isPassword) {
        if (isPasswordShow) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    } else {
        VisualTransformation.None
    }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = RoundedCornerShape(10.dp),
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = {
                    onIconChange()
                }) {
                    Image(
                        painter = painterResource(id = if (isPasswordShow) R.drawable.ic_gone_icon else R.drawable.ic_visibility_icon),
                        contentDescription = "eye_icon",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        },
        visualTransformation = toggleValue,
        isError = isValidValue
    )
}

@Composable
fun ErrorText(error: String) {
    Text(
        text = error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(start = 5.dp),
        color = Color.Red
    )
}