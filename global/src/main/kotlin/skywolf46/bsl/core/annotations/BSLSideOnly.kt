package skywolf46.bsl.core.annotations

import skywolf46.bsl.core.enums.BSLSide

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class BSLSideOnly(val side: BSLSide)
