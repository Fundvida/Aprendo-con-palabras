// NetworkListener.kt

package com.example.fundacion

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

class NetworkListener(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            obtenerIP()
        }
    }

    fun startListening() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopListening() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun obtenerIP() {
        val redes = connectivityManager.allNetworks
        for (red in redes) {
            val capacidades = connectivityManager.getNetworkCapabilities(red)
            if (capacidades?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true) {
                val direcciones = connectivityManager.getLinkProperties(red)?.linkAddresses
                direcciones?.forEach { direccion ->

                    println("esta es la url                ${config.url}")
                    val ip = direccion.address.hostAddress
                    Log.d("SplashActivity", "IP actual: $ip")
                    // Aquí puedes hacer lo que necesites con la dirección IP, como guardarla en SharedPreferences
                    val nuevaIP = modificarIP(ip)
                    Log.d("SplashActivity", "Nueva IP modificada: $nuevaIP")

                    config.url = "http://$nuevaIP/slim/"
                    Log.d("SplashActivity", "Nueva URL: ${config.url}")

                }
            }
        }
    }

    private fun modificarIP(ipOriginal: String): String {
        // Suponiendo que la IP original tiene el formato "192.168.10.165"
        // y queremos modificarla a "192.168.10.1"
        val partesIP = ipOriginal.split(".").toMutableList()
        if (partesIP.size == 4) {
            partesIP[3] = "1" // Cambiar el último octeto a 1
            return partesIP.joinToString(".")
        }
        return ipOriginal // En caso de que la IP original no sea válida
    }
}
