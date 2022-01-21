fun main() {
    Downloader()
    val calculator = Calculator(skipHeader = true)
    calculator.calcConsumptionByMonths()
    calculator.calcPriceByMonths()
}