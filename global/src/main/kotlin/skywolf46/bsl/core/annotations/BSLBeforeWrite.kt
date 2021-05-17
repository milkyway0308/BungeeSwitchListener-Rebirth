package skywolf46.bsl.core.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class BSLBeforeWrite(val priority: Int = 0)
