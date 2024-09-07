package com.raillylinker.springboot_mvc_template.custom_objects

import kotlin.math.*

// [지도 좌표계 관련 유틸]
// 테스트 완료 및 이상없음
object MapCoordinateUtil {
    // (지도 좌표 1 에서 지도 좌표 2 까지의 거리 (미터) 반환, 하버사인 공식)
    fun getDistanceMeterBetweenTwoLatLngCoordinate(
        latlng1: Pair<Double, Double>,
        latlng2: Pair<Double, Double>
    ): Double {
        val r = 6371e3  // 지구 반지름 (미터 단위)
        val lat1Rad = latlng1.first * PI / 180 // deg to rad
        val lat2Rad = latlng2.first * PI / 180 // deg to rad
        val deltaLat = (latlng2.first - latlng1.first) * PI / 180 // deg to rad
        val deltaLon = (latlng2.second - latlng1.second) * PI / 180 // deg to rad

        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(deltaLon / 2) * sin(deltaLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return r * c
    }

    // (여러 지도 좌표들의 중심 좌표(Latitude, Longitude) 반환)
    fun getCenterLatLngCoordinate(latLngList: List<Pair<Double, Double>>): Pair<Double, Double> {
        if (latLngList.isEmpty()) {
            throw IllegalArgumentException("The list must not be empty")
        }

        var xSum = 0.0
        var ySum = 0.0
        var zSum = 0.0

        for (latLng in latLngList) {
            val latRad = latLng.first * PI / 180
            val lonRad = latLng.second * PI / 180

            xSum += cos(latRad) * cos(lonRad)
            ySum += cos(latRad) * sin(lonRad)
            zSum += sin(latRad)
        }

        val total = latLngList.size

        val avgX = xSum / total
        val avgY = ySum / total
        val avgZ = zSum / total

        val centralLon = atan2(avgY, avgX)
        val hypotenuse = sqrt(avgX * avgX + avgY * avgY)
        val centralLat = atan2(avgZ, hypotenuse)

        return Pair(centralLat * 180 / PI, centralLon * 180 / PI)
    }
}