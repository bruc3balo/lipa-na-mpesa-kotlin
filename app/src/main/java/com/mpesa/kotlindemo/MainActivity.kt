package com.mpesa.kotlindemo

import android.Manifest.permission.INTERNET
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.mpesa.kotlindemo.databinding.ActivityMainBinding
import com.mpesa.kotlindemo.viewmodel.MpesaViewModel

class MainActivity : AppCompatActivity() {

     lateinit var mpesaViewModel: MpesaViewModel
     lateinit var binding: ActivityMainBinding
     private val MY_PERMISSIONS_REQUEST_INTERNET: Int = 2;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mpesaViewModel = ViewModelProvider(this)[MpesaViewModel::class.java]
        binding.sendMoney.setOnClickListener {
            if(permission()) {
                Thread {
                    Looper.prepare()
                    val amount :String = binding.amount.text.toString().trim()
                    val phone :String = binding.phone.text.toString().trim()
                    if(validateForm(amount, phone))
                        if(mpesaViewModel.sendLipaNaMpesaRequest(amount, "Nikujaribu tu",phone, "Ile ile tu"))
                            showToast("Haiya lipa basi")
                        else
                            showToast("Umeuma nje")
                }.start()
            }
        }
    }

    fun permission () : Boolean{
        if (ContextCompat.checkSelfPermission(this, INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(INTERNET), MY_PERMISSIONS_REQUEST_INTERNET)
            return false
        }

        return true
    }


    private fun validateForm(amount: String, phone:String) : Boolean{

        if(amount.isEmpty()) {
            showToast("Weka pesa buda, ala")
            return false
        } else if(phone.isEmpty()) {
            showToast("Sasa ntajuaje unatimia nani")
            return false
        } else if(!phone.startsWith("254") && !phone.startsWith("01")) {
            showToast("Kwani uko majuu?")
            return false
        } else if(amount.toInt() < 1) {
            showToast("Wewe sasa unanipima, ata kama ni test")
            return false
        }

        showToast("Hapo sawa")
        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}