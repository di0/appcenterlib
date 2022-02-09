package com.develdio.appcenterms.distribute.helper

import java.io.File

/**
 * A file entity encapsulated easy to test.
 */
class FileReader(val fileName: String) {
    fun read(): File = File(fileName)

    fun <T : Any> readLinesToList(): List<T> {
        return this.read().useLines { it.toList() as List<T> }
    }
}