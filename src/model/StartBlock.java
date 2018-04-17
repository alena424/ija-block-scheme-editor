package model;

public class StartBlock extends Block {
    public StartBlock(){
        icon = "img/minus.png";
        selectedIcon = "img/selected/minus.png";
        name = "Start block";
        static_block = true; // nemenny blok
        static_x = 600;
        static_y = 40;
    }
}
