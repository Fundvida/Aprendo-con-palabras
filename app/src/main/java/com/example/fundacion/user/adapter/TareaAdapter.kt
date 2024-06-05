
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundacion.R
import com.example.fundacion.admin.game.PruebaJuego_abecedario
import com.example.fundacion.admin.game.PruebaJuego_construir_oraciones_simples
import com.example.fundacion.admin.game.PruebaJuego_deletreo_simple
import com.example.fundacion.admin.game.PruebaJuego_silabasimple
import com.example.fundacion.admin.game.PruebaJuego_sopaletras_basico
import com.example.fundacion.admin.game.PruebaJuego_vocal
import com.example.fundacion.config
import com.example.fundacion.user.Tarea

class TareaAdapter(private val tareaList: List<Tarea>) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.uuu_adapter_listgame, parent, false)
        return TareaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareaList[position]
        holder.bind(tarea)



        holder.button.setOnClickListener {
            config.IDJuegoPrueba = tarea.id.toString()
            config.NAMEJuegoPrueba = tarea.tarea

            when (tarea.tema) {
                1 -> {
                    val intent = Intent(holder.itemView.context, PruebaJuego_vocal::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                2 -> {
                    val intent = Intent(holder.itemView.context, PruebaJuego_abecedario::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                3 -> {
                    val intent = Intent(holder.itemView.context, PruebaJuego_silabasimple::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                4 -> {
                    val intent = Intent(holder.itemView.context, PruebaJuego_deletreo_simple::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                5 -> {
                    val intent = Intent(holder.itemView.context, PruebaJuego_sopaletras_basico::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                6 -> {
                    val intent = Intent(holder.itemView.context, PruebaJuego_construir_oraciones_simples::class.java)
                    holder.itemView.context.startActivity(intent)
                }
                else -> {
                    println("El número no está definido.")
                }
            }

        }
    }

    override fun getItemCount() = tareaList.size

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tareaTextView: TextView = itemView.findViewById(R.id.tareaTextView)
        val button: ImageButton = itemView.findViewById(R.id.jugar)

        fun bind(tarea: Tarea) {
            tareaTextView.text = tarea.tarea
        }
    }
}
