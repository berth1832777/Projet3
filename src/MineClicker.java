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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MineClicker extends Application {

    public static Scene mainScene;
    public static Scene upgradeScene;
    static int blocksMined;
    static String blocksMinedS = "Blocks mined: 0";
    static int bps;
    static String bpsS = "0";
    static Label bpsLabel; // je dois metttre ce label la ici parce que j'ai besoin de l'accéder en dehors de la méthode SetupMainWindow.
    static ImageView[] images = new ImageView[5];

    static Upgrade[] upgrades = {new Upgrade(20,1), new Upgrade(500,5),new Upgrade(10000,20), new Upgrade(100000,100)};
    //stone - iron - gold - diamond


    static int clickStrength = 1;

    static Timeline bpsTimeline = new Timeline();

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
        try{ primaryStage.getIcons().add(new Image(new FileInputStream("Images/dirtIcon.png"))); }
        catch (FileNotFoundException ex) { System.out.println("Dirt icon not found!"); }

        primaryStage.show();



        //stage avec les upgrades
        Stage upgradeStage = new Stage();
        upgradeStage.setTitle("Villageois mineur");
        upgradeStage.setResizable(false);
        try{ upgradeStage.getIcons().add(new Image(new FileInputStream("Images/villagerIcon.png"))); }
        catch (FileNotFoundException ex) { System.out.println("Villager icon not found!"); }

        upgradeStage.setScene(upgradeScene);

        upgradeStage.show();

    }

    void SetupMainWindow() {

        Button click = new Button("Mine!",images[0]);
        Label scoreDisplay = new Label("Blocks mined : 0");
        bpsLabel = new Label("Blocks per second : " + bpsS);

        click.setScaleX(8);
        click.setScaleY(8);
        click.setTranslateX(240);
        click.setTranslateY(150);

        click.setOnAction( (event) ->addBlocks(clickStrength));

        //timeline qui update le texte scoreDisplay
        Timeline textTimeline = new Timeline(new KeyFrame(Duration.millis(10),t -> scoreDisplay.setText(blocksMinedS)));
        textTimeline.setCycleCount(Animation.INDEFINITE);
        textTimeline.play();

        //start la timeline qui update le nombre de blocs
        updateCountSpeed();

        scoreDisplay.setTranslateX(100);
        //scoreDisplay.setTranslateY(15);
        scoreDisplay.setFont(new Font(40));



        mainScene = new Scene(new Group(click,scoreDisplay,bpsLabel));

    }

    void SetupUpgradeWindow () {

        //buttons
        Button buyStonePickaxe = new Button("Buy Stone Pickaxe", images[1]);
        Button buyIronPickaxe = new Button("Buy Iron Pickaxe",images[2]);
        Button buyGoldPickaxe = new Button("Buy Gold Pickaxe",images[3]);
        Button buyDiamondPickaxe = new Button("Buy Diamond Pickaxe",images[4]);

        buyStonePickaxe.setTranslateY(0);
        buyIronPickaxe.setTranslateY(25);
        buyGoldPickaxe.setTranslateY(50);
        buyDiamondPickaxe.setTranslateY(75);


        //labels
        Label stoneQt = new Label("Cost : " + upgrades[0].getCost() + "You Have " + upgrades[0].getQt() + ".");
        Label ironQt = new Label("Cost : " + upgrades[1].getCost() + "You Have " + upgrades[1].getQt() + ".");
        Label goldQt = new Label("Cost : " + upgrades[2].getCost() + "You Have " + upgrades[2].getQt() + ".");
        Label diamondQt = new Label("Cost : " + upgrades[3].getCost() + "You Have " + upgrades[3].getQt() + ".");

        stoneQt.setTranslateX(160);
        stoneQt.setTranslateY(5);
        ironQt.setTranslateX(160);
        ironQt.setTranslateY(30);
        goldQt.setTranslateX(160);
        goldQt.setTranslateY(55);
        diamondQt.setTranslateX(160);
        diamondQt.setTranslateY(80);


        //listeners pour les upgrades
        buyStonePickaxe.setOnAction((event) -> buyUpgrade(upgrades[0],stoneQt));
        buyIronPickaxe.setOnAction((event) -> buyUpgrade(upgrades[1],ironQt));
        buyGoldPickaxe.setOnAction((event) -> buyUpgrade(upgrades[2],goldQt));
        buyDiamondPickaxe.setOnAction((event) -> buyUpgrade(upgrades[3],diamondQt));

        //création de la scene
        upgradeScene = new Scene(new Group(buyStonePickaxe,buyIronPickaxe,buyGoldPickaxe,buyDiamondPickaxe,stoneQt,ironQt,goldQt,diamondQt));


    }

    //méthode pour aller chercher les images dans le dossier
    void InitGraphics () {

        for (int i = 0; i < upgrades.length; i++){

            try {
                images[i] = new ImageView(new Image(new FileInputStream("Images/" + i + ".png")));
            } catch (FileNotFoundException ex) {
                System.out.println( i + " IMAGE NOT FOUND");
            }

        }

    }

    void buyUpgrade (Upgrade upgrade, Label upgradeLabel) {

        if (blocksMined >= upgrade.getCost()) {


            blocksMined -= upgrade.getCost();
            blocksMinedS =  "Blocks mined: " + blocksMined;
            upgrade.setQt(upgrade.getQt()+ 1);
            bps++;
            upgradeLabel.setText("Cost : " + upgrade.getCost() + "You Have " + upgrade.getQt() + ".");
            updateCountSpeed();
            updateBps();

        }

    }

    void addBlocks(int amount) {

        blocksMined += amount;
        blocksMinedS = "Blocks mined: " + blocksMined;

    }

    void updateBps () {

        bpsS = "Blocks per second: " + bps;
        bpsLabel.setText(bpsS);

    }

    void updateCountSpeed () {

        double bpsTime = (1.0/bps);

        if (bpsTimeline.getStatus().equals(Animation.Status.RUNNING)) {bpsTimeline.stop();}

        bpsTimeline = new Timeline(new KeyFrame(Duration.seconds(bpsTime),t -> addBlocks(1)));
        bpsTimeline.setCycleCount(Animation.INDEFINITE);
        bpsTimeline.play();

    }

}
