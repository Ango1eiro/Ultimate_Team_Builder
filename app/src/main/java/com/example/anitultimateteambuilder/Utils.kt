package com.example.anitultimateteambuilder

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

fun imageUriToByteArray(context : Context, uri: Uri?) : ByteArray? {
    if (uri == null) {
        return null
    } else {
        val iNs = context.contentResolver.openInputStream(uri)

        val bitmap = BitmapFactory.decodeStream(iNs)
        val blob = ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, blob)
        val byteArray = blob.toByteArray()
        return resizeImage(byteArray)
    }
}

fun byteArrayToImage(byteArray: ByteArray?) : Bitmap? {
    if (byteArray == null) {
        return null
    } else {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}

fun resizeImage(byteArray: ByteArray) : ByteArray {
    var nByteArray = byteArray
    while (nByteArray.size > 500000) {
        val bitmap = BitmapFactory.decodeByteArray(nByteArray, 0, nByteArray.size);
        val resized = Bitmap.createScaledBitmap(bitmap, (bitmap.getWidth()*0.8).toInt(), (bitmap.getHeight()*0.8).toInt(), true);
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
        nByteArray = stream.toByteArray()
    }

    return nByteArray
}

fun getNameForCV(resource: Int,name: String) : String {

    val h_v1 = R.layout.player_item_cv_small_h_v1
    val h_v2 = R.layout.player_item_cv_small_h_v2
    val h_v3 = R.layout.player_item_cv_small_h_v3
    return when(resource){
        h_v1 -> getFirstName(name)
        h_v2 -> getFirstName(name)
        h_v3 -> getFirstName(name)
        else -> name
    }
}

fun getFirstName(name: String) : String {

    var lastName: String
    var firstName: String

    if(name.split("\\s".toRegex()).size>1){
        lastName = name.substring(name.lastIndexOf(" ")+1);
        firstName = name.substring(0, name.lastIndexOf(' '));
    }
    else{
        firstName = name;
    }

    if(firstName.length > 8){
        firstName = firstName.substring(0,8).plus(".")
    }

    return firstName

}

fun View.setOnNotVeryLongClickListener(listener: () -> Unit) {
    setOnTouchListener(object : View.OnTouchListener {

        private val longClickDuration = 500L
        private val handler = Handler()

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event?.action == MotionEvent.ACTION_DOWN) {
                handler.postDelayed({ listener.invoke() }, longClickDuration)
            } else if (event?.action == MotionEvent.ACTION_UP) {
                handler.removeCallbacksAndMessages(null)
            }
            return true
        }
    })
}

fun updateCardBackground(context: Context, rarity: PlayerRarity, cardInnerLayout : ConstraintLayout) {

    when (rarity){
        PlayerRarity.PLAYER_RARITY_MOMENTS -> {
            cardInnerLayout.background = ContextCompat.getDrawable(context, R.drawable.gradient_list_moments)
            setupAnimation(cardInnerLayout.background as AnimationDrawable)
        }
        PlayerRarity.PLAYER_RARITY_LEGEND -> {
            cardInnerLayout.background = ContextCompat.getDrawable(context, R.drawable.gradient_list_legend)
            setupAnimation(cardInnerLayout.background as AnimationDrawable)
        }
        PlayerRarity.PLAYER_RARITY_COMMON -> cardInnerLayout.background = null
    }

}

fun setupAnimation(ad: AnimationDrawable) {
    ad.setEnterFadeDuration(2000)
    ad.setExitFadeDuration(2000)
    ad.start()
}