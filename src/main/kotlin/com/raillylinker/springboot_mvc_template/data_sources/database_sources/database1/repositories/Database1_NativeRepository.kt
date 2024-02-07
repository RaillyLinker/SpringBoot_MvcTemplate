package com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.repositories

import com.raillylinker.springboot_mvc_template.data_sources.database_sources.database1.tables.Database1_Template_TestData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

// 주의 : NativeRepository 의 반환값으로 기본 Entity 객체는 매핑되지 않으므로 OutputVo Interface 를 작성하여 사용할것.
// sql 문은 한줄로 표기 할 것을 권장. (간편하게 복사해서 사용하며 디버그하기 위하여)
// Output Interface 변수에 is 로 시작되는 변수는 매핑이 안되므로 사용하지 말것.
@Repository
interface Database1_NativeRepository : JpaRepository<Database1_Template_TestData, Long> {
    // <C7>
    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            test_data.content as content, 
            test_data.random_num as randomNum, 
            ABS(test_data.random_num-:num) as distance 
            from template.test_data 
            where row_delete_date is NULL 
            order by 
            distance
            """
    )
    fun selectListForC7N5(
        @Param(value = "num") num: Int
    ): List<SelectListForC7N5OutputVo>

    interface SelectListForC7N5OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var distance: Int
    }


    ////
    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.content as content, 
            test_data.random_num as randomNum, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            ABS(TIMESTAMPDIFF(SECOND, test_data.row_create_date, :date)) as timeDiffSec 
            from template.test_data 
            where row_delete_date is NULL 
            order by 
            timeDiffSec
            """
    )
    fun selectListForC7N6(
        @Param(value = "date") date: String
    ): List<SelectListForC7N6OutputVo>

    interface SelectListForC7N6OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var timeDiffSec: Long
    }


    ////
    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            test_data.content as content, 
            test_data.random_num as randomNum, 
            ABS(test_data.random_num-:num) as distance 
            from 
            template.test_data 
            where row_delete_date is NULL 
            order by distance
            """,
        countQuery = """
            select 
            count(*) 
            from 
            template.test_data 
            where 
            row_delete_date is NULL
            """
    )
    fun selectListForC7N8(
        @Param(value = "num") num: Int,
        pageable: Pageable
    ): Page<SelectListForC7N8OutputVo>

    interface SelectListForC7N8OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var distance: Int
    }


    ////
    @Modifying // Native Query 에서 Delete, Update 문은 이것을 붙여야함
    @Query(
        nativeQuery = true,
        value = """
            UPDATE template.test_data 
            SET 
            content = :content 
            WHERE 
            uid = :uid
            """
    )
    fun updateForC7N10(
        @Param(value = "uid") uid: Long,
        @Param(value = "content") content: String
    )


    ////
    // like 문을 사용할 때, replace 로 검색어와 탐색 정보의 공백을 없애줌으로써 공백에 유연한 검색이 가능
    @Query(
        nativeQuery = true,
        value = """
            select 
            test_data.uid as uid, 
            test_data.row_create_date as rowCreateDate, 
            test_data.row_update_date as rowUpdateDate, 
            test_data.content as content, 
            test_data.random_num as randomNum 
            from template.test_data 
            where 
            replace(content, ' ', '') like replace(concat('%',:searchKeyword,'%'), ' ', '') 
            and row_delete_date is NULL
            """,
        countQuery = """
            select 
            count(*) 
            from template.test_data 
            where 
            replace(content, ' ', '') like replace(concat('%',:searchKeyword,'%'), ' ', '') 
            and row_delete_date is NULL
            """
    )
    fun selectListForC7N11(
        @Param(value = "searchKeyword") searchKeyword: String,
        pageable: Pageable
    ): Page<SelectPageForC7N11OutputVo>

    interface SelectPageForC7N11OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
    }


    ////
    // 1. 먼저 가져올 정보를 인터페이스에 맞게 가져옵니다.
    //     주의할 점은, where 문을 절대 사용하지 않으며, order by 로 순서만 정렬해야 합니다.
    //     이것이 orderedCoreTable 입니다.
    // 2. orderedCoreTable 에 rowNum 을 붙여줍니다.
    //     이것이 rowNumTable 입니다.
    // 3. rowNumTable 에서 where 문을 사용하여, 동일한 쿼리 결과에서 원하는 uid 의 rowNum 을 찾고, 이보다 큰 rowNum 을 추려냅니다.
    // 4. 3번을 했다면, 이제부터 where 문과 limit 을 사용하여 결과를 마음껏 필터링 하면 됩니다.
    @Query(
        nativeQuery = true,
        value = """
            select
            *
            from
            (
                select
                *,
                @rownum \:= @rownum + 1 AS rowNum
                from
                (
                    SELECT 
                    uid as uid, 
                    row_create_date as rowCreateDate, 
                    row_update_date as rowUpdateDate, 
                    content as content, random_num as randomNum,
                    ABS(test_data.random_num-:num) as distance,
                    row_delete_date as rowDeleteDate
                    from
                    template.test_data
                    order by
                    distance asc
                ) as orderedCoreTable,
                (SELECT @rownum \:= 0) as rowNumStart
            ) as rowNumTable
            where
            rowNum > 
            (
            select if
            (
                :lastItemUid > 0,
                (
                    (
                        select rowNumCopy
                        from
                        (
                            select
                            *,
                            @rownumCopy \:= @rownumCopy + 1 AS rowNumCopy 
                            from
                            (
                                SELECT 
                                uid as uid, 
                                row_create_date as rowCreateDate, 
                                row_update_date as rowUpdateDate, 
                                content as content, random_num as randomNum,
                                ABS(test_data.random_num-:num) as distance,
                                row_delete_date as rowDeleteDate
                                from
                                template.test_data
                                order by
                                distance asc
                            ) as orderedCoreTableCopy,
                            (SELECT @rownumCopy \:= 0) as rowNumStartCopy
                        ) as rowNumTableCopy
                        where uid = :lastItemUid
                    )
                ),
                0
            )
            )
            and
            rowActivate is NULL
            limit :pageElementsCount
            """
    )
    fun selectListForC7N14(
        @Param(value = "lastItemUid") lastItemUid: Long,
        @Param(value = "pageElementsCount") pageElementsCount: Int,
        @Param(value = "num") num: Int
    ): List<SelectListForC7N14OutputVo>

    interface SelectListForC7N14OutputVo {
        var uid: Long
        var rowCreateDate: LocalDateTime
        var rowUpdateDate: LocalDateTime
        var content: String
        var randomNum: Int
        var distance: Int
    }


    //------------------------------------------------------------------------------------------------------------------
    // <C9>
    @Query(
        nativeQuery = true,
        value = """
            SELECT 
            *, 
            (
                6371 * acos(
                    cos(radians(latitude)) * 
                    cos(radians(:latitude)) * 
                    cos(radians(:longitude) - 
                    radians(longitude)) + 
                    sin(radians(latitude)) * 
                    sin(radians(:latitude))
                )
            ) as distanceKiloMeter 
            FROM template.test_map 
            HAVING 
            distanceKiloMeter <= :radiusKiloMeter 
            order by 
            distanceKiloMeter
            """
    )
    fun selectListForApiC9N5(
        @Param(value = "latitude") latitude: Double,
        @Param(value = "longitude") longitude: Double,
        @Param(value = "radiusKiloMeter") radiusKiloMeter: Double
    ): List<SelectListForApiC9N5OutputVo>

    interface SelectListForApiC9N5OutputVo {
        var uid: Long
        var latitude: Double
        var longitude: Double
        var distanceKiloMeter: Double
    }

    @Query(
        nativeQuery = true,
        value = """
            SELECT * 
            FROM 
            template.test_map 
            WHERE 
            latitude BETWEEN :southLatitude AND :northLatitude 
            AND 
            (
                (
                    :westLongitude <= :eastLongitude AND 
                    longitude BETWEEN :westLongitude AND :eastLongitude
                )
                OR
                (
                    :westLongitude > :eastLongitude AND 
                    (
                        longitude >= :westLongitude OR 
                        longitude <= :eastLongitude
                    )
                )
            )
            """
    )
    fun selectListForApiC9N6(
        @Param(value = "northLatitude") northLatitude: Double,
        @Param(value = "eastLongitude") eastLongitude: Double,
        @Param(value = "southLatitude") southLatitude: Double,
        @Param(value = "westLongitude") westLongitude: Double
    ): List<SelectListForApiC9N6OutputVo>

    interface SelectListForApiC9N6OutputVo {
        var uid: Long
        var latitude: Double
        var longitude: Double
    }

}