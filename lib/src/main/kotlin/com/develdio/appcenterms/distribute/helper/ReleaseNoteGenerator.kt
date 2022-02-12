package com.develdio.appcenterms.distribute.helper

object ReleaseNoteGenerator {
    private var buffer: StringBuffer = StringBuffer()
    private var changes: StringBuilder = StringBuilder()
    private const val DEFAULT_WILDCARD_PREFIX = "**"
    private const val START_INDEX = 2
    private const val INITIAL_LINE = 0

    fun fromFile(fileReader: FileReader, afterProcessed: (String) -> Unit) {
        val lines = fileReader.readLinesToList<String>()
        lines.forEachIndexed { numberLine, line ->
            if (line.startsWith(DEFAULT_WILDCARD_PREFIX)) {
                val lineWithoutAsterisk = line.substring(START_INDEX,
                    line.length
                )
                if (numberLine == INITIAL_LINE) {
                    buffer.appendLine(lineWithoutAsterisk)
                } else {
                    changes.append(buffer.toCustomString())
                    buffer.setLength(0)
                    buffer.appendLine(lineWithoutAsterisk)
                    if (isFinalLine(numberLine, lines)) {
                        changes.append(buffer.toCustomString())
                    }
                }
            } else {
                buffer.appendLine(line)
                if (isFinalLine(numberLine, lines)) {
                    changes.append(buffer.toCustomString())
                }
            }
        }
        afterProcessed(changes.toString())
    }

    private fun isFinalLine(numberLine: Int, lines: List<String>): Boolean {
        return (numberLine == (lines.size - 1))
    }

    private fun StringBuffer.appendLine(s: String) {
        append(s + System.getProperty("line.separator"))
    }

    private fun StringBuffer.toCustomString(): String {
        return "\t• ${toString()}"
    }
}
