package com.gl4.pizzadeliveray

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var selectedIngredients: ArrayList<String> // to store the selected ingredients

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup pizza types in the Spinner
        val pizzaTypes = arrayOf("Medium", "Mini", "Maxi")
        val ingredientsTypes = arrayOf("Fromage", "Tomate", "Olive")
        val pizzaTypesAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pizzaTypes)

        val pizzaTypeSpinner: Spinner = findViewById(R.id.pizzaTypeSpinner)
        val ingredientsButton: Button = findViewById(R.id.ingredientsButton)
        ingredientsButton.text = "Pick ingredients"

        pizzaTypeSpinner.adapter = pizzaTypesAdapter
        selectedIngredients = ArrayList() // initialize selectedIngredients

        // Show multi-choice dialog when ingredients button is clicked
        ingredientsButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Ingredients")
                    .setMultiChoiceItems(ingredientsTypes, null) { _, which, isChecked ->
                        if (isChecked) {
                            selectedIngredients.add(ingredientsTypes[which])
                        } else if (selectedIngredients.contains(ingredientsTypes[which])) {
                            selectedIngredients.remove(ingredientsTypes[which])
                        }
                    }
                    .setPositiveButton("OK", null)
                    .show()
        }

        val orderButton: Button = findViewById(R.id.orderButton)
        orderButton.setOnClickListener { view ->
            val firstName = findViewById<EditText>(R.id.firstNameEditText).text.toString()
            val lastName = findViewById<EditText>(R.id.lastNameEditText).text.toString()
            val address = findViewById<EditText>(R.id.addressEditText).text.toString()
            val pizzaType = pizzaTypeSpinner.selectedItem.toString()
            val ingredientsStr = selectedIngredients.joinToString(", ")

            // Implement logic for ingredients selection

            var didSend =
                    sendOrder(
                            firstName,
                            lastName,
                            address,
                            pizzaType,
                            ingredientsStr
                    ) // Pass ingredients list
            if (didSend) {

                var newIntent = Intent(view.context, SplashScreenActivity::class.java)
                startActivity(newIntent)
            }
        }
    }

    private fun sendOrder(
            firstName: String,
            lastName: String,
            address: String,
            pizzaType: String,
            ingredient: String
    ): Boolean {
        val message =
                "Order from $firstName $lastName, Address: $address, Pizza: $pizzaType, Ingredients: $ingredient"

        // Displaying the toast message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        //        sendSMS("vendorPhoneNumber", message)

        //        sendSMS("vendorPhoneNumber", message)
        // Or sendEmail("vendorEmail", "New Pizza Order", message)

        return true
    }

    private fun sendSMS(phoneNo: String, msg: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    // Implement sendEmail function if needed
}
