package com.nutrino.razorpaytestgateway.payment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.nutrino.razorpaytestgateway.BuildConfig
import com.nutrino.razorpaytestgateway.MainActivity
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

//method 2 : gateway
class PaymentActivity: ComponentActivity(), ExternalWalletListener, PaymentResultWithDataListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPayment()

    }

    override fun onExternalWalletSelected(walletName: String?, p1: PaymentData?) {
                Toast.makeText(
            this,
            "External wallet selected: $walletName",
            Toast.LENGTH_LONG
        ).show()

    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Toast.makeText(this, "Transaction completed", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@PaymentActivity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Error in payment processing", Toast.LENGTH_SHORT).show()
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

}