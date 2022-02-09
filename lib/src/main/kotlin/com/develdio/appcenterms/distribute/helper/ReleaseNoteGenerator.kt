package com.develdio.appcenterms.distribute.helper

object ReleaseNoteGenerator {
    private var sb: StringBuffer = StringBuffer()
    private const val DEFAULT_WILDCARD_PREFIX = "**"
    private const val START_INDEX = 2
    private const val INITIAL_LINE = 0

    fun fromFile(fileReader: FileReader, changes: StringBuilder) {
        val lines = fileReader.readLinesToList<String>()
        lines.forEachIndexed { numberLine, line ->
            if (line.startsWith(DEFAULT_WILDCARD_PREFIX)) {
                val lineWithoutAsterisk = line.substring(START_INDEX,
                    line.length
                )
                if (numberLine == INITIAL_LINE) {
                    sb.appendLine(lineWithoutAsterisk)
                } else {
                    changes.append(sb.toCustomString())
                    sb.setLength(0)
                    sb.appendLine(lineWithoutAsterisk)
                    if (isFinalLine(numberLine, lines)) {
                        changes.append(sb.toCustomString())
                    }
                }
            } else {
                sb.appendLine(line)
                if (isFinalLine(numberLine, lines)) {
                    changes.append(sb.toCustomString())
                }
            }
        }
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