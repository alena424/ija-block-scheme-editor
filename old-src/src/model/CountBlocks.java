package model;

public class CountBlocks extends Block {

    public CountBlocks(TypePort[] inputPorts, TypePort[] outputPorts) {
        super(inputPorts, outputPorts);
    }

    @Override
    public void functions() {
        //pocita s tim, ze dostane spravne hodnoty
        Double sums = 0.0;
        for ( int i = 0; i < this.inputPorts.length; i++ ){
            // secteme hodnoty
            sums += this.inputPorts[i].value;
        }
        outputPorts[0].value = sums;
    }
}
