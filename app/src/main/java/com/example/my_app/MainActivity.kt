package com.example.my_app // <-- SESUAIKAN DENGAN PUNYA KAMU

import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Pengaturan agar tampilan tidak tertutup status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inisialisasi/Hubungkan ID dari XML ke Kotlin (mainactivity)
        val nameInput = findViewById<TextInputEditText>(R.id.nameInput)

        val emailLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)

        val passInput = findViewById<TextInputEditText>(R.id.passInput)

        val confirmPassLayout = findViewById<TextInputLayout>(R.id.confirmPassLayout)
        val confirmPassInput = findViewById<TextInputEditText>(R.id.confirmPassInput)

        val spinnerCity = findViewById<Spinner>(R.id.spinnerCity)

        val loginBtn = findViewById<Button>(R.id.loginBtn)

        //Fungsi spinner
        val listKota = arrayOf("Bandung", "Jakarta", "Surabaya", "Yogyakarta", "Medan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listKota)
        spinnerCity.adapter = adapter

        // 2. Validasi Real-time untuk Email (Poin 02)
        emailInput.addTextChangedListener { text ->
            val email = text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.error = "Format email salah!"
            } else {
                emailLayout.error = null
            }
        }

        // 3. Validasi Real-time untuk Password Match (Poin 02)
        confirmPassInput.addTextChangedListener { text ->
            val pass = passInput.text.toString()
            val confirm = text.toString()
            if (confirm != pass) {
                confirmPassLayout.error = "Password tidak cocok!"
            } else {
                confirmPassLayout.error = null
            }
        }

        // 4. Aksi saat Tombol di-Klik (Submit)
        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val pass = passInput.text.toString()
            val confirm = confirmPassInput.text.toString()
            val name = findViewById<TextInputEditText>(R.id.nameInput).text.toString()

            // Cek RadioGroup (Jenis Kelamin)
            val selectedGenderId = findViewById<RadioGroup>(R.id.rgGender).checkedRadioButtonId
            if (selectedGenderId == -1) { // -1 artinya belum ada yang dipilih
                Toast.makeText(this, "Pilih jenis kelamin dulu ya!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek CheckBox (Hobi min 3)
            val cb1 = findViewById<CheckBox>(R.id.cbCoding).isChecked
            val cb2 = findViewById<CheckBox>(R.id.cbGaming).isChecked
            val cb3 = findViewById<CheckBox>(R.id.cbTraveling).isChecked
            val cb4 = findViewById<CheckBox>(R.id.cbReading).isChecked

            var jumlahHobi = 0
            if (cb1) jumlahHobi++
            if (cb2) jumlahHobi++
            if (cb3) jumlahHobi++
            if (cb4) jumlahHobi++

            if (jumlahHobi < 3) {
                Toast.makeText(this, "Kamu harus pilih minimal 3 hobi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cek apakah ada yang kosong
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show()
            } else if (emailLayout.error != null || confirmPassLayout.error != null) {
                Toast.makeText(this, "Harap perbaiki data yang salah", Toast.LENGTH_SHORT).show()
            } else {
                // Tampilkan Dialog Konfirmasi (Poin 04)
                showConfirmationDialog()
            }

            //Validasi sudah terlewatti semua
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Apakah Anda yakin data yang diisi sudah benar?")
            //User klik tombol ya
            builder.setPositiveButton("Ya") { dialog, which ->
                val kotaTerpilih = spinnerCity.selectedItem.toString()
                Toast.makeText(this, "Berhasil daftar dari kota $kotaTerpilih!", Toast.LENGTH_LONG).show()
            }
            //User klik tombol batal
            builder.setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss() // Cuma nutup dialog, nggak jadi daftar
            }

            builder.show()
        }

        // 5. Gesture Interaction: Long Press pada Tombol (Poin 05)
        loginBtn.setOnLongClickListener {
            Toast.makeText(this, "Format form telah di-reset!", Toast.LENGTH_SHORT).show()
            // Contoh aksi tambahan: mengosongkan form
            nameInput.text?.clear()
            emailInput.text?.clear()
            passInput.text?.clear()
            confirmPassInput.text?.clear()
            true // Artinya event sudah ditangani
        }
    }

    // Fungsi untuk menampilkan Dialog Konfirmasi (Poin 04)
    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin data yang dimasukkan sudah benar?")
            .setPositiveButton("Ya") { _, _ ->
                Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}