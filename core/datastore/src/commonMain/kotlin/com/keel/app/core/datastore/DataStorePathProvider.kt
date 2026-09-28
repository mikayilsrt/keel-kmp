package com.keel.app.core.datastore

/** Resolves the absolute path of a DataStore file. Implemented per platform. */
fun interface DataStorePathProvider {
    fun absolutePath(fileName: String): String
}
