package com.example.googlemapsjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.googlemapsjetpackcompose.ui.theme.GoogleMapsJetpackComposeTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

val indiaState = LatLng(37.8856611,41.1183238)

val defaultCamrePosition = CameraPosition.fromLatLngZoom(indiaState, 15f)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleMapsJetpackComposeTheme {
                val cameraPositionState = rememberCameraPositionState {
                    position = defaultCamrePosition
                }
                val isMapLoaded = remember {
                    mutableStateOf(false)
                }
                Box(modifier = Modifier.fillMaxSize()) {
                        GoogleMapView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(450.dp),
                            cameraPositionState = cameraPositionState,
                            onMapLoaded = {
                                isMapLoaded.value = true
                            }
                        )
                        if (!isMapLoaded.value) {
                            AnimatedVisibility(visible = isMapLoaded.value) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .align(Alignment.Center)
                                )
                            }

                    }
                }
            }
        }
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState, //harita kameranın pozisyonunu tutan bir durum nesnesidir.
    onMapLoaded: () -> Unit //, harita yüklendiğinde çağrılacak bir işlevdir.
) {
    val locationState = rememberMarkerState( //haritada bir konumu temsil eden
        position = indiaState //position parametresi, işaretçinin başlangıç konumunu belirtir.
    )

    val mapUiSettings by remember {
        mutableStateOf(MapUiSettings(compassEnabled = false))
    }
    val mapProperties by remember { //harita özelliklerini temsil eder
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    var showInfoWindow by remember {
        mutableStateOf(false)
    }

    GoogleMap(
        modifier = modifier,
        onMapLoaded = onMapLoaded,
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = mapProperties
    ) {


        Marker(
            state = locationState,
            draggable = true,
            onClick = {
                if (showInfoWindow){
                    locationState.showInfoWindow()
                }else{
                    locationState.hideInfoWindow()
                }
                showInfoWindow = !showInfoWindow
                return@Marker false
            },
            title = "Map Title"
        )


    }


}