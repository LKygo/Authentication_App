package compose.material.theme

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import compose.material.theme.ui.theme.Material3ComposeTheme
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request


interface LoginHandler {
    fun handleLogin(username: String, password: String)
}

class MainActivity : ComponentActivity(), LoginHandler {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            Material3ComposeTheme {
                LoginApplication(){}

            }
        }
    }

    val LocalNavController = compositionLocalOf<NavController> { error("NavController not found") }

    @Composable
    fun LoginApplication() {
        val navController = rememberNavController()

        LoginPage(loginHandler = this@MainActivity, navController = navController)
        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(navController = navController, startDestination = "login_page", builder = {
                composable("login_page", content = { LoginPage(navController = navController) })
                composable(
                    "register_page",
                    content = { RegisterPage(navController = navController) })
                composable("reset_page", content = { ResetPage(navController = navController) })
            })
        }
    }


    override fun handleLogin(username: String, password: String) {
        val navController = LocalNavController.current
        try {
            val client = OkHttpClient()
            val requestBody = FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build()
            val request = Request.Builder()
                .url("https://dummyjson.com/auth/login") // Replace with your API endpoint
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                // Handle successful response
                val responseBody = response.body?.string()
//                   Navigate to welcome page
                HandleLoginResponse(true, navController)

            } else {
                // Handle error response
                val errorBody = response.body?.string()
                HandleLoginResponse(false, navController) // Pass NavController here
            }
        } catch (e: Exception) {
            // Handle network or other errors
            Toast.makeText(this@MainActivity, "Failed login", Toast.LENGTH_SHORT).show()

        }
    }
}
@Composable
fun HandleLoginResponse(response: Boolean, navController: NavController) {
    if (response) {
        Toast.makeText(LocalContext.current, "Successful login", Toast.LENGTH_SHORT).show()
        navController.navigate("welcome_page")
    } else {
        Toast.makeText(LocalContext.current, "Failed login", Toast.LENGTH_SHORT).show()
    }
}
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}



