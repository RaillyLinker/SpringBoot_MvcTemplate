package raillylinker.module_idp_common.aop_aspects

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import raillylinker.module_idp_common.data_sources.ProjectConst

// [모듈간 AOP 테스트용 어노테이션 AOP]
@Component
@Aspect
class TestAnnotationAspect {
    companion object {
        // Annotation 경로
        const val TEST_ANNOTATION_PATH =
            "@annotation(${ProjectConst.PACKAGE_NAME}.annotations.TestAnnotation)"
    }

    @Around(TEST_ANNOTATION_PATH)
    fun aroundTestAnnotationFunction(joinPoint: ProceedingJoinPoint): Any? {
        println("test activate")

        val proceed = joinPoint.proceed()

        return proceed // 결과 리턴
    }
}