package com.thiwain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "package_receiver")
public class PackageReceiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false)
    private String fname;

    @Column(length = 45, nullable = false)
    private String lname;

    @Column(length = 10)
    private String mobile;

    @Lob
    private String address;

    @ManyToOne
    @JoinColumn(name = "shipments_id", nullable = false)
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(length = 45)
    private String city;

    @ManyToOne
    @JoinColumn(name = "location_matrix_id", nullable = false)
    private LocationMatrix locationMatrix;

    @Column(length = 150)
    private String email;

    public PackageReceiver() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocationMatrix getLocationMatrix() {
        return locationMatrix;
    }

    public void setLocationMatrix(LocationMatrix locationMatrix) {
        this.locationMatrix = locationMatrix;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}