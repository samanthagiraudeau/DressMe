
package com.example.dressmeapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Copie l'image pointée par [sourceUri] dans le stockage interne de l'app.
 * Retourne le chemin absolu du fichier sauvegardé.
 *
 * - Compresse en JPEG (qualité configurable) pour économiser de l'espace.
 * - Génère un nom unique.
 */
fun copyImageToInternalStorage(
    context: Context,
    sourceUri: Uri,
    filePrefix: String = "img_",
    jpegQuality: Int = 90
): String {
    val contentResolver = context.contentResolver

    val input: InputStream = contentResolver.openInputStream(sourceUri)
        ?: throw IllegalArgumentException("Impossible d'ouvrir l'URI : $sourceUri")

    // Décode en Bitmap (pratique si on veut compresser/redimensionner)
    val bitmap = BitmapFactory.decodeStream(input)
        ?: throw IllegalStateException("Décodage image échoué pour : $sourceUri")

    // Nom de fichier unique
    val fileName = "${filePrefix}${System.currentTimeMillis()}.jpg"
    val destFile = File(context.filesDir, fileName)

    // Écriture JPEG compressé
    FileOutputStream(destFile).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, jpegQuality, out)
        out.flush()
    }

    return destFile.absolutePath
}

/**
 * Option : si tu préfères une copie binaire brute sans recompression (garde format d'origine).
 */
fun copyImageRawToInternalStorage(context: Context, sourceUri: Uri, filePrefix: String = "img_"): String {
    val contentResolver = context.contentResolver
    val input = contentResolver.openInputStream(sourceUri)
        ?: throw IllegalArgumentException("Impossible d'ouvrir l'URI : $sourceUri")

    val extension = guessExtensionFromMimeType(contentResolver.getType(sourceUri)) ?: "bin"
    val fileName = "${filePrefix}${System.currentTimeMillis()}.$extension"
    val destFile = File(context.filesDir, fileName)

    input.use { inp ->
        FileOutputStream(destFile).use { out ->
            inp.copyTo(out)
            out.flush()
        }
    }
    return destFile.absolutePath
}

private fun guessExtensionFromMimeType(mime: String?): String? = when (mime) {
    "image/jpeg" -> "jpg"
    "image/png"  -> "png"
    "image/webp" -> "webp"
    else -> null
}
