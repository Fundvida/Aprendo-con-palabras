package com.example.fundacion.admin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.RefreshGame
import com.example.fundacion.admin.RefreshGamePreguntasDeletreoSimples
import com.example.fundacion.admin.adapter.AdapterPregDeletreoSimple
import com.example.fundacion.admin.lpreguntas
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty


private val preg: MutableList<lpreguntas> = mutableListOf()
private lateinit var rvPreg: RecyclerView

class FGame_deletreo_simple : Fragment(), RefreshGamePreguntasDeletreoSimples {




    private lateinit var uploadButtonEdit: Button

    var veriimage = 0

    private val PICK_IMAGE_REQUEST = 1

    private var selectedImageUri: Uri? = null


    override fun refresh() {
        datos()
    }

    override fun modalEditSilabas(position: Int) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.aaa_modal_game_deletreo_simple_edit)

        val palabra : EditText = dialog.findViewById(R.id.palabra)
        val puntaje : EditText = dialog.findViewById(R.id.puntaje)


        palabra.setText(preg[position].palabra)
        puntaje.setText(preg[position].puntaje)


        val btncancel : Button = dialog.findViewById(R.id.btn_cancel)
        btncancel.setOnClickListener { dialog.dismiss() }

        val close : ImageButton = dialog.findViewById(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }

        val btnaceptar : Button = dialog.findViewById(R.id.btn_aceptar)
        btnaceptar.setOnClickListener {

            val postData = """
                     {
                        "palabra": "${palabra.text}",
                        "puntaje" : "${puntaje.text}"
                        
                     }
                """.trimIndent()

            Fuel.delete("${config.url}admin/preg-deletreo-simples-edit/"+ preg[position].id)
                .jsonBody(postData)
                .responseString{result ->
                    result.fold(
                        success = { data ->
                            Toasty.warning(requireContext(), "Pregunta Modificada", Toasty.LENGTH_SHORT).show()
                            datos()
                            palabra.setText("")
                            puntaje.setText("")

                            dialog.dismiss()
                        },
                        failure = { error ->
                            Log.e("Upload", "Error: ${error.exception.message}")
                        }
                    )
                }


        }



        dialog.show()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private var refresh: RefreshGame? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_f_game_deletreo_simple, container, false)

        val title = view.findViewById<TextView>(R.id.title)
        title.setText(config.GameTarea)


        val LinearCreate : LinearLayout = view.findViewById(R.id.view_new)
        val LinearList : LinearLayout = view.findViewById(R.id.view_list)

        val retro = view.findViewById<ImageButton>(R.id.retro)
        retro.setOnClickListener { refresh?.retro() }

        rvPreg = view.findViewById(R.id.listPreguntas)
        rvPreg.layoutManager = LinearLayoutManager(context)

        datos()

        val nuevo : Button = view.findViewById(R.id.nuevo)
        nuevo.setOnClickListener {
            LinearCreate.visibility = View.VISIBLE
            LinearList.visibility = View.GONE
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_image_search_24)

        }




        val cancel : Button = view.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            LinearCreate.visibility = View.GONE
            LinearList.visibility = View.VISIBLE
        }

        val palabra : EditText = view.findViewById(R.id.palabra)
        val puntaje : EditText = view.findViewById(R.id.puntaje)


        val aceptar : Button = view.findViewById(R.id.aceptar)
        aceptar.setOnClickListener {
            val postData = """
                                            {
                                                "tareas": "${config.IDGameTarea}",
                                                "palabra": "${palabra.text}",
                                                "puntaje" : "${puntaje.text}"
                                            }
            """.trimIndent()

            Fuel.post("${config.url}admin/preg-deletreo-simples-new")
                .jsonBody(postData)
                .responseString{result ->
                    result.fold(
                        success = { data ->
                            println(data)
                            Toasty.success(requireContext(), "Pregunta Agregada", Toasty.LENGTH_SHORT).show()
                            datos()
                            LinearCreate.visibility = View.GONE
                            LinearList.visibility = View.VISIBLE
                            palabra.setText("")
                            puntaje.setText("")
                            selectedImageUri = null

                            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_image_search_24)

                        },
                        failure = { error ->
                            Log.e("Upload", "Error: ${error.exception.message}")
                        }
                    )
                }
        }






        return view
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data?.data
            val imageUri = data.data

            if (veriimage == 0){
            }else{
            }



        }

    }

    fun datos(){
        Fuel.get("${config.url}admin/preg-silabas-simples-all/${config.IDGameTarea}").responseString{result ->
            result.fold(
                success = {data->
                    val pregunta = Gson().fromJson(data, Array<lpreguntas>::class.java).toList()
                    preg.clear()
                    preg.addAll(pregunta)
                    val adapter = AdapterPregDeletreoSimple(requireContext(), preg, this@FGame_deletreo_simple)
                    rvPreg.adapter = adapter
                },
                failure = { error ->
                    Log.e("Upload", "Error: ${error.exception.message}")
                }
            )
        }
    }


    fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = activity?.contentResolver?.query(uri, projection, null, null, null)
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath: String = cursor?.getString(columnIndex ?: 0) ?: ""
        cursor?.close()
        return filePath
    }

    fun tieneEspacios(nombre: String): Boolean {
        return nombre.contains(" ")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is RefreshGame) {
            refresh = context
        } else {
            throw RuntimeException("$context debe implementar OnFragmentInteractionListener")
        }
    }
    override fun onDetach() {
        super.onDetach()
        refresh = null
    }




}