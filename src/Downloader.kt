import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.ReadableByteChannel
import java.time.LocalDate
import kotlin.math.floor
import kotlin.math.round


class Downloader() {

    private var startDate: LocalDate = LocalDate.now()
    private val endDate: LocalDate = LocalDate.now()

    init {
        val maxFile = File("src/files").listFiles().maxByOrNull {
            it.absolutePath
        }
        if (maxFile != null) {
            val maxFileSplit = maxFile.absolutePath.split("marginalpdbc_")[1]
            val year = maxFileSplit.substring(0, 4)
            val month = maxFileSplit.substring(4, 6)
            val day = maxFileSplit.substring(6, 8)
            startDate = LocalDate.parse("$year-$month-$day").plusDays(1)
        }

        if (startDate.isBefore(endDate)) download()
    }

    private fun download() {
        var startDateTmp = startDate
        while (startDateTmp.isBefore(endDate)) {
            val year = startDateTmp.year
            var month = startDateTmp.monthValue.toString()
            var day = startDateTmp.dayOfMonth.toString()
            if (month.length == 1) {
                month = "0$month"
            }
            if (day.length == 1) day = "0$day"
            val fileName = "marginalpdbc_$year$month$day.1"
            println("Downloading: $fileName...")
            val url = URL("https://www.omie.es/es/file-download?parents%5B0%5D=marginalpdbc&filename=$fileName")
            val readableByteChannel: ReadableByteChannel = Channels.newChannel(url.openStream())

            val fileOutputStream = FileOutputStream("src/files/$fileName.txt")
            fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            startDateTmp = startDateTmp.plusDays(1)
        }
    }
}