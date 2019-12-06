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

    fun countOrbits(): Int = if (parent != null) {
        parent!!.countOrbits() + 1
    } else {
        0
    }

    fun getParents(): List<Mass> = if (parent == null) {
        emptyList()
    } else {
        listOf(parent!!, *parent!!.getParents().toTypedArray())
    }

    private fun addChild(child: Mass) {
        children.add(child)
    }
}

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

    12.solve {
        convertOrbitInput()
            .forEach {
                val first = idToMass.computeIfAbsent(it.first, ::Mass)
                val second = idToMass.computeIfAbsent(it.second, ::Mass)

                second.addParent(first)
            }

        val santa = idToMass.getValue("SAN")
        println("SAN ${santa.getParents()} ${santa.countOrbits()}")
        val you = idToMass.getValue("YOU")
        println("YOU ${you.getParents()} ${you.countOrbits()}")

        val junction = santa.findCommonParent(you)!!
        println("junction ${junction.getParents()} ${junction.countOrbits()}")

        val jumps = santa.countOrbits() - junction.countOrbits()  + you.countOrbits() - junction.countOrbits() - 2
        jumps.toString()
    }
}

fun Int.factorial() = (1..this).sum()

fun Mass.findCommonParent(other: Mass): Mass? {
    val thisParents = getParents()
    val otherParents = other.getParents()

    return thisParents.find(otherParents::contains)
}