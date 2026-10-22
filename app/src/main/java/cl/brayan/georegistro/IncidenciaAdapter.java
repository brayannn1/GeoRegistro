package cl.brayan.georegistro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import org.osmdroid.util.GeoPoint;

public class IncidenciaAdapter
        extends RecyclerView.Adapter<IncidenciaAdapter.IncidenciaViewHolder> {

    private final List<Map<String, Object>> incidencias;

    public IncidenciaAdapter(List<Map<String, Object>> incidencias) {
        this.incidencias = incidencias;
    }

    @NonNull
    @Override
    public IncidenciaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incidencia, parent, false);

        return new IncidenciaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IncidenciaViewHolder holder, int position) {

        Map<String, Object> incidencia = incidencias.get(position);

        holder.txtTitulo.setText(texto(incidencia, "titulo"));
        holder.txtCategoria.setText(
                "Categoría: " + texto(incidencia, "categoria"));
        holder.txtDescripcion.setText(
                texto(incidencia, "descripcion"));
        holder.txtFecha.setText(
                texto(incidencia, "fecha") + " - "
                        + texto(incidencia, "hora"));
        holder.txtEstado.setText(
                "Estado: " + texto(incidencia, "estado"));


        holder.btnVerMapa.setOnClickListener(v -> {
            Object latitudObj = incidencia.get("latitud");
            Object longitudObj = incidencia.get("longitud");

            if (latitudObj == null || longitudObj == null) {
                Toast.makeText(v.getContext(),
                        "Esta incidencia no tiene ubicación guardada",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            double latitud = ((Number) latitudObj).doubleValue();
            double longitud = ((Number) longitudObj).doubleValue();

            Intent intent = new Intent(v.getContext(), Mapa.class);
            Toast.makeText(v.getContext(),
                    "Lat: " + latitud + " | Lon: " + longitud,
                    Toast.LENGTH_LONG).show();
            intent.putExtra("latitud", latitud);
            intent.putExtra("longitud", longitud);
            intent.putExtra("titulo", texto(incidencia, "titulo"));

            v.getContext().startActivity(intent);
        });

    }

    private String texto(Map<String, Object> datos, String campo) {
        Object valor = datos.get(campo);
        return valor == null ? "" : valor.toString();
    }

    @Override
    public int getItemCount() {
        return incidencias.size();
    }

    static class IncidenciaViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitulo, txtCategoria, txtDescripcion;
        TextView txtFecha, txtEstado;
        Button btnVerMapa;

        IncidenciaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTituloIncidencia);
            txtCategoria = itemView.findViewById(R.id.txtCategoriaIncidencia);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionIncidencia);
            txtFecha = itemView.findViewById(R.id.txtFechaIncidencia);
            txtEstado = itemView.findViewById(R.id.txtEstadoIncidencia);
            btnVerMapa = itemView.findViewById(R.id.btnVerMapa);
        }
    }
}
