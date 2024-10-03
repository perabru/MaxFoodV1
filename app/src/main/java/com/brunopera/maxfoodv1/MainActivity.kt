package com.brunopera.maxfoodv1


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class MainActivity : AppCompatActivity() {

    private var balance: Double = 0.0
    private lateinit var tvBalance: TextView
    private lateinit var etAddCredits: EditText
    private lateinit var btnAddCredits: Button
    private lateinit var btnScanQR: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBalance = findViewById(R.id.tv_balance)
        etAddCredits = findViewById(R.id.et_add_credits)
        btnAddCredits = findViewById(R.id.btn_add_credits)
        btnScanQR = findViewById(R.id.btn_scan_qr)

        // Atualiza o saldo ao adicionar créditos
        btnAddCredits.setOnClickListener {
            val creditsToAdd = etAddCredits.text.toString().toDoubleOrNull()
            if (creditsToAdd != null) {
                addCredits(creditsToAdd)
                etAddCredits.text.clear()
            }
        }

        // Inicia o scanner de QR Code
        btnScanQR.setOnClickListener {
            scanQRCode()
        }
    }

    // Função para adicionar créditos
    private fun addCredits(credits: Double) {
        balance += credits
        updateBalance()
    }

    // Função para atualizar o saldo na interface
    private fun updateBalance() {
        tvBalance.text = "Saldo: R$%.2f".format(balance)
    }

    // Função para iniciar o scanner de QR Code
    private fun scanQRCode() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escaneie o QR Code")
        integrator.setCameraId(0) // Use a câmera traseira
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    // Função que lida com o resultado da leitura do QR Code
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents != null) {
            // QR Code escaneado com sucesso, adicione os créditos
            val scannedValue = result.contents.toDoubleOrNull()
            if (scannedValue != null) {
                addCredits(scannedValue)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
