package com.example.a3dprintertracker;

public class Printer {
    private String Name, Serial, Manufacturer, amount_Paid, date_Purchased;

    //initializes the object with parameters
    public Printer(String name, String serial, String manufacturer, String amount_Paid, String date_Purchased) {
        this.Name = name;
        this.Serial = serial;
        this.Manufacturer = manufacturer;
        this.amount_Paid = amount_Paid;
        this.date_Purchased = date_Purchased;
    }

    //Functions for retrieving different printer parameters
    public String getName() {
        return Name;
    }

    public String getSerial() {
        return Serial;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public String getAmount_Paid() {
        return amount_Paid;
    }

    public String getDate_Purchased() {
        return date_Purchased;
    }
}
