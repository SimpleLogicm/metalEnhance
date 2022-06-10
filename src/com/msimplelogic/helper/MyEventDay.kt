package com.msimplelogic.helper

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.widget.Toast
import com.applandeo.materialcalendarview.EventDay
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


@SuppressLint("ParcelCreator")
internal class MyEventDay : EventDay, Parcelable {
    var note: String
        private set
    var imageResource = 0

    constructor(day: Calendar?, imageResource: Int, note: String) : super(day, imageResource) {
        var imageResource = imageResource
        this.note = note
        imageResource = imageResource
    }

    private constructor(`in`: Parcel) : super(`in`.readSerializable() as Calendar, `in`.readInt()) {
        note = `in`.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeSerializable(calendar)
        parcel.writeInt(imageResource)
        parcel.writeString(note)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Creator<MyEventDay?> = object : Creator<MyEventDay?> {
            override fun createFromParcel(`in`: Parcel): MyEventDay? {
                return MyEventDay(`in`)
            }

            override fun newArray(size: Int): Array<MyEventDay?> {
                return arrayOfNulls(size)
            }
        }
    }


}