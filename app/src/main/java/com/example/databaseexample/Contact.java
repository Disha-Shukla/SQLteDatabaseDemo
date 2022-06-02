package com.example.databaseexample;

public class Contact {

    int _id;
    String _name;
    String _phone_number;
    String email;
    String address;
    byte[] Image;

    public Contact(){   }

    public Contact(int id, String name, String _phone_number, String email, String address){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this.email = email;
        this.address = address;
    }

    public Contact(int id, String name, String _phone_number, String email, String address, byte[] image){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this.email = email;
        this.address = address;
        this.Image = image;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public Contact(String name, String _phone_number, String email, String address, byte[] Image){
        this._name = name;
        this._phone_number = _phone_number;
        this.email = email;
        this.address = address;
        this.Image = Image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Contact(String name, String _phone_number, String email, String address){
        this._name = name;
        this._phone_number = _phone_number;
        this.email = email;
        this.address = address;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_phone_number() {
        return _phone_number;
    }

    public void set_phone_number(String _phone_number) {
        this._phone_number = _phone_number;
    }
}
