package com.example.location.reminder.reminders.savereminder.selectreminderlocation


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.location.reminder.R
import com.example.location.reminder.app.utils.setDisplayHomeAsUpEnabled
import com.example.location.reminder.base.BaseFragment
import com.example.location.reminder.base.NavigationCommand
import com.example.location.reminder.databinding.FragmentSelectLocationBinding
import com.example.location.reminder.reminders.savereminder.SaveReminderViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.ext.android.inject

class SelectLocationFragment : BaseFragment(), OnMapReadyCallback {

    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding

    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectLocationBinding.inflate(inflater, container, false)

        binding.apply {
            fragment = this@SelectLocationFragment
            viewModel = _viewModel
            lifecycleOwner = this@SelectLocationFragment
        }


        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    @Deprecated("Deprecated in Java", ReplaceWith("inflater.inflate(R.menu.map_options, menu)", "com.example.location.reminder.R"))
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        googleMap.mapType = when (item.itemId) {
            R.id.normal_map -> GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid_map -> GoogleMap.MAP_TYPE_HYBRID
            R.id.satellite_map -> GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrain_map -> GoogleMap.MAP_TYPE_TERRAIN
            else -> GoogleMap.MAP_TYPE_NORMAL
        }
        return true
    }

    fun onSaveLocation() {
        if (marker != null) {
            _viewModel.latitude.value = marker?.position?.latitude
            _viewModel.longitude.value = marker?.position?.longitude
            _viewModel.reminderSelectedLocationStr.value = marker?.title
            _viewModel.navigationCommand.value = NavigationCommand.Back
        } else {
            Toast.makeText(requireContext(), "You Should Choose Location", Toast.LENGTH_SHORT).show()
        }
    }


    private fun onAddMarker(map: GoogleMap) {

        map.setOnMapLongClickListener { latLng ->
            map.clear()
            marker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            )
        }
    }

    private fun onPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            map.clear()
            marker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            marker?.showInfoWindow()
        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val isAccessFineLocationGranted = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val isAccessCoarseLocationGranted = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (isAccessFineLocationGranted && isAccessCoarseLocationGranted) {
            googleMap.isMyLocationEnabled = true
        }

        val sydney = LatLng(30.141177, 31.283978)
        val marker = MarkerOptions()
            .position(sydney)
            .title("Marker in Sydney")
        googleMap.addMarker(marker)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17f))

        onAddMarker(map)
        onPoiClick(map)
    }
}
