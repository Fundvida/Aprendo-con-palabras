package com.example.fundacion

import android.content.Context
import android.content.SharedPreferences
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT

class TokenManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)

    // Función para guardar el token y los datos en SharedPreferences
    fun saveTokenData(token: String) {
        try {
            val jwt: DecodedJWT = JWT.decode(token)
            val id = jwt.getClaim("id").asInt()
            val docente = jwt.getClaim("docente").asInt()
            val nombres = jwt.getClaim("nombres").asString()
            val apellidos = jwt.getClaim("apellidos").asString()
            val carnet = jwt.getClaim("carnet").asInt()
            val nacimiento = jwt.getClaim("nacimiento").asString()
            val celular = jwt.getClaim("celular").asInt()
            val pais = jwt.getClaim("pais").asString()
            val ciudad = jwt.getClaim("ciudad").asString()
            val user = jwt.getClaim("user").asString()
            val email = jwt.getClaim("email").asString()
            val pass = jwt.getClaim("pass").asString()
            val puntos = jwt.getClaim("puntos").asInt()
            val edad = jwt.getClaim("edad").asInt()
            val estado = jwt.getClaim("estado").asInt()

            with (sharedPref.edit()) {
                putString("token", token)
                putInt("id", id ?: 0)
                putInt("docente", docente ?: 0)
                putString("nombres", nombres)
                putString("apellidos", apellidos)
                putInt("carnet", carnet ?: 0)
                putString("nacimiento", nacimiento)
                putInt("celular", celular ?: 0)
                putString("pais", pais)
                putString("ciudad", ciudad)
                putString("user", user)
                putString("email", email)
                putString("pass", pass)
                putInt("puntos", puntos ?: 0)
                putInt("edad", edad ?: 0)
                putInt("estado", estado ?: 0)
                apply()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función para obtener el token
    fun getToken(): String? {
        return sharedPref.getString("token", null)
    }

    // Función para obtener otros datos según sea necesario
    fun getUserData(): Map<String, Any?> {
        return mapOf(
            "id" to sharedPref.getInt("id", 0),
            "docente" to sharedPref.getInt("docente", 0),
            "nombres" to sharedPref.getString("nombres", null),
            "apellidos" to sharedPref.getString("apellidos", null),
            "carnet" to sharedPref.getInt("carnet", 0),
            "nacimiento" to sharedPref.getString("nacimiento", null),
            "celular" to sharedPref.getInt("celular", 0),
            "pais" to sharedPref.getString("pais", null),
            "ciudad" to sharedPref.getString("ciudad", null),
            "user" to sharedPref.getString("user", null),
            "email" to sharedPref.getString("email", null),
            "pass" to sharedPref.getString("pass", null),
            "puntos" to sharedPref.getInt("puntos", 0),
            "edad" to sharedPref.getInt("edad", 0),
            "estado" to sharedPref.getInt("estado", 0)
        )
    }
}
