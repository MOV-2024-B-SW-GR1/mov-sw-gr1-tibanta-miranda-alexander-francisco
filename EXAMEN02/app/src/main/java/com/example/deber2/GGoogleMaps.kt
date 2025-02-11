package com.example.deber2

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class GGoogleMaps : AppCompatActivity() {
    private lateinit var mapa: GoogleMap
    private var latitud: Double? = null
    private var longitud: Double? = null
    val nombrePermisoFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    val nombrePermisoCoarse = android.Manifest.permission.ACCESS_COARSE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ggoogle_maps)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener coordenadas enviadas desde EmpresaDetailActivity
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)

        solicitarPermisos()
        inicializarLogicaMapa()
    }

    fun solicitarPermisos() {
        if (!tengoPermisos()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(nombrePermisoFine, nombrePermisoCoarse), 1
            )
        }
    }

    fun tengoPermisos(): Boolean {
        val contexto = applicationContext
        val permisoFine = ContextCompat.checkSelfPermission(contexto, nombrePermisoFine)
        val permisoCoarse = ContextCompat.checkSelfPermission(contexto, nombrePermisoCoarse)
        return permisoFine == PackageManager.PERMISSION_GRANTED && permisoCoarse == PackageManager.PERMISSION_GRANTED
    }

    fun inicializarLogicaMapa() {
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentoMapa.getMapAsync { googleMap ->
            mapa = googleMap
            establecerConfiguracionMapa()

            // Verifica si hay coordenadas válidas
            if (latitud != null && longitud != null && latitud != 0.0 && longitud != 0.0) {
                val ubicacionGuardada = LatLng(latitud!!, longitud!!)
                val titulo = "Ubicación Guardada"
                val marcador = anadirMarcador(ubicacionGuardada, titulo)
                marcador.tag = titulo
                moverCamaraConZoom(ubicacionGuardada)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun establecerConfiguracionMapa() {
        with(mapa) {
            if (tengoPermisos()) {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
        }
    }

    fun moverCamaraConZoom(latLang: LatLng, zoom: Float = 15f) {
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))
    }

    fun anadirMarcador(latLang: LatLng, title: String): Marker {
        return mapa.addMarker(MarkerOptions().position(latLang).title(title))!!
    }
}