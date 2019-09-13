import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MineClicker extends Application {

    public static Scene mainScene;
    public static Scene upgradeScene;
    static int blocksMined;
    static String blocksMinedS;
    static int bps;
    static ImageView[] images = new ImageView[5];

    static Upgrade stonePick = new Upgrade(20,1);

    static int clickStrength = 1;

    static Timeline bpsTimeline;

    public static void main(String[] args) { launch(args);}

    @Override
    public void start (Stage primaryStage) {


        InitGraphics();

        SetupMainWindow();
        SetupUpgradeWindow();


        //stage avec le bouton a cliquer
        primaryStage.setScene(mainScene);

        primaryStage.setTitle("Minecraft Clicker : Also play Terraria!");
        primaryStage.setResizable(false);

        primaryStage.setWidth(400);
        primaryStage.setHeight(400);

        primaryStage.show();

        //stage avec les upgrades
        Stage upgradeStage = new Stage();

        upgradeStage.setScene(upgradeScene);

        upgradeStage.show();

    }

    void SetupMainWindow() {

        Button click = new Button("Mine!",images[0]);
        Label blocksMined = new Label("Blocks mined : ");
        Label scoreDisplay = new Label("0");

        click.setScaleX(8);
        click.setScaleY(8);
        click.setTranslateX(100);
        click.setTranslateY(150);

        click.setOnAction( (event) ->Click());

        //timeline qui update le texte scoreDisplay
        Timeline textTimeline = new Timeline(new KeyFrame(Duration.millis(10),t -> scoreDisplay.setText(blocksMinedS)));
        textTimeline.setCycleCount(Animation.INDEFINITE);
        textTimeline.play();

        //timeline qui update les clics par seconde des upgrades
        bpsTimeline = new Timeline(new KeyFrame(Duration.millis(0),t -> addBlock()));
        bpsTimeline.setCycleCount(Animation.INDEFINITE);
        bpsTimeline.play();

        blocksMined.setTranslateX(100);
        blocksMined.setTranslateY(50);

        scoreDisplay.setTranslateX(180);
        scoreDisplay.setTranslateY(50);


        mainScene = new Scene(new Group(click,blocksMined,scoreDisplay));

    }

    void SetupUpgradeWindow () {

        Button buyStonePickaxe = new Button("Buy Stone Pickaxe", images[1]);
        Button buyIronPickaxe = new Button("Buy Iron Pickaxe",images[2]);
        Button buyGoldPickaxe = new Button("Buy Gold Pickaxe",images[3]);
        Button buyDiamondPickaxe = new Button("Buy Diamond Pickaxe",images[4]);

        Label stoneQt = new Label("Cost : " + stonePick.getCost() + "You Have " + stonePick.getQt() + ".");
        Label ironQt = new Label("You Have " + stonePick.getQt() + ".");
        Label goldQt = new Label("You Have " + stonePick.getQt() + ".");
        Label diamondQt = new Label("You Have " + stonePick.getQt() + ".");

        buyStonePickaxe.setTranslateY(0);
        buyIronPickaxe.setTranslateY(25);
        buyGoldPickaxe.setTranslateY(50);
        buyDiamondPickaxe.setTranslateY(75);


        //listeners pour les upgrades
        buyStonePickaxe.setOnAction((event) -> {

            if (blocksMined >= stonePick.getCost()) {


                blocksMined -= stonePick.getCost();
                blocksMinedS =  "" + blocksMined;
                stonePick.setQt(stonePick.getQt()+ 1);
                bps++;
                stoneQt.setText("Cost : " + stonePick.getCost() + "You Have " + stonePick.getQt() + ".");
                updateCountSpeed();

            }

        });


        upgradeScene = new Scene(new Group(buyStonePickaxe,buyIronPickaxe,buyGoldPickaxe,buyDiamondPickaxe,stoneQt));


    }

    void InitGraphics () {

        for (int i = 0; i < 5; i++){

            try {
                images[i] = new ImageView(new Image(new FileInputStream("Images/" + i + ".png")));
            } catch (FileNotFoundException ex) {
                System.out.println( i + " PICK NOT FOUND");
            }

        }

    }

    public static void Click () {

        blocksMined += clickStrength;
        blocksMinedS =  "" + blocksMined;

    }

    void addBlock() {

        blocksMined++;
        blocksMinedS = "" + blocksMined;

    }

    void updateCountSpeed () {

        double bpsTime = (1000.0*(1.0/bps));
        bpsTimeline.stop();
        bpsTimeline = new Timeline(new KeyFrame(Duration.millis(bpsTime),t -> addBlock()));
        bpsTimeline.setCycleCount(Animation.INDEFINITE);
        bpsTimeline.play();

    }

}
