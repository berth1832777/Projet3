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
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*

Thomas Bergeron
Projet 3: Clicker

 */
public class MineClicker extends Application {

    public static Scene mainScene;
    public static Scene upgradeScene;
    public static Scene enchantScene;
    static double blocksMined;
    static int bps;
    static Label bpsLabel; // je dois metttre ce label la ici parce que j'ai besoin de l'accéder en dehors de la méthode SetupMainWindow.
    static ImageView[] images = new ImageView[5];

    static Upgrade[] upgrades = {
            new Upgrade("Stone Pickaxe",20,1),
            new Upgrade("Iron Pickaxe",500,5),
            new Upgrade("Gold Pickaxe",10000,100),
            new Upgrade("Diamond Pickaxe",100000,1000)
    };


    static int clickStrength = 1;
    static Upgrade enchant = new Enchant();

    static Timeline bpsTimeline = new Timeline();

    public static void main(String[] args) { launch(args);}

    @Override
    public void start (Stage primaryStage) {


        InitGraphics();

        SetupMainWindow();
        SetupUpgradeWindow();
        SetupEnchantWindow();


        //stage avec le bouton a cliquer
        primaryStage.setScene(mainScene);

        primaryStage.setTitle("Minecraft Clicker : Also play Terraria!");
        primaryStage.setResizable(false);
        try{ primaryStage.getIcons().add(new Image(new FileInputStream("Images/dirtIcon.png"))); }
        catch (FileNotFoundException ex) { System.out.println("Dirt icon not found!"); }

        primaryStage.show();



        //stage avec les upgrades (resizable car des fois quand on a trop d'upgrades le texte sort de la fenêtre.)
        Stage upgradeStage = new Stage();
        upgradeStage.setTitle("Miner");
        upgradeStage.setResizable(true);
        try{ upgradeStage.getIcons().add(new Image(new FileInputStream("Images/villagerIcon.png"))); }
        catch (FileNotFoundException ex) { System.out.println("Villager icon not found!"); }

        upgradeStage.setScene(upgradeScene);

        upgradeStage.show();


        //stage avec l'enchantement
        Stage enchantStage = new Stage();
        enchantStage.setTitle("Enchanter");
        enchantStage.setResizable(false);
        try{ enchantStage.getIcons().add(new Image(new FileInputStream("Images/villagerIcon.png"))); }
        catch (FileNotFoundException ex) { System.out.println("Villager icon not found!"); }

        enchantStage.setScene(enchantScene);

        enchantStage.show();


    }

    void SetupMainWindow() {

        Button click = new Button();
        try {click = new Button("Mine!",new ImageView(new Image(new FileInputStream("Images/stone.png"))));} catch (FileNotFoundException ex) {
            System.out.println("stone image not found!");
        }
        Label scoreDisplay = new Label("Blocks mined:\n0");
        bpsLabel = new Label("Blocks per second : " + ParseNumber(bps));

        click.setScaleX(1);
        click.setScaleY(1);
        click.setTranslateX(10);
        click.setTranslateY(100);

        click.setOnAction( (event) ->addBlocks(clickStrength));


        //timeline qui update le texte scoreDisplay
        Timeline textTimeline = new Timeline(new KeyFrame(Duration.millis(10),t -> {


            scoreDisplay.setText("Blocks mined:\n" + ParseNumber(blocksMined));

            //Quand la variable blocksMined se wrap et devient négative, on gagne la partie et le jeu se ferme.
            if (blocksMined < 0) {

                System.out.println("Vous avez miné tout le monde de Minecraft! Bravo! Maintenant arrêtez de jouer!");
                System.exit(0);

            }

        }));
        textTimeline.setCycleCount(Animation.INDEFINITE);
        textTimeline.play();


        //timeline qui update le nombre de blocs
        bpsTimeline = new Timeline(new KeyFrame(Duration.millis(10),t -> addBlocks((bps/100.0))));
        bpsTimeline.setCycleCount(Animation.INDEFINITE);
        bpsTimeline.play();

        scoreDisplay.setTranslateX(100);
        scoreDisplay.setTranslateY(10);
        scoreDisplay.setFont(new Font(30));



        mainScene = new Scene(new Group(click,scoreDisplay,bpsLabel));

    }

    void SetupUpgradeWindow () {

        //buttons
        Button buyStonePickaxe = new Button("Buy Stone Pickaxe (+ " + upgrades[0].getStrength() + " bps)", images[1]);
        Button buyIronPickaxe = new Button("Buy Iron Pickaxe(+ " + upgrades[1].getStrength() + " bps)",images[2]);
        Button buyGoldPickaxe = new Button("Buy Gold Pickaxe(+" + upgrades[2].getStrength() + " bps)",images[3]);
        Button buyDiamondPickaxe = new Button("Buy Diamond Pickaxe(+ " + ParseNumber(upgrades[3].getStrength()) + " bps)",images[4]); //J'ai mis un ParseNumber ici parce que le bonus de bps est plus grand que 1000 au départ, mais pas les autres.

        buyStonePickaxe.setTranslateY(0);
        buyIronPickaxe.setTranslateY(25);
        buyGoldPickaxe.setTranslateY(50);
        buyDiamondPickaxe.setTranslateY(75);


        //labels
        Label stoneQt = new Label("Cost : " + ParseNumber(upgrades[0].getCost()) + ". You Have " + ParseNumber(upgrades[0].getQt()) + ".");
        Label ironQt = new Label("Cost : " + ParseNumber(upgrades[1].getCost()) + ". You Have " + ParseNumber(upgrades[1].getQt()) + ".");
        Label goldQt = new Label("Cost : " + ParseNumber(upgrades[2].getCost()) + ". You Have " + ParseNumber(upgrades[2].getQt()) + ".");
        Label diamondQt = new Label("Cost : " + ParseNumber(upgrades[3].getCost()) + ". You Have " + ParseNumber(upgrades[3].getQt()) + ".");

        stoneQt.setTranslateX(230);
        stoneQt.setTranslateY(5);
        ironQt.setTranslateX(230);
        ironQt.setTranslateY(30);
        goldQt.setTranslateX(230);
        goldQt.setTranslateY(55);
        diamondQt.setTranslateX(230);
        diamondQt.setTranslateY(80);


        //listeners pour les upgrades
        buyStonePickaxe.setOnAction((event) -> buyUpgrade(upgrades[0],stoneQt,buyStonePickaxe));
        buyIronPickaxe.setOnAction((event) -> buyUpgrade(upgrades[1],ironQt,buyIronPickaxe));
        buyGoldPickaxe.setOnAction((event) -> buyUpgrade(upgrades[2],goldQt,buyGoldPickaxe));
        buyDiamondPickaxe.setOnAction((event) -> buyUpgrade(upgrades[3],diamondQt,buyDiamondPickaxe));

        //création de la scene
        upgradeScene = new Scene(new Group(buyStonePickaxe,buyIronPickaxe,buyGoldPickaxe,buyDiamondPickaxe,stoneQt,ironQt,goldQt,diamondQt));


    }

    void SetupEnchantWindow () {

        //ramasser l'image pour le bouton d'enchant
        ImageView enchantPic = new ImageView();
        try{enchantPic = new ImageView(new Image(new FileInputStream("Images/enchant.png")));} catch (FileNotFoundException ex) { System.out.println("ENCHANT PIC NOT FOUND!"); }

        Button enchantPick = new Button("Enchant! (Current click power : " + ParseNumber(clickStrength) + ")",enchantPic);
        Label enchantCost = new Label("You can enchant your pickaxe to increase the power of your clicks.\nYour pickaxe is efficiency " + ParseNumber(enchant.getQt()+1) + ". Cost : " + ParseNumber(enchant.getCost()) + ".");

        enchantPick.setTranslateX(10);
        enchantPick.setTranslateY(50);

        enchantPick.setOnAction((event) -> upgradeClick(enchantCost, enchantPick));


        enchantScene = new Scene((new Group(enchantPick,enchantCost)));

    }

    void upgradeClick (Label enchantLabel, Button enchantButton) {

        if (blocksMined >= enchant.getCost()) {


            blocksMined -= enchant.getCost();
            enchant.setQt(enchant.getQt()+ 1);

            clickStrength += enchant.getStrength();

            //bonus quand on obtient une dizaine de chaque upgrade, la force des upgrades double (+1 car on commence déjà à 1 de clickstrength)
            if ((enchant.getQt()+1)%10 == 0) {

                enchant.setStrength(enchant.getStrength() * 2);

            }


            enchantLabel.setText("You can enchant your pickaxe to increase the power of your clicks.\nYour pickaxe is efficiency " + ParseNumber(enchant.getQt()+1) + ". Cost : " + ParseNumber(enchant.getCost()) + ".");
            enchantButton.setText("Enchant! (Current click power : " + ParseNumber(clickStrength) + ")");

        }

    }

    //méthode pour aller chercher les images des pioches dans le dossier
    void InitGraphics () {

        for (int i = 0; i < images.length; i++){

            try {
                images[i] = new ImageView(new Image(new FileInputStream("Images/" + i + ".png")));
            } catch (FileNotFoundException ex) {
                System.out.println( i + " IMAGE NOT FOUND");
            }

        }

    }

    void buyUpgrade (Upgrade upgrade, Label upgradeLabel, Button button) {

        if (blocksMined >= upgrade.getCost()) {


            blocksMined -= upgrade.getCost();
            upgrade.setQt(upgrade.getQt()+ 1);

            bps += upgrade.getStrength();

            //bonus quand on obtient une dizaine de chaque upgrade, la force des upgrades double
            if (upgrade.getQt()%10 == 0) {

                upgrade.setStrength(upgrade.getStrength() * 2);

            }


            upgradeLabel.setText("Cost : " + ParseNumber(upgrade.getCost()) + ". You Have " + ParseNumber(upgrade.getQt()) + ".");
            button.setText("Buy " + upgrade.getName() + "(+ " + ParseNumber(upgrade.getStrength()) + " bps)");
            updateBps();

        }

    }

    void addBlocks(double amount) {

        blocksMined += amount;

    }

    void updateBps () {

        bpsLabel.setText("Blocks per second: " + ParseNumber(bps));

    }

    String ParseNumber (double value) {

            //méthode qui met des espaces après 3 chiffres dans un nombre(par exemple 1400 devient 1 400).
            int valueInt = (int)value;
            DecimalFormat df = new DecimalFormat("#,###");

            return df.format(valueInt);

    }

}
