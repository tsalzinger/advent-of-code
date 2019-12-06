package me.salzinger

val idToMass = mutableMapOf<String, Mass>()

data class Mass(
    val id: String
) {
    var parent: Mass? = null
    var children = mutableListOf<Mass>()

    fun addParent(parent: Mass) {
        this.parent = parent
        parent.addChild(this)
    }

    fun isLeaf() = children.isEmpty()
    fun isRoot() = parent == null

    fun getLeafs(level: Int = 0): List<Pair<Mass, Int>> = if (isLeaf()) {
        listOf(this to level)
    } else {
        children.flatMap { it.getLeafs(level + 1) }
    }

    fun getAllChildren(level: Int = 0): List<Pair<Mass, Int>> =
        listOf(
            this to level,
            *children.flatMap { it.getAllChildren(level + 1) }.toTypedArray()
        )

    fun countOrbits(): Int = parent?.countOrbits() ?: 0

    private fun addChild(child: Mass) {
        children.add(child)
    }
}

class OrbitTree(

)


fun List<String>.convertOrbitInput() = map { it.split(')').run { first() to get(1) } }

fun main() {
    11.solve {
        convertOrbitInput()
            .forEach {
                val first = idToMass.computeIfAbsent(it.first, ::Mass)
                val second = idToMass.computeIfAbsent(it.second, ::Mass)

                second.addParent(first)
            }

        val root = idToMass.getValue("COM")

        assert(root.isRoot())

        val sum: Int = root.getAllChildren().map { it.second }.sum()
        sum.toString()
    }
}

fun Int.factorial() = (1..this).sum()