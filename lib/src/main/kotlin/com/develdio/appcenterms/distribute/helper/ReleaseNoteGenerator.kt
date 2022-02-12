package com.develdio.appcenterms.distribute.helper

object ReleaseNoteGenerator {
    private var buffer: StringBuffer = StringBuffer()
    private val contentRead: StringBuilder = StringBuilder()
    private const val DEFAULT_WILDCARD_PREFIX = "**"
    private const val START_INDEX = 2
    private const val INITIAL_LINE = 0

    fun fromFile(fileReader: FileReader, afterProcessed: (String) -> Unit) {
        val lines = fileReader.readLinesToList<String>()
        lines.forEachIndexed { currentLine, line ->
            if (line.startsWith(DEFAULT_WILDCARD_PREFIX)) {
                val lineWithoutWildCard = line.substring(START_INDEX,
                    line.length
                )
                if (currentLine == INITIAL_LINE) {
                    buffer.appendLine(lineWithoutWildCard)
                } else {
                    contentRead.append(buffer.toCustomString())
                    buffer.setLength(0)
                    buffer.appendLine(lineWithoutWildCard)
                    if (isFinalLine(currentLine, lines)) {
                        contentRead.append(buffer.toCustomString())
                    }
                }
            } else {
                buffer.appendLine(line)
                if (isFinalLine(currentLine, lines)) {
                    contentRead.append(buffer.toCustomString())
                }
            }
        }
        afterProcessed(contentRead.toString())
    }

    private fun isFinalLine(currentLine: Int, lines: List<String>): Boolean {
        return (currentLine == (lines.size - 1))
    }

    private fun StringBuffer.appendLine(s: String) {
        append(s + System.getProperty("line.separator"))
    }

    private fun StringBuffer.toCustomString(): String {
        return "\t• ${toString()}"
    }
}
