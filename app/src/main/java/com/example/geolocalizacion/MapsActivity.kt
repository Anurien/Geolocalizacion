package com.example.geolocalizacion

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geolocalizacion.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val latitude = 42.23639016660114
    private val longitude = -8.71412387331969
    private var colelatLng: LatLng = LatLng(latitude, longitude)
    private var currentLocation: Location? = null
    private lateinit var locationManager: LocationManager

    //Minimo tiempo para updates en Milisegundos
    private val MIN_TIEMPO_ENTRE_UPDATES = (1000 * 60 * 1).toLong() // 1 minuto

    //Minima distancia para updates en metros.
    private val MIN_CAMBIO_DISTANCIA_PARA_UPDATES = (1.5).toLong() // 1.5 metros


    companion object {
        const val REQUEST_LOCATION = 0
    }

   // @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val locListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Aquí se recibe la ubicación actual del usuario
                currentLocation = location
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

            }

            override fun onProviderEnabled(provider: String) {

            }

            override fun onProviderDisabled(provider: String) {

            }
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIEMPO_ENTRE_UPDATES,
            MIN_CAMBIO_DISTANCIA_PARA_UPDATES.toFloat(),
            locListener,
            Looper.getMainLooper()
        )

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Hago visible los botones para apliar y desampliar el mapa
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        createMarker()

        val cole = LatLng(42.23639016660114, -8.71412387331969)
        mMap.addMarker(MarkerOptions().position(cole).title("cole"))

        //Cuando se  ha cargado el mapa le decimos que activa la localización
        enableLocation()


        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

    private fun createMarker() {
        val vigo = LatLng(42.23282, -8.72264)
        mMap.addMarker(MarkerOptions().position(vigo).title("Vigo"))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(vigo, 15f),
            4000,
            null
        )
        val results = FloatArray(5)
        if (currentLocation != null) {
            for (i in crearPuntos().indices) {

                val latitude = currentLocation!!.latitude
                val longitude = currentLocation!!.longitude
                Log.d("asd", "$latitude")
                Location.distanceBetween(
                    latitude,
                    longitude,
                    crearPuntos()[i].latitude,
                    crearPuntos()[i].longitude,
                    results
                )
                if (results[i] > 10) {
                    mMap.addMarker(
                        MarkerOptions().position(distacia(crearPuntos()[i])).title("La mas cerca")
                    )
                }
            }
        }
    }

    /**
     * Método que comprueba que el permiso este activado, pidiendo el permiso y viendo si es igual a el PERMISSION_GRANTED
     */
    private fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


//SupressLint es una interfaz que indica que se deben ignoar las advertencias especificadas
    /**
     * Método que intenta activar la localización
     */
    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        //Si el mapa no está inicializado vete
        if (!::mMap.isInitialized) return
        if (isPermissionsGranted()) {
            //si  ha aceptado los permisos permitimos la localización
            mMap.isMyLocationEnabled = true
        } else {
            //no está activado el permiso , se lo tenemos que pedir a través
            // del siguiente método
            requestLocationPermission()
        }
    }

    /**
     * Método para pedir al usuario que active los permisos
     */
    private fun requestLocationPermission() {
        //Si cumple el primer if significa que le hemos pedido ya al usuario los permisos y los ha rechazado
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()

        } else {
            //Si entra por el else es la primera vez que le pedimos los permisos
            //Se le pasa el companion object para saber si ha aceptado los permisos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        }

    }

    /**
     * Método que captura la respuesta del usuario si acepta los permisos
     */
    @SuppressLint("MissingSuperCall", "MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // cuando el requestcode se igual al companion object definido
        when (requestCode) {
            // Si grantResults no está vacio y el permiso 0 está aceptado signfica que ha acptado nuestros permisos
            REQUEST_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT
                ).show()

            }
            //Por si ha aceptado otro permiso
            else -> {
            }
        }
    }

    /**
     * Método que sirve para comprobar si los permisos siguen activos caundo el usuario
     * se va de la aplicación y vuelve para que la aplicación no rompa
     */
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::mMap.isInitialized) return
        if (!isPermissionsGranted()) {
            !mMap.isMyLocationEnabled
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Método que sirve para que cuando el usuario pulse el botón OnMyLocation
     * le lleve a la ubicación
     */
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * Método que muestra la latitud y longitud de nuestra ubicación cuando pulsamos sobre ella
     */
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude},${p0.longitude} ", Toast.LENGTH_SHORT).show()
        currentLocation = p0
    }


    private fun crearPuntos(): MutableList<LatLng> {
        val randomPoints: MutableList<LatLng> = ArrayList()
        val dLatLng = LatLng(42.23708372564379, -8.714509383173333)
        val gLatLng = LatLng(42.23768047558122, -8.71356698179755)
        val fLatLng = LatLng(42.23915341127386, -8.71947734143543)
        val faLatLng = LatLng(42.23803379805943, -8.71477555189415)
        val bLatLng = LatLng(42.23845365271288, -8.716789835362103)
        randomPoints.add(dLatLng)
        randomPoints.add(gLatLng)
        randomPoints.add(fLatLng)
        randomPoints.add(faLatLng)
        randomPoints.add(bLatLng)
        return randomPoints
    }

    /**
     *Método en el que introduces unas coodernadas y un radio y genera marcas aleatorias en el mapa
     * @param point : el punto de donde se van a generar las marcas
     * @return el valor mas cercano al punto dado
     */
    private fun distacia(point: LatLng): LatLng {
        val myLocation = Location("")
        myLocation.latitude = point.latitude
        myLocation.longitude = point.longitude
        val randomPoints: MutableList<LatLng> = crearPuntos()
        val randomDistances: MutableList<Float> = ArrayList()

        for (i in randomPoints.indices) {
            val l1 = Location("")
            l1.latitude = randomPoints[i].latitude
            l1.longitude = randomPoints[i].longitude
            randomDistances.add(l1.distanceTo(myLocation))
        }
        //Coge el valor mas cercano a la marca actual
        val indexMasCercaMarca = randomDistances.indexOf(Collections.min(randomDistances))
        colelatLng = randomPoints[indexMasCercaMarca]
        return randomPoints[indexMasCercaMarca]
    }

}

