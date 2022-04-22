package logging

import org.apache.logging.log4j.LogManager

private val LOG = LogManager.getLogger(LoggingDemo::class.java)

class LoggingDemo {
    fun someOperation() {
        LOG.info("log info")
    }

    fun someOperation2() {
        LOG.warn("log warning")

        try {
            throw IllegalArgumentException("asdf")
        }catch (e: Exception) {
            LOG.error("Some error", e)
        }
    }
}

fun main() {
    LoggingDemo().someOperation()
    LoggingDemo().someOperation2()
}