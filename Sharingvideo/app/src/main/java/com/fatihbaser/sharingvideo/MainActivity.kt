package com.fatihbaser.sharingvideo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun imageViewMore(view: View?) {

        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val photoURI = FileProvider.getUriForFile(
                    getApplicationContext(), "no.realitylab.arface.fileprovider", File(
                   // videoRecorderJava.videoPath.absolutePath
            )
            )
            intent.putExtra(Intent.EXTRA_STREAM, photoURI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "video/mp4"
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun shareToInstagramStory(backgroundFilePath: String) {
        val background: Uri = grantAccessToFile(backgroundFilePath)
        // Instantiate implicit intent with ADD_TO_STORY action,
        // background asset, sticker asset, and attribution link
        val intent = Intent("com.instagram.share.ADD_TO_STORY")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.setDataAndTypeAndNormalize(background, getMimeType(background))


        // Instantiate activity and verify it will resolve implicit intent
        if (packageManager.resolveActivity(intent, 0) != null) {
            startActivityForResult(intent, 0)
        }
    }
    fun startInstagramFeed(mediaPath: String?) {
        val INSTAGRAM_PACKAGE_NAME = "com.instagram.android"
        try {
            val media = File(mediaPath)
            val uri = Uri.fromFile(media)
            val share = Intent("com.instagram.share.ADD_TO_FEED")
            share.type = "video/*"
            share.putExtra(Intent.EXTRA_STREAM, uri)

            grantUriPermission(
                    "com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            startActivity(share)
        } catch (e: Exception) {

            try {
                startActivity(
                        Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$INSTAGRAM_PACKAGE_NAME")
                        )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                        Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                        "https://play.google.com/store/apps/details?id="
                                                + INSTAGRAM_PACKAGE_NAME
                                )
                        )
                )
            }
        }
    }



    fun getMimeType(uri: Uri): String? {
        var type:String? = null
        var extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        if (extension != null)
        {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type!!
    }
    fun grantAccessToFile(filePath: String):Uri {
        return FileProvider.getUriForFile(
                getApplicationContext(), "no.realitylab.arface.fileprovider", File(
                filePath
        )
        )
    }

}