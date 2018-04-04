package model;

public class Main {
    public static void main(String[] args) {

        // pridam na obrazovku
        DisplayModel canvas = new DisplayModel();

        // vytvorime jeden blok
        TypePort[] scitani_in = new TypePort[2]; //2 vstupy
        TypePort[] scitani_out = new TypePort[1]; //1 vystup
        Double value1 = 30.0;
        Double value2 = 50.0;


        for ( int i = 0; i < 2; i ++ ){
            scitani_in[i] = new TypePort("honza", value1);
        }

        scitani_out[0] = new TypePort("honza", 0.0);
        System.out.println("Jdeme scitat");
        CountBlocks scitani = new CountBlocks(scitani_in, scitani_out);

        CountBlocks scitani2 = new CountBlocks(scitani_in, scitani_out);

        CountBlocks scitani3 = new CountBlocks(scitani_in, scitani_out);


        canvas.displayBlock(scitani);

        canvas.displayBlock(scitani2);

        canvas.displayBlock(scitani3);

        // spojime ho s pocatkem a konec
        canvas.connect(canvas.startBlock, scitani);
        System.out.println(canvas.connectionChildren);
        canvas.connect(canvas.startBlock, scitani2);
        System.out.println(canvas.connectionChildren);
        canvas.connect(scitani2, scitani3);
        canvas.connect(scitani, scitani3);
        //canvas.connect( scitani, scitani2 );
        //canvas.connect( scitani, scitani3 );
        //System.out.println(canvas.connectionChildren);

        //canvas.connect(scitani, canvas.endBlock);
        //canvas.connect(scitani2, canvas.endBlock);
        canvas.connect(scitani3, canvas.endBlock);
        //connect(scitani3, canvas.endBlock);
        System.out.println(canvas.connectionChildren);

        //System.out.println("Bloku je celkem: " + canvas.numberOfBlock );
        //System.out.println("Vypis vsechny deti bloku scitani: ");

        canvas.getAllChildren(scitani.id);
        //System.out.println("Vypis vsechny rodice bloku scitani: ");
        //canvas.getAllParents(scitani);*/


    }
}
