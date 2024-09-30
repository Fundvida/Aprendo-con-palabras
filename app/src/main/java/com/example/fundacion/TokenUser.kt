package com.example.fundacion

import android.content.Context
import android.content.SharedPreferences
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT

class TokenUser(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("MyApp_USER", Context.MODE_PRIVATE)

    fun saveTokenData(token: String) {
        try {
            val jwt: DecodedJWT = JWT.decode(token)
            val id = jwt.getClaim("id").asInt()
            val nombres = jwt.getClaim("nombres").asString()
            val apellidos = jwt.getClaim("apellidos").asString()
            val codigo = jwt.getClaim("codigo").asString()
            val nacimiento = jwt.getClaim("nacimiento").asString()
            val celular = jwt.getClaim("celular").asInt()
            val pais = jwt.getClaim("pais").asString()
            val ciudad = jwt.getClaim("ciudad").asString()
            val email = jwt.getClaim("email").asString()
            val user = jwt.getClaim("user").asString()
            val pass = jwt.getClaim("pass").asString()
            val ci = jwt.getClaim("ci").asInt()
            val rol = jwt.getClaim("rol").asInt()
            val estado = jwt.getClaim("estado").asInt()

            with (sharedPref.edit()) {
                putString("tokenUSER", token)
                putInt("id", id ?: 0)
                putString("nombres", nombres)
                putString("apellidos", apellidos)
                putString("codigo", codigo)
                putString("nacimiento", nacimiento)
                putInt("celular", celular ?: 0)
                putString("pais", pais)
                putString("ciudad", ciudad)
                putString("email", email)
                putString("user", user)
                putString("pass", pass)
                putInt("ci", ci ?: 0)
                putInt("rol", rol ?: 0)
                putInt("estado", estado ?: 0)
                apply()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para obtener el token
    fun getToken(): String? {
        return sharedPref.getString("tokenUSER", null)
    }

    // Función para obtener otros datos según sea necesario
    fun getUserData(): Map<String, Any?> {
        return mapOf(
            "id" to sharedPref.getInt("id", 0),
            "nombres" to sharedPref.getString("nombres", null),
            "apellidos" to sharedPref.getString("apellidos", null),
            "codigo" to sharedPref.getString("codigo", null),
            "nacimiento" to sharedPref.getString("nacimiento", null),
            "celular" to sharedPref.getInt("celular", 0),
            "pais" to sharedPref.getString("pais", null),
            "ciudad" to sharedPref.getString("ciudad", null),
            "email" to sharedPref.getString("email", null),
            "user" to sharedPref.getString("user", null),
            "pass" to sharedPref.getString("pass", null),
            "ci" to sharedPref.getInt("ci", 0),
            "rol" to sharedPref.getInt("rol", 0),
            "estado" to sharedPref.getInt("estado", 0)
        )
    }
}