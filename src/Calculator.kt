import java.io.File

class Calculator(private val skipHeader: Boolean) {

    private val prices : LinkedHashMap<String,Double>
    private val consumptions : LinkedHashMap<String,Double>

    init {
        prices = pricesToMap()
        consumptions = consumptionToMap()
    }

    private fun pricesToMap(): LinkedHashMap<String, Double> {
        val priceByDates = LinkedHashMap<String, Double>()
        val files = File("src/files")
        files.listFiles().map { file ->
            file.useLines { it.toList() }
                .map {
                    if (it != "MARGINALPDBC;" && it != "*") {
                        val line = it.split(";")
                        val date = "${line[0]}-${line[1]}-${line[2]} ${line[3]}"
                        val value = line[4].toDouble() / 1000 // Price is in €/MWh, convert to €/kWh
                        priceByDates[date] = value
                    }
                }
        }
        return priceByDates
    }

    private fun consumptionToMap(): LinkedHashMap<String, Double> {
        val consumptionByDates = LinkedHashMap<String, Double>()
        val files = File("src/consumption")
        files.listFiles().map { file ->
            file.useLines {
                val lines = it.toMutableList()
                if (skipHeader) lines.removeAt(0)
                lines
            }.map {
                val line = it.split(";")
                val dateSplit = line[1].split("/")
                val hour = line[2]
                val date = "${dateSplit[2]}-${dateSplit[1]}-${dateSplit[0]} $hour"
                val value = line[3].replace(",", ".").toDouble()
                consumptionByDates[date] = value
            }
        }
        return consumptionByDates
    }

    fun calcConsumptionByMonths() {
        val calc = LinkedHashMap<String, Double>()
        consumptions.map {
            val date = it.key.split(" ")[0].split("-")
            val calcDate = "${date[0]}-${date[1]}-01"

            if (!calc.containsKey(calcDate))
                calc[calcDate] = 0.0

            if (consumptions.containsKey(it.key))
                calc[calcDate] = calc[calcDate]!! + consumptions[it.key]!!
        }

        println("Consumption by months")
        var total = 0.0
        calc.map {
            val valueRounded = "%.2f".format(it.value)
            println("${it.key}: $valueRounded kWh")
            total += it.value
        }
        val totalRounded = "%.2f".format(total)
        println("Total: $totalRounded kWh")
    }

    fun calcPriceByMonths() {
        val calc = LinkedHashMap<String, Double>()
        prices.map {
            val date = it.key.split(" ")[0].split("-")
            val calcDate = "${date[0]}-${date[1]}-01"

            if (!calc.containsKey(calcDate))
                calc[calcDate] = 0.0

            if (consumptions.containsKey(it.key))
                calc[calcDate] = calc[calcDate]!! + consumptions[it.key]!! * it.value
        }
        println("Price * Monthly consumption")
        var total = 0.0
        calc.map {
            val valueRounded = "%.2f".format(it.value)
            if (valueRounded != "0,00") println("${it.key}: $valueRounded €")
            total += it.value
        }
        val totalRounded = "%.2f".format(total)
        println("Total: $totalRounded €")
    }

}