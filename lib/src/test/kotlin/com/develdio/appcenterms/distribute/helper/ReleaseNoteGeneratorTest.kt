package com.develdio.appcenterms.distribute.helper

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ReleaseNoteGeneratorTest {
    @MockK
    private lateinit var fileReader: FileReader

    @Test
    fun `should split text according defined wildcard pattern`() {
        every { fileReader.readLinesToList<String>() } returns linesRead()

        var currentOutputText = String()
        ReleaseNoteGenerator.fromFile(fileReader) { resultAfterRead ->
            currentOutputText = resultAfterRead
        }

        Assertions.assertEquals(expectedOutputText(), currentOutputText)
    }

    private fun linesRead(): List<String> {
        return mutableListOf<String>(
            "**A just first Test. First line.",
            "**A just second Test. Second line."
        )
    }

    private fun expectedOutputText(): String {
        return "\t• A just first Test. First line.\n\t• A just second Test. Second line.\n"
    }
}
