package com.example.se330_pharmacy.Models;

import java.sql.Date;

public class Receipt {
    int receipt_id;
    int employee_id;
    int personcharge_id;
    int payslip_id;
    String tenNhanVien;
    String tenNguoiTra;

    String content;
    Date createDate;
    Double totalPay;
    String note;
    String status;

    public Receipt() {
    }

    public Receipt(int receipt_id, int employee_id,int personcharge_id, String content, Date createDate, Double totalPay, String note, String status,int payslip_id) {
        this.receipt_id = receipt_id;
        this.employee_id = employee_id;
        this.content = content;
        this.createDate = createDate;
        this.totalPay = totalPay;
        this.note = note;
        this.status = status;
        this.personcharge_id=personcharge_id;
        this.payslip_id = payslip_id;
    }

    public int getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(int receipt_id) {
        this.receipt_id = receipt_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(Double totalPay) {
        this.totalPay = totalPay;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getPersoncharge_id() {
        return personcharge_id;
    }

    public void setPersoncharge_id(int personcharge_id) {
        this.personcharge_id = personcharge_id;
    }
    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getTenNguoiTra() {
        return tenNguoiTra;
    }

    public void setTenNguoiTra(String tenNguoiTra) {
        this.tenNguoiTra = tenNguoiTra;
    }
    public int getPayslip_id() {
        return payslip_id;
    }

    public void setPayslip_id(int payslip_id) {
        this.payslip_id = payslip_id;
    }

}
