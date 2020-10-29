package fr.labard.simplegpstracker

import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import fr.labard.simplegpstracker.model.util.Constants
import kotlinx.android.synthetic.main.activity_tracker.*
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapFragment : Fragment() {

    lateinit var mapView: MapView
    private lateinit var mapController: IMapController


    private val viewModel by viewModels<MapFragmentViewModel> {
        MapFragmentViewModelFactory((requireContext().applicationContext as GPSApplication).appRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentResult("requestRecordId", bundleOf(
            Constants.Intent.RECORD_ID_EXTRA to
            arguments?.getInt(Constants.Intent.RECORD_ID_EXTRA)
        ))
        val view = layoutInflater.inflate(R.layout.fragment_record_list, container, false)
        mapView = view.findViewById(R.id.activity_tracker_fragment_map)
        buildMapView()

        setFragmentResultListener("requestRecordId") { _, bundle ->
            viewModel.recordId = bundle.getString("recordId")!!
        }

        viewModel.allLocations.observe(viewLifecycleOwner, {
            activity_tracker_toolbar.subtitle = (viewModel
                .getLocationsByRecordId(viewModel.recordId).count().toString())
            val line = Polyline()
            var parcours = viewModel.getLocationsByRecordId(viewModel.recordId)
                .map { GeoPoint(it.latitude, it.longitude) }
            if (parcours.isEmpty()) parcours = mutableListOf(GeoPoint(0.0, 0.0))
            line.setPoints(parcours)
            mapView.overlayManager.add(line)
            mapController.setCenter(parcours.last())
        })

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }*/

    private fun buildMapView() {
        /*requestPermissionsIfNecessary(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        ))*/

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapController = mapView.controller
        mapController.setZoom(3.0)
        val mLocationOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(activity),
            mapView
        )
        mLocationOverlay.enableMyLocation()
        mapView.overlays.add(mLocationOverlay)

        val mCompassOverlay = CompassOverlay(
                activity,
            InternalCompassOrientationProvider(activity),
            mapView
        )

        mCompassOverlay.enableCompass()

        mapView.overlays.apply {
            add(mLocationOverlay)
            add(mCompassOverlay)
        }
    }

    override fun onPause() {
        super.onStop()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    /*override fun onDestroy() {
        super.onDestroy()
        stopService(locationServiceIntent)
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(LocationBroadcastReceiver)
    }*/
}