import org.assertj.core.api.ObjectAssert

inline fun <T> T.assertThat(block: ObjectAssert<T>.() -> Unit): T {
//    Assertions.assertThat(this).run {  }
    return this
}
