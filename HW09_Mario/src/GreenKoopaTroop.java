public class GreenKoopaTroop extends Enemy {
    
    private static String[] img_files_lt = {"Green_Koopa_Troopa.gif", "Green_Koopa_Troopa.gif",
            "Green_Koopa_Troopa.gif", "Green_Koopa_Troopa2.gif", "Green_Koopa_Troopa2.gif",
            "Green_Koopa_Troopa2.gif"};
    
    public static int INIT_WIDTH = 32;
    public static int INIT_HEIGHT = 48;

    public GreenKoopaTroop(int courtWidth, int courtHeight, int startDistance) {
        super(courtWidth, courtHeight, INIT_WIDTH, INIT_HEIGHT, startDistance, img_files_lt);
    }
    
    @Override
    public int incrementScore(int score) {
        return score += 200;
    }
}
