package lab6

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.modules.SerializersModule
import lab2.*

object ShapeJSON {
    val module = SerializersModule {
        polymorphic(ColoredShape2d::class) {
            subclass(Circle::class)
            subclass(Rectangle::class)
            subclass(Square::class)
            subclass(Triangle::class)
        }
    }

    private val json = Json {
        serializersModule = module
    }

    fun serialize(shapes: List<ColoredShape2d>): String {
        return json.encodeToString(shapes)
    }

    fun serializeCollector(collector: ShapeCollector): String {
        return serialize(collector.shapes)
    }

    fun deserialize(string: String): List<ColoredShape2d> {
        return json.decodeFromString(string)
    }

    fun deserializeAsCollector(string: String): ShapeCollector {
        return ShapeCollector(deserialize(string))
    }
}