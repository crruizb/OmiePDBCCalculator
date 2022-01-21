# OMIE PDBC Calculator
This project is composed of two parts:
1. Download hourly electrical prices from [OMIE](https://www.omie.es/es/file-access-list?parents%5B0%5D=/&parents%5B1%5D=Mercado%20Diario&parents%5B2%5D=1.%20Precios&dir=Precios%20horarios%20del%20mercado%20diario%20en%20Espa%C3%B1a&realdir=marginalpdbc)
2. Calculate your monthly consumption and price given a set of csv files.

This probably won't calculate your total bill exactly since this only calculates the consumption part of the bill and also there are more things to take into account as taxes or every particular contract. But it is good enough to get an idea.

## Before running
Remove all files from src/consumption if you want to use your files. The data inside is just random, and it is just there in order for you to be able to run the project and see what it does without providing any data.

## How to run
Just add your csv files at src/consumption with the following structure:

`CUPS;date;hour;consumption`

Where:
- CUPS: Is your identifier, not really needed. If you do not want to put it, just put a random string.
- Date: Must be in dd/MM/yyyy format, example: 03/11/2022
- Hour: The hour for the consumption, example: 14
- Consumption: Must be in kWh and using comma as decimal separator, example: 0,534

Full example:

`RandomCUPS;30/07/2021;20;0,053`

You can add just 1 csv file or multiple files, all of them will be red by the calculator.

Finally, just run the Main.kt. If your csv files do not contain a header, remember to set the skipHeader variable to false on the Main file.