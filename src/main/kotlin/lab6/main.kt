package lab6

import lab2.*
import kotlin.random.Random

fun main() {
    val file = "shapes.json"
    val collector = ShapeFiles.readAsCollector(file)
    println(collector.shapes)
    collector.add(Circle(Random.nextDouble(5.0, 10.0)))
    collector.add(Triangle(Random.nextDouble(5.0, 10.0), Random.nextDouble(5.0, 10.0), Random.nextDouble(1.0, 3.0)))
    ShapeFiles.writeCollector(file, collector)
}