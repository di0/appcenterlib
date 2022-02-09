package com.develdio.appcenterms.distribute

import com.develdio.appcenterms.distribute.templates.ReleaseNote

class NoteCreator {
    var aboutIt: String = String()
        private set

    var author: String = String()
        private set

    var branchName: String = String()
        private set

    var commitHash: String = String()
        private set

    var reasons = ArrayList<String>()
        private set

    var changes = StringBuilder()
        private set

    var releaseTemplate: String? = null
        private set

    var filePathWithReasonDescription: String? = null
        private set

    fun addAboutIt(aboutIt: String): NoteCreator {
        this.aboutIt = aboutIt
        return this
    }

    fun addReasons(reason: String): NoteCreator {
        reasons.add(reason)
        return this
    }

    fun addReasonWithFile(filename: String): NoteCreator {
        this.filePathWithReasonDescription = filename
        return this
    }

    fun addAuthor(author: String): NoteCreator {
        this.author = author
        return this
    }

    fun addBranchName(branchName: String): NoteCreator {
        this.branchName = branchName
        return this
    }

    fun addCommitHash(commitHash: String): NoteCreator {
        this.commitHash = commitHash
        return this
    }

    fun addReleaseTemplate(releaseTemplate: String): NoteCreator {
        this.releaseTemplate = releaseTemplate
        return this
    }

    fun create(): String {
        return ReleaseNote(this).createTemplate()
    }
}