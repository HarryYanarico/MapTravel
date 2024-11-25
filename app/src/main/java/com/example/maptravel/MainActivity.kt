package com.example.maptravel

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.gms.maps.model.LatLng
import android.Manifest
import android.content.Intent

//locacion
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.widget.Button
//import android.view.View
//import android.location.Location

//marcadores
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import android.graphics.Color
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
    //drawlines
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.CustomCap
import com.google.android.gms.maps.model.JointType

    //drawPolygon
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.PolygonOptions

import com.google.android.gms.maps.model.GroundOverlayOptions

    //addHeatMap
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.heatmaps.Gradient


class MainActivity : AppCompatActivity(), OnMapReadyCallback {


    private var mGoogleMap: GoogleMap? = null //para crear la app que mas pues
    private lateinit var autocompleteFragment:AutocompleteSupportFragment//para la barra de busqueda si no te funciona tu culpa

    //locacion
    //private val codigoSolicitudPermisodeUbicacion = 123
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    //private var lastLocation: Location? = null

    //variables para usar mas abajo
    private var mLat = -33.87365
    private var mLng = 151.20689

    //private lateinit var googleMap: GoogleMap no funciona


///***************************FUNCIONES PARA USAR***************************///
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //para autocompletado barra de busqueda
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //para la barra de busqueda  prueba 216
        Places.initialize(applicationContext, getString(R.string.google_map_api_key))
            autocompleteFragment =
                supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                        as AutocompleteSupportFragment
            autocompleteFragment.setPlaceFields(listOf(Place.Field.ID,Place.Field.ADDRESS,Place.Field.LAT_LNG))
            autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLong = place.latLng!!
                mLat = latLong.latitude
                mLng = latLong.longitude
                zoomOnMap(latLong)
            }
            override fun onError(status: Status) {
                Toast.makeText(this@MainActivity,"Se produjo algún error",Toast.LENGTH_SHORT).show()
                Log.i("Error de lugar", "Ocurrió un error: $status")
            }
        })

        // tipos de vistas --Start
        val mapOptionButton: ImageButton = findViewById(R.id.mapOptionsMenu)
        val popupMenu = PopupMenu(this, mapOptionButton)
        popupMenu.menuInflater.inflate(R.menu.map_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            changeMap(menuItem.itemId)
            true
        }

        mapOptionButton.setOnClickListener{
            popupMenu.show()
        }
        // tipos de vistas --End

        //ubicacion
        // Inicializar el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        // Configurar el botón para obtener la ubicación
        val btnGetLocation: Button = findViewById(R.id.btnGetLocation)
        btnGetLocation.setOnClickListener {
            //getLocation()
            //enableLocation()
            enableMyLocation()
        }

    //streetview vista de calles
    val fbStreetView = findViewById<FloatingActionButton>(R.id.fbStreetView)
    fbStreetView.setOnClickListener{
        val intento = Intent(this,StreetViewsActivity::class.java)
        intento.putExtra("latitud", mLat)
        intento.putExtra("longitud", mLng)
        startActivity(intento)
    }

    }

    //FUNCION PRINCIPAL
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        //createMaker()
        //enableLocation()
        enableMyLocation()

        mGoogleMap?.setOnMapClickListener {
//            addCircle(it)
            addMarker(it)
            mLat = it.latitude
            mLng = it.longitude
        }

        mGoogleMap?.setOnMapLongClickListener {
            addCustomMarker(R.drawable.location_pin_24dp,it)
        }

        mGoogleMap?.setOnMarkerClickListener {
            it.remove()
            false
        }

        drawLines()
        zoomOnMap(LatLng(70.0, 75.903))
        addMarker(LatLng(-23.684, 133.903))
        mGoogleMap?.setOnPolylineClickListener {
            it.color = -0x00bb00
        }

        drawPolygon()
        mGoogleMap?.setOnPolygonClickListener {
            it.strokeColor = -0x00ff00
        }


        val androidOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.reciclar))
            .position(LatLng(4.566,5.345),100f)

        mGoogleMap?.addGroundOverlay(androidOverlay)

        addHeatMap()
    }

    //barra de busqueda: funcion zoom
    private fun zoomOnMap(latLng:LatLng)
    {
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng, 7.6f)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }


    //tipos de vistas
    private fun changeMap(itemId: Int){
        when (itemId){
        R.id.normal_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        R.id.hybrid_map_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        R.id.satellite_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
        R.id.terrain_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }

    //location

    // Manejar el clic del botón
//    fun onGetLocationClick() {
//        //getLocation()
//    }

    private fun enableMyLocation() {
        // Verificar si el permiso de ubicación está concedido
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Habilitar la capa de "Mi ubicación" en el mapa
            mGoogleMap?.isMyLocationEnabled = true

            // Obtener la última ubicación conocida del dispositivo
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // Verificar si la ubicación es válida
                    if (location != null) {
                        // Crear un objeto LatLng con la ubicación actual
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        // Centrar el mapa en la ubicación actual con un nivel de zoom de 15
                        //mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,14f))
                    }
                }
        } else {
            // Mostrar un mensaje si el permiso de ubicación no está concedido
            Toast.makeText(this, "Permiso de ubicación no concedido", Toast.LENGTH_SHORT).show()
        }
    }


//Marcadores
    private var circle:Circle? = null
    private fun addCircle(centre:LatLng)
    {
        circle?.remove()
        circle = mGoogleMap?.addCircle(CircleOptions()
            .center(centre)
            .radius(1000.0)
            .strokeWidth(8f)
            .strokeColor(Color.parseColor("#FF0000"))
            .fillColor(ContextCompat.getColor(this,R.color.blue))
        )
    }

    private fun addCustomMarker(iconImage: Int, position: LatLng)
    {
        mGoogleMap?.addMarker(MarkerOptions().position(position)
            .draggable(true)
            .title("Custom Marker")
            .snippet("Snippet")
            .icon(BitmapDescriptorFactory.fromResource(iconImage))
        )
    }

    private fun drawLines()
    {
        val DOT: PatternItem = Dot()
        val GAP: PatternItem = Gap(20f)
        // val DASH

        val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

        val polyline = mGoogleMap?.addPolyline(
            PolylineOptions()
                .clickable(true)
                .addAll(Constants.getAstLatLong())
                .endCap(ButtCap())
                .startCap(CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.right_arrow)))
                .color(ContextCompat.getColor(this,R.color.blue))
                .jointType(JointType.BEVEL)
                .width(12f)
                .pattern(PATTERN_POLYLINE_DOTTED)
        )
    }

    private fun addMarker(position: LatLng)
    {
        mGoogleMap?.addMarker(MarkerOptions().position(position)
            .title("Marker")
            .snippet("Snippet")
        )
    }

    private fun drawPolygon()
    {
        val DOT: PatternItem = Dot()
        val GAP: PatternItem = Gap(20f)
        val DASH : PatternItem = Dash(20f)
        val PATTERN_POLYGON_DOT_DASH = listOf(GAP, DOT, DASH,DOT)
        val polygon = mGoogleMap?.addPolygon(
            PolygonOptions()
                .clickable(true)
                .addAll(Constants.getStarCord())
                .fillColor(-0xff00ff)
                .strokeColor(-0xffaabb)
                .strokeWidth(20f)
                .strokePattern(PATTERN_POLYGON_DOT_DASH)
        )
    }

    private fun addHeatMap()
    {
        val heatMapProvider = HeatmapTileProvider.Builder()
//            .data(Constants.getHeatmapData())
//            .radius(20)
//            .build()
            .weightedData(Constants.getHeatmapWeightedData())
            .radius(20)
            .maxIntensity(1000.0)
            .build()
        mGoogleMap?.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))
    }

//    private fun addHeatMap()
//    {
//        val heatMapProvider = HeatmapTileProvider.Builder()
//            .weightedData(Constants.getHeatmapWeightedData())
//            .radius(20)
//            .gradient(Gradient(intArrayOf(Color.GREEN, Color.YELLOW, Color.RED),
//                floatArrayOf(0.2f, 0.6f, 1.0f),
//                1000))
//            .build()
//        mGoogleMap?.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))
//    }

}