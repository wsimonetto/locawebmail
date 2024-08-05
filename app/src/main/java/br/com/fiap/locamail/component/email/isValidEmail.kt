package br.com.fiap.locamail.component.email

import android.util.Patterns

class EmailValidator {
    companion object {
        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}