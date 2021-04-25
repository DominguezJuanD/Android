package com.example.djd_d.baloopizza3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PedidoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PedidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    View vista;
    //TextView mjs;
    Spinner producto1, producto2, producto3;
    EditText Cantidad1, Cantidad2, Cantidad3;
    TextView mjs;
    ArrayList<String> descripcion_productos;
    Fragment miFragment = null;

    Button buttonEn;
    private RequestQueue queue;

    private OnFragmentInteractionListener mListener;


    public PedidoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidoFragment newInstance(String param1, String param2) {
        PedidoFragment fragment = new PedidoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_pedido, container, false);
        producto1 = vista.findViewById(R.id.IdSpinnerPro1);
        producto2 = vista.findViewById(R.id.IdSpinnerPro2);
        producto3 = vista.findViewById(R.id.IdSpinnerPro3);
        Cantidad1 = vista.findViewById(R.id.Cant1);
        Cantidad2 = vista.findViewById(R.id.Cant2);
        Cantidad3 = vista.findViewById(R.id.Cant3);
        buttonEn = vista.findViewById(R.id.buttonEnviar);
        descripcion_productos = new ArrayList<>();
        queue = Volley.newRequestQueue(getContext());
        mjs = vista.findViewById(R.id.mjs);

        String mensaje =this.getArguments().getString("usuario1");

        mjs.setText(mensaje);

        obtenerDatosVolley();
        buttonEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedido();
            }
        });
        return vista;
    }

    private void obtenerDatosVolley() {

        String url = "http://baloopizzeria.000webhostapp.com/android-danyy/RellenarProductos.php"; //aca va la dirección de donde tengas tu archivo que responda en json

        JsonObjectRequest request =new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray miJSONArray = response.getJSONArray("Usuario");
                    descripcion_productos.add("Seleccione...");
                    for(int i = 0; i < miJSONArray.length();i++){
                        JSONObject miJSONOjet = miJSONArray.getJSONObject(i);
                        String NombreProducto = miJSONOjet.getString("descripcion");

                        descripcion_productos.add(NombreProducto);

                    }
                    producto1.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, descripcion_productos));
                    producto2.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, descripcion_productos));
                    producto3.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, descripcion_productos));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(request);
    }

    private void enviarPedido() {
        boolean value = false;
        String url = "http://baloopizzeria.000webhostapp.com/android-danyy/envioPedido.php?usuario="+mjs.getText().toString()+"&";

        if (producto1.getSelectedItem().toString() != "Seleccione..." && (!Cantidad1.getText().toString().equals(""))){
            // Toast.makeText(getContext(), "jajaja", Toast.LENGTH_SHORT).show();
            url = url + "producto1="+producto1.getSelectedItem().toString() + "&cantidad1=" + Cantidad1.getText().toString()+"&";
            value = true;
        }

        if (producto2.getSelectedItem().toString() != "Seleccione..." && (!Cantidad2.getText().toString().equals(""))){
            url = url + "producto2="+producto2.getSelectedItem().toString() + "&cantidad2=" + Cantidad2.getText().toString()+"&";
            value = true;
        }

        if (producto3.getSelectedItem().toString() != "Seleccione..." && (!Cantidad3.getText().toString().equals(""))) {
            url = url + "producto3=" + producto3.getSelectedItem().toString() + "&cantidad3=" + Cantidad3.getText().toString() + "&";
            value = true;
        }
        url = url.replace(" ","%20");
        url = url.substring(0,url.length() - 1);

        if (!value){
            Toast.makeText(getContext(), "Falta Producto o Cantidad", Toast.LENGTH_SHORT).show();
        }


        final JsonObjectRequest jsonObjetRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonResponse = response.getJSONArray("Usuario");
                    JSONObject miJSONOjet = jsonResponse.getJSONObject(0);
                    boolean success = miJSONOjet.getBoolean("success");
                    if (success){

                        producto1.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, descripcion_productos));
                        producto2.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, descripcion_productos));
                        producto3.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, descripcion_productos));

                        Toast.makeText(getContext(), "Eviado Corectamente", Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(getContext(), "Error al Enviar", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de Conexion", Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonObjetRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
