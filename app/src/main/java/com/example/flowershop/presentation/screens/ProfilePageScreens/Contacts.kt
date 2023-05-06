package com.example.flowershop.presentation.screens.ProfilePageScreens

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.example.flowershop.R
import com.example.flowershop.presentation.screens.common.h2
import com.example.flowershop.presentation.screens.common.noRippleClickable
import com.example.flowershop.util.Constants.GMAIL_ADDRESS
import com.example.flowershop.util.Constants.TELEGRAM_URL
import com.example.flowershop.util.Constants.VK_URL

@Composable
fun Contacts() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
    ) {
        h2(
            text = "Контакты",
            color = MaterialTheme.colors.onBackground
        )

        val vkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(VK_URL))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
                .noRippleClickable {
                    context.startActivity(vkIntent)
                }
        ) {
            Image(
                painterResource(id = R.drawable.vk),
                contentDescription = "vk",
                modifier = Modifier
                    .size(36.dp)
            )
            Text(
                text = "Никита Шилов",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        val tgIntent = Intent(Intent.ACTION_VIEW, Uri.parse(TELEGRAM_URL))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
                .noRippleClickable {
                    context.startActivity(tgIntent)
                }
        ) {
            Image(
                painterResource(id = R.drawable.telegram),
                contentDescription = "tg",
                modifier = Modifier
                    .size(36.dp)
            )
            Text(
                text = "Никита Шилов",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        val email = GMAIL_ADDRESS
        val subject = "Мобильное приложение магазина цветов"
        val gmailIntent = Intent(Intent.ACTION_SEND).apply {
            data = "mailto:$email?subject=${subject}".toUri()
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
                .noRippleClickable {
                    gmailIntent.resolveActivityInfo(context.packageManager, 0)?.let {
                        context.startActivity(gmailIntent)
                    }
                }
        ) {
            Image(
                painterResource(id = R.drawable.gmail),
                contentDescription = "gmail",
                modifier = Modifier
                    .size(36.dp)
            )
            Text(
                text = "Никита Шилов",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 16.sp
                ),
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
    }
}