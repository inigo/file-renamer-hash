package net.surguy.filerenamer

/**
 * Go through a directory and rename each of the files in it based on their hash and a lookup previously
 * recorded by `HashRecorder`.
 *
 * Duplicate files will be copied to all the relevant paths - we assume that if two files have the same hash,
 * then they are identical (hash collisions are very unlikely). The most likely cause for dupes will be empty files.
 */
class HashRetriever {

}
