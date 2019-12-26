package com.example.kotlinpdfprinter

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val file_name:String ="test_pdf.pdf"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object :PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    btn_create_pdf.setOnClickListener{
                        createPDFFile(Common.getAppPath(this@MainActivity)+file_name)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }).check()
    }

    private fun createPDFFile(path: String) {
        if(File(path).exists())
            File(path).delete()
        try {
            val  document = Document()
            //save
            PdfWriter.getInstance(document,FileOutputStream(path))
            //Open to write
            document.open()

            //Setting
            document.pageSize= PageSize.A4
            document.addCreationDate()
            document.addAuthor("Duc Dang")
            document.addCreator("Duc Dang")

            //Font setting
            val  colorAccent= BaseColor(0,153,204,255)
            val headingFontSize= 20.0f
            val valueFontSize= 26.0f

            //Custom font
            val fontName = BaseFont.createFont("asset/fonts/brandon_medium.otf","UFT-8",BaseFont.EMBEDDED)

            val tittleStyle= Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK)
            addNewItem(document,"Order Details",Element.ALIGN_CENTER,tittleStyle)

            val headingStyle =Font(fontName,headingFontSize,Font.NORMAL,colorAccent)
            addNewItem(document,"Order No:",Element.ALIGN_LEFT,headingStyle)

            val valueStyle =Font(fontName,valueFontSize,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document,"#123123",Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Order Date:",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,"26/12/2019",Element.ALIGN_LEFT,valueStyle)

            addLineSeperator(document)
            addNewItem(document,"Accoutn name:",Element.ALIGN_LEFT,headingStyle)
            addNewItem(document,"26/12/2019",Element.ALIGN_LEFT,valueStyle)

            document.close()
            Toast.makeText(this@MainActivity,"SUCCESS",Toast.LENGTH_SHORT).show()
            printPDF()
        }
        catch (e:Exception){

        }
    }

    private fun printPDF() {
        val  printManager= getSystemService(Context.PRINT_SERVICE) as PrintManager
        try{
            val  printAdapter = PdfDocumentAdapter(this@MainActivity,Common.getAppPath(this@MainActivity)+file_name)
            printManager.print("Document",printAdapter,PrintAttributes.Builder().build())
        }catch (e:Exception){
            Log.e("Error",e.message)
        }
    }

    private fun addLineSeperator(document: Document) {
        val  lineSeperator = LineSeparator()
        lineSeperator.lineColor = BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeperator))
        addLineSpace(document)
    }

    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, align: Int, style: Font) {
        val chunk = Chunk(text,style)
        val  p= Paragraph(chunk)
        p.alignment =align
        document.add(p)

    }
}
