package model;

/* potrebujeme generovat ID*/

public class WrapId{

    public static volatile int id = 0;

    public static int getId() {
        id++;
        return id;
    }
}