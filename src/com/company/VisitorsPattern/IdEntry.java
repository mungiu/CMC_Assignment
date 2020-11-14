package com.company.VisitorsPattern;

import com.company.ast.Command;

public class IdEntry {
    public int level;
    public String id;
    public Command attr;


    public IdEntry(int level, String id, Command attr) {
        this.level = level;
        this.id = id;
        this.attr = attr;
    }


    public String toString() {
        return level + "," + id;
    }
}
