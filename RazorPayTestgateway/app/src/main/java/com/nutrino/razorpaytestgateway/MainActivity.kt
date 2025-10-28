package com.nutrino.razorpaytestgateway

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutrino.razorpaytestgateway.payment.PaymentActivity
import com.nutrino.razorpaytestgateway.ui.theme.RazorPayTestgatewayTheme
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.razorpay.ExternalWalletListener
import org.json.JSONObject



//method 1
class MainActivity : ComponentActivity(), PaymentResultWithDataListener, ExternalWalletListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Checkout.preload(applicationContext)

        setContent {
            RazorPayTestgatewayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PaymentScreen(
                        modifier = Modifier.padding(innerPadding),
                        onPaymentClick = { startPayment() }
                    )
                }
            }
        }
    }

    private fun startPayment() {
        val checkout = Checkout()
        checkout.setKeyID(BuildConfig.RAZORPAY_API_KEY)

        try {
            val options = JSONObject().apply {
                put("name", "Akshay Sarapure's")
                put("description", "Test Payment Subscription charges : ")
                put("image", "https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin_Icon.png")
                put("currency", "INR")
                put("amount", "1000") // Amount in paise
                put("send_sms_hash", true)

                val prefill = JSONObject().apply {
                    put("email", "test@razorpay.com")
                    put("contact", "9021066696")
                }
                put("prefill", prefill)
            }

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(paymentId: String?, paymentData: PaymentData?) {
        Toast.makeText(
            this,
            "Payment Successful\nPayment ID: $paymentId",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPaymentError(errorCode: Int, errorDescription: String?, paymentData: PaymentData?) {
        if(Checkout.NETWORK_ERROR == errorCode){
            Toast.makeText(
                this,
                "Network Error. Please check your internet connection and try again.",
                Toast.LENGTH_LONG
            ).show()
            return

        }else if(Checkout.PAYMENT_CANCELED == errorCode){
            Toast.makeText(
                this,
                "Payment Cancelled by user.",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        else if(Checkout.TLS_ERROR	== errorCode){
            Toast.makeText(
                this,
                "TLS Error. Please ensure your device supports TLS 1.2 or higher.",
                Toast.LENGTH_LONG
            ).show()
            return
        }else{
            Toast.makeText(
                this,
                "Payment Failed with error code: $errorCode",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onExternalWalletSelected(walletName: String?, paymentData: PaymentData?) {
        Toast.makeText(
            this,
            "External wallet selected: $walletName",
            Toast.LENGTH_LONG
        ).show()
    }
}

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    onPaymentClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onPaymentClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Pay Now")
        }
    }
}
//
//class MainActivity : ComponentActivity(){
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        Checkout.preload(applicationContext)
//
//        setContent {
//            RazorPayTestgatewayTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Box(
//                        modifier = Modifier.padding(innerPadding),
//                        contentAlignment = Alignment.Center
//                    ){
//                        Button(onClick = {
//                            val intent = Intent(this@MainActivity,PaymentActivity::class.java)
//                            startActivity(intent)
//
//                        }) {
//                            Text("PayNow")
//                        }
//
//                    }
//                }
//            }
//        }
//    }
//}