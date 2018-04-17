package model;


public class EndBLock extends Block {
    public EndBLock(){
        icon = "img/equals.png";
        selectedIcon = "img/selected/equals.png";
        name = "End block";
        static_block = true; // nemenny blok
        static_x = 600;
        static_y = 576;
    }
}
