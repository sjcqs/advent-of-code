package days


typealias RelationShip = Pair<String, String>

object J006 {
    private const val COM_ID = "COM"
    private val RELATIONSHIP_REGEX = """(\w+)\)(\w+)""".toRegex()

    data class SpaceObject(
        val id: String,
        val orbits: List<SpaceObject>
    ) {
        val allOrbits: List<SpaceObject>
            get() = orbits + orbits.flatMap { spaceObject -> spaceObject.allOrbits }

        val orbitsCount: Int
            get() = orbits.size + orbits.sumBy { spaceObject -> spaceObject.orbitsCount }

        override fun toString(): String {
            return "SpaceObject:\n  id: $id\n  orbitedBy: ${orbits.joinToString { it.id }}"
        }

        fun distanceTo(common: SpaceObject): Int {
            return if (id == common.id) 0 else 1 + orbits.sumBy { it.distanceTo(common) }
        }
    }

    fun orbitsCountChecksum(input: String): Int {
        val relationships = parseRelationships(input)
        val centerOfMass = getOrbitedBy(COM_ID, relationships)
        return totalNumberOfOrbits(centerOfMass)
    }

    fun minimumOrbitalTransfersCount(
        input: String,
        sourceId: String,
        targetId: String,
        useOverkill: Boolean
    ): Int {
        val relationships = parseRelationships(input)

        return if (useOverkill) {
            val centerOfMass = getOrbitedBy(COM_ID, relationships)
            val reachableMap = getReachableMap(centerOfMass)

            val sourceOrbit = reachableMap.keys
                .first { spaceObject -> spaceObject.id == relationships.first { it.second == sourceId }.first }
            val targetOrbit = reachableMap.keys
                .first { spaceObject -> spaceObject.id == relationships.first { it.second == targetId }.first }
            minDistanceDijkstra(reachableMap, sourceOrbit, targetOrbit)
        } else {
            val source = getOrbiting(sourceId, relationships)
            val target = getOrbiting(targetId, relationships)

            val common = source.allOrbits.intersect(target.allOrbits).first()
            source.distanceTo(common) - 1 + target.distanceTo(common) - 1
        }
    }

    private fun parseRelationships(input: String): List<RelationShip> {
        return input.split("\n")
            .mapNotNull { relationship -> RELATIONSHIP_REGEX.matchEntire(relationship)?.destructured }
            .map { (orbitedId, orbitingId) -> orbitedId to orbitingId }
    }

    private fun getReachableMap(
        centerOfMass: SpaceObject
    ): Map<SpaceObject, List<SpaceObject>> {
        return mutableMapOf<SpaceObject, List<SpaceObject>>().apply {
            centerOfMass.orbits.onEach { spaceObject ->
                merge(spaceObject, listOf(centerOfMass)) { prev, new -> prev + new }
                for ((key, value) in getReachableMap(spaceObject)) {
                    merge(key, value) { prev, new -> prev + new }
                }
            }
            put(centerOfMass, centerOfMass.orbits)
        }
    }

    private fun minDistanceDijkstra(
        reachableMap: Map<SpaceObject, List<SpaceObject>>,
        source: SpaceObject,
        target: SpaceObject
    ): Int {
        val distances = mutableMapOf(source to 0)
        val previous = mutableMapOf<SpaceObject, SpaceObject>()
        val unvisited = reachableMap.keys.toMutableSet()

        var current: SpaceObject
        while (unvisited.isNotEmpty()) {
            current = unvisited
                .minBy { spaceObject -> distances.getOrDefault(spaceObject, Int.MAX_VALUE) } ?: continue
            unvisited.remove(current)

            val distanceToCurrent = distances[current]
            reachableMap[current]?.forEach { neighbour ->
                val distance = distanceToCurrent?.let { it + 1 } ?: Int.MAX_VALUE
                if (distance < distances.getOrDefault(neighbour, Int.MAX_VALUE)) {
                    distances[neighbour] = distance
                    previous[neighbour] = current
                }
            }
        }
        return distances.getOrDefault(target, Int.MAX_VALUE)
    }

    private fun totalNumberOfOrbits(obj: SpaceObject): Int {
        val indirectOrbits = obj.orbits.sumBy { orbitingObject -> totalNumberOfOrbits(orbitingObject) }
        return obj.orbitsCount + indirectOrbits
    }

    private fun getOrbiting(spaceObjectId: String, relationships: List<RelationShip>): SpaceObject {
        val map = relationships
            .foldRight(mutableMapOf<String, List<String>>()) { (orbitedId, orbitingId), accumulator ->
                accumulator.apply {
                    merge(orbitingId, listOf(orbitedId)) { currentList, newList -> currentList + newList }
                }
            }
        return SpaceObject(
            id = spaceObjectId,
            orbits = orbits(spaceObjectId, map)
        )
    }

    private fun getOrbitedBy(spaceObjectId: String, relationships: List<RelationShip>): SpaceObject {
        val map = relationships
            .foldRight(mutableMapOf<String, List<String>>()) { (orbitedId, orbitingId), accumulator ->
                accumulator.apply {
                    merge(orbitedId, listOf(orbitingId)) { currentList, newList -> currentList + newList }
                }
            }
        return SpaceObject(
            id = spaceObjectId,
            orbits = orbits(spaceObjectId, map)
        )
    }

    private fun orbits(id: String, map: Map<String, List<String>>): List<SpaceObject> {
        val objects = map.getOrDefault(id, emptyList())

        return objects.map { orbitingId -> SpaceObject(orbitingId, orbits(orbitingId, map)) }
    }
}