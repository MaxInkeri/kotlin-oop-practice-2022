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

    fun serialize(collector: ShapeCollector): String {
        return json.encodeToString(collector.shapes)
    }

    fun deserialize(string: String): ShapeCollector {
        val collector = ShapeCollector()
        val shapes = json.decodeFromString<List<ColoredShape2d>>(string)
        collector.add(shapes)
        return collector
    }
}