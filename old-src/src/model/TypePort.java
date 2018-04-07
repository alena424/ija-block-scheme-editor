package model;

public class TypePort {
    protected String name; // nazev typu
    protected Double value = 0.0; // hodnota typy
    public TypePort(String name, Double value){
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Double getValue() {
        return this.value;
    }
}
