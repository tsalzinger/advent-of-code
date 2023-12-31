import org.assertj.core.api.AbstractBooleanAssert
import org.assertj.core.api.Assertions
import org.assertj.core.api.IterableAssert
import org.assertj.core.api.ObjectAssert

inline fun <T> T.assertThat(block: ObjectAssert<T>.() -> Unit): T {
    Assertions.assertThat(this).run(block)
    return this
}

inline fun <T : Iterable<S>, S> T.assertIterable(block: IterableAssert<S>.() -> Unit): T {
    Assertions.assertThat(this).run(block)
    return this
}

inline fun <T : Sequence<S>, S> T.assertIterable(block: IterableAssert<S>.() -> Unit): T {
    Assertions.assertThat(this.asIterable()).run(block)
    return this
}

inline fun Boolean.assertBoolean(block: AbstractBooleanAssert<*>.() -> Unit): Boolean {
    Assertions.assertThat(this).run(block)
    return this
}
