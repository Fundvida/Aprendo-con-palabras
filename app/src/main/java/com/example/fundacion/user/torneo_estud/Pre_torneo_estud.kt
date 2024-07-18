package com.example.fundacion.user.torneo_estud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.BaseActivity
import com.example.fundacion.R
import com.example.fundacion.config
import com.example.fundacion.user.EGAME_torneo_list
import com.example.fundacion.user.ESTUDdatos
import com.example.fundacion.user.ESTUDdatos.Companion.temaActivityMap
import com.example.fundacion.user.ETorneo_Game
import com.example.fundacion.user.adapter.Torneo_pre_Adapter
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson




private lateinit var rvTorneo : RecyclerView
private val torneo_list: MutableList<ETorneo_Game> = mutableListOf()


class Pre_torneo_estud : BaseActivity() {


    var IDTORNEO : String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_torneo_estud)


        IDTORNEO = intent.getStringExtra("TORNEO_ID")
        val torneoNombre = intent.getStringExtra("TORNEO_NOMBRE")

        findViewById<TextView>(R.id.title_torneo).text = torneoNombre.toString()

        rvTorneo = findViewById(R.id.torneo_list_pre)
        rvTorneo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        datos()

        /*
        esto es una modal antes de comenzar

                <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1.5"
            android:orientation="vertical"
            >

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="0dp"
                android:layout_weight="1"
                android:orientation="horizontal"
                android:layout_marginHorizontal="20dp"
                >
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:orientation="horizontal"
                    android:gravity="center"
                    android:background="@color/white"
                    >
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/adminU"
                        android:textSize="20dp"
                        android:text="Tiempo a terminar el torneo "
                        android:layout_marginLeft="30dp"

                        />
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/adminU"
                        android:textSize="20dp"
                        android:text="15 min. "
                        android:layout_marginLeft="30dp"

                        />

                </LinearLayout>
                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:orientation="horizontal"

                    android:gravity="center"
                    android:background="@color/adminU"
                    >



                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/white"
                        android:textSize="20dp"
                        android:text="Record Actual : "
                        android:layout_marginLeft="30dp"
                        />
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/white"
                        android:textSize="20dp"
                        android:text=" 0  pts."
                        android:layout_marginLeft="30dp"

                        />


                </LinearLayout>

            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="0dp"
                android:layout_weight="1"
                android:orientation="horizontal"
                android:layout_marginHorizontal="20dp"
                >

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:orientation="horizontal"
                    android:gravity="center"
                    android:background="@color/adminU"
                    >

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/white"
                        android:textSize="20dp"
                        android:text="Cantidad de intentos : "
                        android:layout_marginLeft="30dp"
                        />
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/white"
                        android:textSize="20dp"
                        android:text=" 0 "
                        android:layout_marginLeft="30dp"

                        />


                </LinearLayout>

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:orientation="horizontal"
                    android:gravity="center"
                    android:background="@color/white"
                    >

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/adminU"
                        android:textSize="20dp"
                        android:text="intentos Permitidos : "
                        android:layout_marginLeft="30dp"

                        />
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:fontFamily="@font/digitalt"
                        android:textColor="@color/adminU"
                        android:textSize="20dp"
                        android:text=" 0 "
                        android:layout_marginLeft="30dp"

                        />


                </LinearLayout>


            </LinearLayout>



        </LinearLayout>

         */

    }

    fun datos()
    {
        Log.e("error IDTORNEO", "$IDTORNEO")
        Fuel.get("${config.url}estud/torneo/games-all-pre/$IDTORNEO").responseString{ result ->
            result.fold(
                success = { r ->
                    Log.d("respuesta get","$r")


                    val datos = Gson().fromJson(r, Array<ETorneo_Game>::class.java).toList()
                    torneo_list.clear()
                    torneo_list.addAll(datos)


                    ESTUDdatos.datos.clear()

                    for (item in torneo_list) {
                        ESTUDdatos.datos.add(EGAME_torneo_list("${item.games.tema}", "${item.games.tema_actual}", "${item.games.id}","${item.games.tarea}"))
                    }

                    rvTorneo.adapter = Torneo_pre_Adapter(this, torneo_list)
                },
                failure = {e -> Log.e("error get", "$e")}
            )
        }
    }

    fun comenzar(view : View)
    {



        val currentIndex = ESTUDdatos.indexActivity

        if (currentIndex <  ESTUDdatos.datos.size) {
            val currentTarea = ESTUDdatos.datos[currentIndex]
            val nextActivityClass = temaActivityMap[currentTarea.nameTEMA]

            Log.e("game", "$nextActivityClass")
            Log.e("game", "$currentTarea")

            if (nextActivityClass != null) {
                val intent = Intent(this, nextActivityClass)
                startActivity(intent)
            }
        } else {
            // Manejar el caso cuando todas las tareas han sido completadas
        }


        /*

        val listType = object : TypeToken<List<ETorneo_Game_List>>() {}.type
        val tasks: List<ETorneo_Game_List> = Gson().fromJson(r, listType)
         */
    }
}