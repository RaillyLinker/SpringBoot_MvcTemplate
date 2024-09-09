package com.raillylinker.springboot_mvc_template.controllers.c9_service1_tk_v1_mapCoordinateCalculation

import com.raillylinker.springboot_mvc_template.annotations.CustomTransactional
import com.raillylinker.springboot_mvc_template.configurations.database_configs.Db1MainConfig
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.repositories.Db1_Native_Repository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.repositories.Db1_Template_TestMap_Repository
import com.raillylinker.springboot_mvc_template.data_sources.database_sources.db1_main.entities.Db1_Template_TestMap
import com.raillylinker.springboot_mvc_template.custom_objects.MapCoordinateUtil
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class C9Service1TkV1MapCoordinateCalculationService(
    // (프로젝트 실행시 사용 설정한 프로필명 (ex : dev8080, prod80, local8080, 설정 안하면 default 반환))
    @Value("\${spring.profiles.active:default}") private var activeProfile: String,

    // (Database Repository)
    private val db1TemplateTestMapRepository: Db1_Template_TestMap_Repository,
    private val db1NativeRepository: Db1_Native_Repository
) {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @CustomTransactional([Db1MainConfig.TRANSACTION_NAME])
    fun api0InsertDefaultCoordinateDataToDatabase(httpServletResponse: HttpServletResponse) {
        db1TemplateTestMapRepository.deleteAll()

        val latLngList: List<Pair<Double, Double>> = listOf(
            Pair(37.5845885, 127.0001891),
            Pair(37.6060504, 126.9607987),
            Pair(37.5844214, 126.9699813),
            Pair(37.5757558, 126.9710255),
            Pair(37.5764907, 126.968655),
            Pair(37.5786667, 127.0156223),
            Pair(37.561697, 126.9968491),
            Pair(37.5880051, 127.0181872),
            Pair(37.5713246, 126.9635654),
            Pair(37.5922066, 127.0135319),
            Pair(37.5690038, 126.9632755),
            Pair(37.584865, 126.948639),
            Pair(37.5690454, 127.0232121),
            Pair(37.5634635, 127.015948),
            Pair(37.5748642, 127.0155003),
            Pair(37.5708604, 126.9612919),
            Pair(37.5570078, 126.9533333),
            Pair(37.5726188, 127.0576283),
            Pair(37.5914225, 127.0129648),
            Pair(37.5659102, 127.0217363)
        )

        for (latLng in latLngList) {
            db1TemplateTestMapRepository.save(
                Db1_Template_TestMap(
                    latLng.first,
                    latLng.second
                )
            )
        }
        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api1GetDistanceMeterBetweenTwoCoordinate(
        httpServletResponse: HttpServletResponse,
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): C9Service1TkV1MapCoordinateCalculationController.Api1GetDistanceMeterBetweenTwoCoordinateOutputVo? {
        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C9Service1TkV1MapCoordinateCalculationController.Api1GetDistanceMeterBetweenTwoCoordinateOutputVo(
            MapCoordinateUtil.getDistanceMeterBetweenTwoLatLngCoordinate(
                Pair(latitude1, longitude1),
                Pair(latitude2, longitude2)
            )
        )
    }


    ////
    fun api2ReturnCenterCoordinate(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1MapCoordinateCalculationController.Api2ReturnCenterCoordinateInputVo
    ): C9Service1TkV1MapCoordinateCalculationController.Api2ReturnCenterCoordinateOutputVo? {
        val latLngCoordinate = ArrayList<Pair<Double, Double>>()

        for (coordinate in inputVo.coordinateList) {
            latLngCoordinate.add(
                Pair(coordinate.centerLatitude, coordinate.centerLongitude)
            )
        }

        val centerCoordinate = MapCoordinateUtil.getCenterLatLngCoordinate(
            latLngCoordinate
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C9Service1TkV1MapCoordinateCalculationController.Api2ReturnCenterCoordinateOutputVo(
            centerCoordinate.first,
            centerCoordinate.second
        )
    }


    ////
    @CustomTransactional([Db1MainConfig.TRANSACTION_NAME])
    fun api3InsertCoordinateDataToDatabase(
        httpServletResponse: HttpServletResponse,
        inputVo: C9Service1TkV1MapCoordinateCalculationController.Api3InsertCoordinateDataToDatabaseInputVo
    ): C9Service1TkV1MapCoordinateCalculationController.Api3InsertCoordinateDataToDatabaseOutputVo? {
        db1TemplateTestMapRepository.save(
            Db1_Template_TestMap(
                inputVo.latitude,
                inputVo.longitude
            )
        )

        val coordinateList =
            ArrayList<C9Service1TkV1MapCoordinateCalculationController.Api3InsertCoordinateDataToDatabaseOutputVo.Coordinate>()
        val latLngCoordinate = ArrayList<Pair<Double, Double>>()

        for (testMap in db1TemplateTestMapRepository.findAll()) {
            coordinateList.add(
                C9Service1TkV1MapCoordinateCalculationController.Api3InsertCoordinateDataToDatabaseOutputVo.Coordinate(
                    testMap.latitude,
                    testMap.longitude
                )
            )

            latLngCoordinate.add(
                Pair(testMap.latitude, testMap.longitude)
            )
        }

        val centerCoordinate = MapCoordinateUtil.getCenterLatLngCoordinate(
            latLngCoordinate
        )

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C9Service1TkV1MapCoordinateCalculationController.Api3InsertCoordinateDataToDatabaseOutputVo(
            coordinateList,
            C9Service1TkV1MapCoordinateCalculationController.Api3InsertCoordinateDataToDatabaseOutputVo.Coordinate(
                centerCoordinate.first,
                centerCoordinate.second
            )
        )
    }


    ////
    @CustomTransactional([Db1MainConfig.TRANSACTION_NAME])
    fun api4DeleteAllCoordinateDataFromDatabase(httpServletResponse: HttpServletResponse) {
        db1TemplateTestMapRepository.deleteAll()
        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
    }


    ////
    fun api5SelectCoordinateDataRowsInRadiusKiloMeterSample(
        httpServletResponse: HttpServletResponse,
        anchorLatitude: Double,
        anchorLongitude: Double,
        radiusKiloMeter: Double
    ): C9Service1TkV1MapCoordinateCalculationController.Api5SelectCoordinateDataRowsInRadiusKiloMeterSampleOutputVo? {
        val entityList =
            db1NativeRepository.forC9N5(
                anchorLatitude,
                anchorLongitude,
                radiusKiloMeter
            )

        val coordinateCalcResultList =
            ArrayList<C9Service1TkV1MapCoordinateCalculationController.Api5SelectCoordinateDataRowsInRadiusKiloMeterSampleOutputVo.CoordinateCalcResult>()
        for (entity in entityList) {
            coordinateCalcResultList.add(
                C9Service1TkV1MapCoordinateCalculationController.Api5SelectCoordinateDataRowsInRadiusKiloMeterSampleOutputVo.CoordinateCalcResult(
                    entity.uid,
                    entity.latitude,
                    entity.longitude,
                    entity.distanceKiloMeter
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C9Service1TkV1MapCoordinateCalculationController.Api5SelectCoordinateDataRowsInRadiusKiloMeterSampleOutputVo(
            coordinateCalcResultList
        )
    }


    ////
    fun api6SelectCoordinateDataRowsInCoordinateBoxSample(
        httpServletResponse: HttpServletResponse,
        northLatitude: Double, // 북위도 (ex : 37.771848)
        eastLongitude: Double, // 동경도 (ex : 127.433549)
        southLatitude: Double, // 남위도 (ex : 37.245683)
        westLongitude: Double // 남경도 (ex : 126.587602)
    ): C9Service1TkV1MapCoordinateCalculationController.Api6SelectCoordinateDataRowsInCoordinateBoxSampleOutputVo? {
        val entityList =
            db1NativeRepository.forC9N6(
                northLatitude,
                eastLongitude,
                southLatitude,
                westLongitude
            )

        val coordinateCalcResultList =
            ArrayList<C9Service1TkV1MapCoordinateCalculationController.Api6SelectCoordinateDataRowsInCoordinateBoxSampleOutputVo.CoordinateCalcResult>()
        for (entity in entityList) {
            coordinateCalcResultList.add(
                C9Service1TkV1MapCoordinateCalculationController.Api6SelectCoordinateDataRowsInCoordinateBoxSampleOutputVo.CoordinateCalcResult(
                    entity.uid,
                    entity.latitude,
                    entity.longitude
                )
            )
        }

        httpServletResponse.setHeader("api-result-code", "")
        httpServletResponse.status = HttpStatus.OK.value()
        return C9Service1TkV1MapCoordinateCalculationController.Api6SelectCoordinateDataRowsInCoordinateBoxSampleOutputVo(
            coordinateCalcResultList
        )
    }
}