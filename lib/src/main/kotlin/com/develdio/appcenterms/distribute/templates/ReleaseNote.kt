package com.develdio.appcenterms.distribute.templates

import com.develdio.appcenterms.distribute.NoteCreator
import com.develdio.appcenterms.distribute.helper.FileReader
import com.develdio.appcenterms.distribute.helper.ReleaseNoteGenerator

class ReleaseNote(private val note: NoteCreator) {
    fun createTemplate(): String {
        ReleaseNoteGenerator.fromFile(FileReader(note.filePathWithReasonDescription!!)) {
            note.changes.appendln(it)
        }

        note.reasons.forEach { reason ->
            note.changes.appendln("\t• $reason")
        }

        val finalTemplate = note.releaseTemplate ?: Internal.CUSTOM_RELEASE_TEMPLATE

        return finalTemplate.format(note.aboutIt, note.changes.toString(),
            note.author, note.branchName, note.commitHash
        )
    }

    private companion object Internal { /* TODO i18n */
        const val CUSTOM_RELEASE_TEMPLATE = """
# Build Purpose:

%s

# Changes:

%s

**Developer/Author**: *%s*

**branch**: *%s*

**commit**: %s
"""
    }
}
