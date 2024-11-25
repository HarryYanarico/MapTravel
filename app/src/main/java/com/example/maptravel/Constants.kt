package com.example.maptravel

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.heatmaps.WeightedLatLng
import kotlin.random.Random

object Constants {
    fun getPlaceList():ArrayList<PlaceModel>
    {
        val placesList = ArrayList<PlaceModel>()
            val place1 = PlaceModel(
                40.690389,
                -74.046139,
                "Statue of Liberty"
            )
        placesList.add(place1)

        val place2 = PlaceModel(
            48.85778599650331,
            2.292758808374579,
            "Eiffel Tower"
        )
        placesList.add(place2)

        val place3 = PlaceModel(
            51.500909,
            -0.123799,
            "Big Ben"
        )
        placesList.add(place3)

        val place4 = PlaceModel(
            43.722628,
            10.396492,
            "Leaning Tower of Pisa"
        )
        placesList.add(place4)

        val place5 = PlaceModel(
            41.888622,
            12.490496,
            "Colosseum"
        )
        placesList.add(place5)

        val place6 = PlaceModel(
            37.828937,
            -122.485816,
            "Golden Gate Bridge"
        )
        placesList.add(place6)

        return placesList
    }

    fun getAstLatLong():ArrayList<LatLng>
    {
        return arrayListOf(
            LatLng(-35.016, 143.321),
            LatLng(-34.747, 145.592),
            LatLng(-34.364, 147.891),
            LatLng(-33.501, 150.217),
            LatLng(-32.306, 149.248),
            LatLng(-32.491, 147.309)
        )
    }
    fun getStarCord():ArrayList<LatLng>
    {
        return arrayListOf(
            LatLng(-27.457, 153.040),
            LatLng(-33.852, 151.211),
            LatLng(-37.813, 144.962),
            LatLng(-34.928, 138.599)
        )
    }

    fun getHeatmapData():ArrayList<LatLng>
    {
        val list = ArrayList<LatLng>()
        for (i in 1..300)
        {
            val latitude = Random.nextDouble(50.0,75.0)
            val longitude = Random.nextDouble(50.0, 75.0)
            list.add(LatLng(latitude, longitude))
//            list.add(LatLng(Random.nextDouble(50.0,75.0),Random.nextDouble(50.0,75.0)))
        }
        return list
    }

    fun getHeatmapWeightedData():ArrayList<WeightedLatLng>
    {
        val list = ArrayList<WeightedLatLng>()
        for (i in 1..300)
        {

            list.add(WeightedLatLng(
                LatLng(Random.nextDouble(50.0,75.0),Random.nextDouble(50.0,75.0))
            ,Random.nextDouble(1.0,1000.0))
            )
        }
        return list
    }
}