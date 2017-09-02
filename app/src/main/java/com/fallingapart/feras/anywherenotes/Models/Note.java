package com.fallingapart.feras.anywherenotes.Models;

import com.orm.SugarRecord;

import java.util.Date;

public class Note extends SugarRecord {

    public String Name;
    public String Description;
    public Date UpdatedAt;
    public int ColorId;

    public Note() {
        super();
    }

    public Note(String name, String description) {
        super();
        Name = name;
        Description = description;
    }
}
