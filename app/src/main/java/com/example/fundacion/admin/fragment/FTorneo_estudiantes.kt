package com.example.fundacion.admin.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.RefreshTorneoEstudiante
import com.example.fundacion.admin.Refreshtorneo
import com.example.fundacion.admin.Torneo_estudiante
import com.example.fundacion.admin.adapter.Adapter_admin_torneo_estudiante
import com.example.fundacion.admin.adapter.Adapter_admin_torneo_estudiante_editar
import com.example.fundacion.admin.adapter.Adapter_admin_torneo_estudiante_new
import com.example.fundacion.admin.lestudiantes
import com.example.fundacion.config
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson

private val estud: MutableList<Torneo_estudiante> = mutableListOf()
private lateinit var rvEstu: RecyclerView

class FTorneo_estudiantes : Fragment(), RefreshTorneoEstudiante {


    private val estuListEdit: MutableList<lestudiantes> = mutableListOf()
    private val estuListNew: MutableList<lestudiantes> = mutableListOf()
    private val estuLis: MutableList<lestudiantes> = mutableListOf()

    lateinit var LinearCreate : LinearLayout
    lateinit var LinearList : LinearLayout

    private lateinit var rvEstuEdit: RecyclerView
    private lateinit var rvEstuNew: RecyclerView

    lateinit var dialog : Dialog

    fun retro(){
        LinearCreate.visibility = View.GONE
        LinearList.visibility = View.VISIBLE
    }

    override fun refresh() {
        datos()
    }

    override fun agregar(){
        datos()
        LinearCreate.visibility = View.GONE
        LinearList.visibility = View.VISIBLE
    }

    override fun agregarEdit(){
        datos()
        dialog.dismiss()
    }


    fun retroEdit(){
        dialog.dismiss()
    }


    override fun edit(position: Int){

        Log.e("GET TORNEO", "$position")

        config.IDJuegoTorneo = position.toString()

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.aaa_modal_torneo_estudiante_edit)

        val close : ImageButton = dialog.findViewById(R.id.btn_close)
        close.setOnClickListener { retroEdit(); dialog.dismiss() }


        rvEstuEdit = dialog.findViewById(R.id.listusuarios_edit)
        rvEstuEdit.layoutManager = LinearLayoutManager(context)


        Log.e("modal edit", "$estud")

        datosEDITAR()





        dialog.show()


    }


    private var refresh: Refreshtorneo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_f_torneo_estudiantes, container, false)

        rvEstu = view.findViewById(R.id.listPreguntas)
        rvEstu.layoutManager = LinearLayoutManager(context)

        rvEstuNew = view.findViewById(R.id.listusuarios_edit)
        rvEstuNew.layoutManager = LinearLayoutManager(context)


        val title = view.findViewById<TextView>(R.id.title)
        title.setText(config.NameTorneo)

        LinearCreate = view.findViewById(R.id.view_new)
        LinearList = view.findViewById(R.id.view_list)

        val nuevo : Button = view.findViewById(R.id.nuevo)
        nuevo.setOnClickListener {
            LinearCreate.visibility = View.VISIBLE
            LinearList.visibility = View.GONE
            datosNEW()
        }

        val cancel : Button = view.findViewById(R.id.cancel)
        cancel.setOnClickListener {
            retro()
        }


        val retro = view.findViewById<ImageButton>(R.id.retro)
        retro.setOnClickListener { refresh?.retro(); println("retrooooooooooo") }



        datos()


        return  view
    }



    fun datos(){

        Fuel.get("${config.url}admin/torneo-estudiantes-all/${config.IDTorneo}").responseString{ result ->
            result.fold(
                success = {data->
                    Log.e("falla", "$data")

                    val list = Gson().fromJson(data, Array<Torneo_estudiante>::class.java).toList()
                    estud.clear()
                    estud.addAll(list)


                    val adapter = Adapter_admin_torneo_estudiante(requireContext(), estud, this@FTorneo_estudiantes)
                    rvEstu.adapter = adapter

                },
                failure = { error ->
                    Log.e("GET TORNEO", "Error: ${error.exception.message}")
                }
            )
        }
    }



    fun datosNEW(){

        Fuel.get("${config.url}admin/torneo-estudiantes-add").responseString{ result ->
            result.fold(
                success = {data->

                    val list = Gson().fromJson(data, Array<lestudiantes>::class.java).toList()
                    estuLis.clear()
                    estuLis.addAll(list)



                    val idsParaFiltrar = estud.map { it.estudiante }.toSet()

                    val gamesListFiltrada = estuLis.filter { it.id !in idsParaFiltrar }.toMutableList()

                    estuListNew.clear()
                    estuListNew.addAll(gamesListFiltrada)

                    Log.e("falla NEW", "$estuListNew")


                    val adapter = Adapter_admin_torneo_estudiante_new(requireContext(), estuListNew, this@FTorneo_estudiantes)
                    rvEstuNew.adapter = adapter

                },
                failure = { error ->
                    Log.e("GET TORNEO", "Error: ${error.exception.message}")
                }
            )
        }
    }



    fun datosEDITAR(){

        Fuel.get("${config.url}admin/torneo-estudiantes-add").responseString{ result ->
            result.fold(
                success = {data->

                    val list = Gson().fromJson(data, Array<lestudiantes>::class.java).toList()
                    estuLis.clear()
                    estuLis.addAll(list)



                    val idsParaFiltrar = estud.map { it.estudiante }.toSet()

                    val gamesListFiltrada = estuLis.filter { it.id !in idsParaFiltrar }.toMutableList()

                    estuListEdit.clear()
                    estuListEdit.addAll(gamesListFiltrada)

                    Log.e("falla NEW", "$estuListEdit")


                    val adapter = Adapter_admin_torneo_estudiante_editar(requireContext(), estuListEdit, this@FTorneo_estudiantes)
                    rvEstuEdit.adapter = adapter

                },
                failure = { error ->
                    Log.e("GET TORNEO", "Error: ${error.exception.message}")
                }
            )
        }
    }








    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Refreshtorneo) {
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