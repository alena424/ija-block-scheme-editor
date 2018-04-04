package model;

/*
Trida zapouzdruje vsechny informace o bloku
 */
public class Block {
    protected TypePort[] inputPorts;
    protected TypePort[] outputPorts;
    protected Integer level;
    protected Integer id;
    protected Integer countInput = 0; // pocita kolik portu vstupnich plnych
    protected Integer countOutput = 0; // pocita koik portu vystupnich plnych

    public Block(TypePort[] inputPorts, TypePort[] outputPorts ){
        if ( inputPorts == null ){
            this.inputPorts = null;
        } else {
            this.inputPorts = new TypePort[inputPorts.length];
            System.out.println("Delka input: " + inputPorts.length);
            System.arraycopy( inputPorts, 0, this.inputPorts, 0, inputPorts.length );
        }

        if ( outputPorts == null ){
            this.outputPorts = null;
        } else {
            this.outputPorts = new TypePort[outputPorts.length];
            System.out.println("Delka output: " + outputPorts.length);
            System.arraycopy( outputPorts, 0, this.outputPorts, 0, outputPorts.length );
        }


    }
    public void functions(){

    }

}


