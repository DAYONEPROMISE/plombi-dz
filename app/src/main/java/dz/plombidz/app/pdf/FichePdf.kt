package dz.plombidz.app.pdf

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import dz.plombidz.app.data.Intervention
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Offline intervention-sheet PDF built with the framework PdfDocument. */
object FichePdf {

    fun build(context: Context, job: Intervention): File {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).build() // A4 points
        val page = doc.startPage(pageInfo)
        val c = page.canvas
        val title = Paint().apply { textSize = 22f; isFakeBoldText = true }
        val head = Paint().apply { textSize = 13f; isFakeBoldText = true }
        val body = Paint().apply { textSize = 12f }
        var y = 70f
        fun line(p: Paint, s: String, dy: Float = 22f) {
            y += dy
            c.drawText(s.take(72), 48f, y, p)
        }
        line(title, "PlombiDZ — Fiche d'intervention", 0f)
        line(body, "Client : ${job.clientName.ifBlank { "—" }}")
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Date(job.dateEpoch))
        line(body, "Date : $date")
        line(body, "Statut : ${if (job.done) "Terminee" else "En cours"}")
        y += 10f
        line(head, "Travail realise :")
        job.description.chunked(70).take(8).forEach { line(body, it, 18f) }
        y += 6f
        line(head, "Materiel / pieces :")
        job.materials.chunked(70).take(6).forEach { line(body, it, 18f) }
        y += 6f
        line(head, "Montant : ${job.priceDa} DA")
        doc.finishPage(page)
        val out = File(context.cacheDir, "fiche-${job.id}-${System.currentTimeMillis()}.pdf")
        out.outputStream().use { doc.writeTo(it) }
        doc.close()
        return out
    }
}
