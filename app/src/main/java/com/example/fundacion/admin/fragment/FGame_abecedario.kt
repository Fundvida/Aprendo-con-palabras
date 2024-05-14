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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundacion.R
import com.example.fundacion.admin.RefreshGame
import com.example.fundacion.admin.RefreshGamePreguntasVocal
import com.example.fundacion.admin.adapter.AdapterPreg
import com.example.fundacion.admin.adapter.AdapterPregAbecedario
import com.example.fundacion.admin.adapter.AdapterPregVocal
import com.example.fundacion.admin.lpreguntas
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.io.File

private val preg: MutableList<lpreguntas> = mutableListOf()
private lateinit var rvPreg: RecyclerView

class FGame_abecedario : Fragment(), RefreshGamePreguntasVocal {

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button

    private lateinit var imageViewEdit: ImageView
    private lateinit var uploadButtonEdit: Button

    var veriimage = 0

    private val PICK_IMAGE_REQUEST = 1

    private var selectedImageUri: Uri? = null


    override fun refresh() {
        datos()
    }

    override fun modalEditVocales(position: Int) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.aaa_modal_game_vocal_edit)

        val palabra : EditText = dialog.findViewById(R.id.palabra)
        val puntaje : EditText = dialog.findViewById(R.id.puntaje)
        imageViewEdit = dialog.findViewById(R.id.img_preview)

        palabra.setText(preg[position].palabra)
        puntaje.setText(preg[position].puntaje)

        Glide.with(requireContext())
            .load("${config.url}admin/preg-abecedario-imagen/${preg[position].img}")
            .into(imageViewEdit)

        val btncancel : Button = dialog.findViewById(R.id.btn_cancel)
        btncancel.setOnClickListener { dialog.dismiss() }

        val close : ImageButton = dialog.findViewById(R.id.btn_close)
        close.setOnClickListener { dialog.dismiss() }

        val btnaceptar : Button = dialog.findViewById(R.id.btn_aceptar)
        btnaceptar.setOnClickListener {

            if (veriimage == 1){
                val file = File(getRealPathFromURI(selectedImageUri!!))
                val dataPart = FileDataPart(file, name = "file", filename = file.name)
                if(tieneEspacios(file.name)){
                    Toasty.warning(requireContext(), "El nombre de la imagen no debe contener espacios!!!!",
                        Toasty.LENGTH_SHORT).show()
                }else{

                    Fuel.upload("${config.url}admin/preg-imagen-abecedario")
                        .add(dataPart)
                        .responseString { result ->
                            result.fold(
                                success = { response ->
                                    // Procesa la respuesta de la API
                                    val res = JSONObject(response)
                                    var names: String? = null
                                    if (res.getString("type")=="success"){
                                        names = res.getString("name")
                                        val postData = """
                                            {
                                                "palabra": "${palabra.text}",
                                                "img" : "$names",
                                                "puntaje" : "${puntaje.text}"
                                            }
                                        """.trimIndent()

                                        println("============= $res")
                                        Fuel.delete("${config.url}admin/preg-abecedario-edit/"+ preg[position].id )
                                            .jsonBody(postData)
                                            .responseString{result ->
                                                result.fold(
                                                    success = { data ->
                                                        Toasty.warning(requireContext(), "Pregunta Modificada", Toasty.LENGTH_SHORT).show()
                                                        datos()
                                                        palabra.setText("")
                                                        puntaje.setText("")
                                                        selectedImageUri = null
                                                        dialog.dismiss()
                                                    },
                                                    failure = { error ->
                                                        Log.e("Upload", "Error: ${error.exception.message}")
                                                    }
                                                )
                                            }
                                    }
                                },
                                failure = { error ->
                                    Log.e("Upload", "Error: ${error.exception.message}")
                                }
                            )
                        }

                }

            }else{

                val postData = """
                     {
                        "palabra": "${palabra.text}",
                        "img" : "${preg[position].img}",
                        "puntaje" : "${puntaje.text}"
                     }
                """.trimIndent()

                Fuel.delete("${config.url}admin/preg-abecedario-edit/"+ preg[position].id)
                    .jsonBody(postData)
                    .responseString{result ->
                        result.fold(
                            success = { data ->
                                Toasty.warning(requireContext(), "Pregunta Modificada", Toasty.LENGTH_SHORT).show()
                                datos()
                                palabra.setText("")
                                puntaje.setText("")
                                selectedImageUri = null
                                dialog.dismiss()
                            },
                            failure = { error ->
                                Log.e("Upload", "Error: ${error.exception.message}")
                            }
                        )
                    }
            }

        }

        uploadButtonEdit = dialog.findViewById(R.id.upload)
        uploadButtonEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
            veriimage = 1
        }

        dialog.show()
    }

    private var refresh: RefreshGame? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_f_game_abecedario, container, false)



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
            imageView.setImageDrawable(drawable)

        }

        imageView = view.findViewById(R.id.img_preview)
        uploadButton = view.findViewById(R.id.upload)
        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
            veriimage = 0
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

            if (selectedImageUri != null){
                val file = File(getRealPathFromURI(selectedImageUri!!))
                val dataPart = FileDataPart(file, name = "file", filename = file.name)
                if(tieneEspacios(file.name)){
                    Toasty.warning(requireContext(), "El nombre de la imagen no debe contener espacios!!!!",Toasty.LENGTH_SHORT).show()
                }else{
                    Fuel.upload("${config.url}admin/preg-imagen-abecedario")
                        .add(dataPart)
                        .responseString { result ->
                            result.fold(
                                success = { response ->
                                    // Procesa la respuesta de la API
                                    val res = JSONObject(response)
                                    var names: String? = null
                                    if (res.getString("type")=="success"){
                                        names = res.getString("name")
                                        val postData = """
                                            {
                                                "tareas": "${config.IDGameTarea}",
                                                "palabra": "${palabra.text}",
                                                "img" : "$names",
                                                "puntaje" : "${puntaje.text}"
                                            }
                                        """.trimIndent()

                                        Fuel.post("${config.url}admin/preg-abecedario-new")
                                            .jsonBody(postData)
                                            .responseString{result ->
                                                result.fold(
                                                    success = { data ->
                                                        Toasty.success(requireContext(), "Pregunta Agregada", Toasty.LENGTH_SHORT).show()
                                                        datos()
                                                        LinearCreate.visibility = View.GONE
                                                        LinearList.visibility = View.VISIBLE
                                                        palabra.setText("")
                                                        puntaje.setText("")
                                                        selectedImageUri = null

                                                        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_image_search_24)
                                                        imageView.setImageDrawable(drawable)

                                                    },
                                                    failure = { error ->
                                                        Log.e("Upload", "Error: ${error.exception.message}")
                                                    }
                                                )
                                            }
                                    }
                                },
                                failure = { error ->
                                    Log.e("Upload", "Error: ${error.exception.message}")
                                }
                            )
                        }
                }

            }else{
                Toasty.error(requireContext(), "La imagen no puede estar vacio", Toasty.LENGTH_SHORT).show()
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
                imageView.setImageURI(imageUri)
            }else{
                imageViewEdit.setImageURI(imageUri)
            }



        }

    }

    fun datos(){
        Fuel.get("${config.url}admin/preg-abecedario-all/${config.IDGameTarea}").responseString{result ->
            result.fold(
                success = {data->
                    val pregunta = Gson().fromJson(data, Array<lpreguntas>::class.java).toList()
                    preg.clear()
                    preg.addAll(pregunta)
                    val adapter = AdapterPregAbecedario(requireContext(), preg, this@FGame_abecedario)
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