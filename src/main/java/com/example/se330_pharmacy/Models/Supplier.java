package com.example.se330_pharmacy.Models;

public class Suppier {
    int partner_id; // doi tac
    String partnername;
    String address;
    String phonenumber;
    String email;

    public Suppier() {
    }

    public Suppier(int partner_id, String partnername, String address, String phonenumber, String email) {
        this.partner_id = partner_id;
        this.partnername = partnername;
        this.address = address;
        this.phonenumber = phonenumber;
        this.email = email;
    }
}
