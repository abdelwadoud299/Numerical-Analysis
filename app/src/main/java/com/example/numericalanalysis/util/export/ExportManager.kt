package com.example.numericalanalysis.util.export

import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.numericalanalysis.data.model.IterationStep
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.OutputStream

object ExportManager {

    fun exportToPdf(context: Context, fileName: String, title: String, steps: List<IterationStep>): Uri? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.textSize = 18f
        paint.isFakeBoldText = true
        canvas.drawText(title, 50f, 50f, paint)

        paint.textSize = 12f
        var y = 100f
        canvas.drawText("Iter", 50f, y, paint)
        canvas.drawText("xr", 100f, y, paint)
        canvas.drawText("f(xr)", 200f, y, paint)
        canvas.drawText("Error", 300f, y, paint)

        paint.isFakeBoldText = false
        y += 20f

        steps.forEach { step ->
            if (y > 800) return@forEach
            canvas.drawText(step.iteration.toString(), 50f, y, paint)
            canvas.drawText("%.6f".format(step.xr), 100f, y, paint)
            canvas.drawText("%.6f".format(step.fxr), 200f, y, paint)
            canvas.drawText(step.error?.let { "%.6f".format(it) } ?: "---", 300f, y, paint)
            y += 20f
        }

        pdfDocument.finishPage(page)

        return saveToMediaStore(context, "$fileName.pdf", "application/pdf") { outputStream ->
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
        }
    }

    fun exportToExcel(context: Context, fileName: String, steps: List<IterationStep>): Uri? {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Results")
        
        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Iteration")
        header.createCell(1).setCellValue("xr")
        header.createCell(2).setCellValue("f(xr)")
        header.createCell(3).setCellValue("Error")

        steps.forEachIndexed { index, step ->
            val row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(step.iteration.toDouble())
            row.createCell(1).setCellValue(step.xr)
            row.createCell(2).setCellValue(step.fxr)
            row.createCell(3).setCellValue(step.error ?: 0.0)
        }

        return saveToMediaStore(context, "$fileName.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") { outputStream ->
            workbook.write(outputStream)
            workbook.close()
        }
    }

    private fun saveToMediaStore(
        context: Context,
        fileName: String,
        mimeType: String,
        writeBlock: (OutputStream) -> Unit
    ): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
        }

        val resolver = context.contentResolver
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Files.getContentUri("external")
        }

        val uri = resolver.insert(collection, contentValues)

        return try {
            uri?.let {
                resolver.openOutputStream(it)?.use { os ->
                    writeBlock(os)
                }
            }
            uri
        } catch (e: Exception) {
            null
        }
    }
}
