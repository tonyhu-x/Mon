package com.taj.ourmonopoly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.taj.ourmonopoly.block.Metro;
import com.taj.ourmonopoly.block.Property;
import com.taj.ourmonopoly.dialog.AlertActionDialog;
import com.taj.ourmonopoly.dialog.AlertActionDialog.AlertAction;
import com.taj.ourmonopoly.dialog.AlertDialog;
import com.taj.ourmonopoly.dialog.BankDialog;
import com.taj.ourmonopoly.dialog.BlindAuctionDialog;
import com.taj.ourmonopoly.dialog.HospitalDialog;
import com.taj.ourmonopoly.dialog.JailDialog;
import com.taj.ourmonopoly.dialog.MetroDialog;
import com.taj.ourmonopoly.dialog.PropertyPurchaseDialog;
import com.taj.ourmonopoly.dialog.PropertyViewDialog;
import com.taj.ourmonopoly.dialog.TradeDialog;

/**
 * The GUI representation of a game.
 */
public class GameScreen extends ScreenAdapter {

    // these values preserve the aspect ratio of the map`
    final float worldWidth = 270;
    final float worldHeight = 390;

    private Skin skin;
    private GameApp game;
    private GameInstance instance;
    private Stage mainStage;
    private ArrayList<BlockImage> blockImages;
    private ArrayList<PlayerImage> playerImages;

    private Stage uiStage;
    private Queue<Dialog> dialogs = new LinkedList<Dialog>();
    private Label infoLabels[];
    /**
     * Fixed images that appear beside the labels.
     */
    private PlayerImage infoImages[];
    private TextButton nextButton;
    private TextButton tradeButton;
    private Table table;
    private OrthographicCamera camera;
    private float deltaZoom;
    private float deltaX, deltaY;

    // trading
    private boolean isTrading;
    private ArrayList<BlockImage> selectedImages;
    private Player selectedPlayer;

    public GameScreen(GameApp game, String[] arr) {
        this.game = game;
        this.instance = new GameInstance(this, arr);
        blockImages = new ArrayList<>();
        playerImages = new ArrayList<>();
        selectedImages = new ArrayList<>();

        try {
            createImages();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        for (var p : instance.players) {
            playerImages.add(new PlayerImage(p, blockImages.get(0), this));
        }
    }

    @Override
    public void show() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        camera = new OrthographicCamera();
        mainStage = new Stage(new FillViewport(worldWidth, worldHeight, camera), game.batch);
        camera.zoom = worldHeight / (worldWidth * GameApp.ASPECT_RATIO);
        deltaZoom = 0f;
        // the x and y aren't real screen coordinates!!!
        // they are the coordinates relative to the stage
        mainStage.addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                deltaZoom += 0.15f * amount;
                return true;
            }
        });
        
        mainStage.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                deltaX -= getDeltaX();
                deltaY -= getDeltaY();
            }
        });

        blockImages.forEach(mainStage::addActor);
        playerImages.forEach(mainStage::addActor);

        uiStage = new Stage(new FitViewport(GameApp.WINDOW_WIDTH, GameApp.WINDOW_HEIGHT), game.batch);
        infoLabels = new Label[instance.players.size()];
        infoImages = new PlayerImage[instance.players.size()];
        table = new Table();
        for (Player p : instance.players) {
            infoLabels[p.number] = new Label(p.name + ": " + instance.startingCashAmt, skin);
            infoImages[p.number] = new PlayerImage(p, this);
            table.add(infoImages[p.number]).size(80, 80).spaceRight(30);
            table.add(infoLabels[p.number]);
            table.row();
        }

        table.setBounds(50, 50, 300, 400);
        uiStage.addActor(table);
        
        nextButton = new TextButton("Next Player\n(Space)", skin);
        nextButton.setBounds(1500, 200, 200, 200);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextMove();
            }
        });

        tradeButton = new TextButton("Trade", skin);
        tradeButton.setBounds(1500, 450, 200, 200);
        tradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isTrading) {
                    if (selectedImages.isEmpty()) {
                        exitTrading();
                    }
                    else {
                        if (selectedPlayer == null) {
                            return;
                        }
                        ArrayList<Property> pro1 = new ArrayList<>();
                        ArrayList<Property> pro2 = new ArrayList<>();
                        
                        for (var im : selectedImages) {
                            var p = (Property) im.getBlock();
                            if (p.owner == instance.getCurrentPlayer())
                                pro1.add(p);
                            else
                                pro2.add(p);
                        }
                        createDialog("Trade", instance.getCurrentPlayer(), selectedPlayer, pro1, pro2);
                    }
                }
                else {
                    isTrading = true;
                    tradeButton.setColor(1, 0, 0, 1);
                }
            }
        });
        
        uiStage.addActor(nextButton);
        uiStage.addActor(tradeButton);
        uiStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (!dialogs.isEmpty())
                    return false;
                switch (keycode) {
                    case Input.Keys.SPACE:
                        nextMove();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        var input = new InputMultiplexer(uiStage, mainStage);
        Gdx.input.setInputProcessor(input);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.zoom += deltaZoom * 0.1f;
        deltaZoom = 0.9f * deltaZoom; 

        camera.translate(deltaX * 0.1f, deltaY * 0.1f);
        deltaX *= 0.9f;
        deltaY *= 0.9f;

        mainStage.getViewport().apply();
        mainStage.act(delta);
        mainStage.draw();

        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();

        if (dialogs.peek() != null && !dialogs.peek().hasKeyboardFocus()) {
            dialogs.poll();
            if (dialogs.peek() != null) {
                dialogs.peek().show(uiStage);
            }
        }
    }

    public void nextMove() {
        // disable the button and shortcut when trading
        if (isTrading)
            return;
        instance.nextPlayer();
        updateLabels();
        updateImages();
    }

    public void updateImages() {
        for (var p : playerImages) {
            p.setBlockParent(blockImages.get(instance.convertPos(p.player.getPosition())));
        }
        for (var b : blockImages) {
            b.updateImage();
        }
    }

    public void updateLabels() {
        for (var p : instance.players) {
            infoLabels[p.number].setText(p.name + ": " + p.cashAmt);
        }
        infoLabels[instance.turn].setColor(0, 1, 0, 1);
        infoLabels[instance.turn == 0 ? infoLabels.length - 1 : instance.turn - 1].setColor(1, 1, 1, 1);
    }

    @SuppressWarnings("unchecked")
    public void createDialog(String type, Object... args) {
        Dialog d;
        switch (type) {
            case "PurchaseProperty":
                d = new PropertyPurchaseDialog(
                    "Purchase Property",
                    skin,
                    this,
                    (Property) args[0],
                    (Player) args[1]
                );
                break;
            case "BlindAuction":
                d = new BlindAuctionDialog("Blind Auction", skin, this, (Property) args[0]);
                break;
            case "ViewProperty":
                d = new PropertyViewDialog(
                    "View Property", 
                    skin, 
                    this,
                    (Property) args[0],
                    instance.getCurrentPlayer()
                );
                break;
            case "ShowAlert":
                d = new AlertDialog("Alert", skin, (String) args[0]);
                break;
            case "AlertAction":
                d = new AlertActionDialog(
                    "Alert",
                    skin,
                    this,
                    (String) args[0],
                    (AlertAction) args[1]
                );
                break;
            case "Metro":
                d = new MetroDialog("Metro", skin, this, (Metro) args[0], (Player) args[1]);
                break;
            case "Jail":
                d = new JailDialog("Jail", skin, (Player) args[0], instance, this);
                break;
            case "Hospital":
                d = new HospitalDialog("Hospital", skin, (Player) args[0], instance, this);
                break;
            case "Bank":
                d = new BankDialog("Bank", skin, instance.getCurrentPlayer());
                break;
            case "Trade":
                d = new TradeDialog(
                    "Trade",
                    skin,
                    this,
                    (Player) args[0],
                    (Player) args[1],
                    (ArrayList<Property>) args[2],
                    (ArrayList<Property>) args[3]
                );
                break;
            default:
                return;
        }

        if (this.dialogs.isEmpty()) {
            d.show(uiStage);
        }
        this.dialogs.add(d);
    }

    private void createImages() throws IOException {
        String path = GameApp.PATH_TO_ASSETS + "blockPos.txt";
        BufferedReader csvReader = null;
        try {
            csvReader = new BufferedReader(new FileReader(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
            System.exit(-1);
        }

        String row;
        int count = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] tokens = row.split(",");
            int x = Integer.parseInt(tokens[0]), y = Integer.parseInt(tokens[1]);
            int rotate = Integer.parseInt(tokens[2]);

            blockImages.add(new BlockImage(instance.blocks.get(count), this, x, y, rotate));
            count++;
        }

    }

    public void selectPlayer(Player p) {
        if (p == instance.getCurrentPlayer() || p == selectedPlayer) {
            return;
        }
        for (var im : blockImages) {
            im.enable();
            if (im.getBlock() instanceof Property && ((Property) im.getBlock()).owner == instance.getCurrentPlayer()) {
                continue;
            }
            // clear previously selected property
            if (selectedPlayer != null)
                im.deselect();
            // disable other property choices
            if (im.getBlock() instanceof Property
                    && ((Property) im.getBlock()).owner != p
                    && ((Property) im.getBlock()).owner != instance.getCurrentPlayer()
                    || !(im.getBlock() instanceof Property))
            {
                if (im.getTouchable() == Touchable.disabled)
                    break;
                im.disable();
            }
        }
        selectedImages.removeIf(i -> ((Property) i.getBlock()).owner != instance.getCurrentPlayer());
        selectedPlayer = p;
    }

    public void selectImage(BlockImage image) {
        Property p = (Property) image.getBlock();
        if (p.owner != instance.getCurrentPlayer()) {
            selectPlayer(p.owner);
        }
        selectedImages.add(image);
    }

    public void deselectImage(BlockImage image) {
        selectedImages.remove(image);
        // long count = selectedImages
        //                 .stream()
        //                 .filter(i -> ((Property) i.getBlock()).owner != instance.getCurrentPlayer())
        //                 .count();
        // if (count == 0) {
        //     for (var b : blockImages) {
        //         b.enable();
        //     }
        //     selectedPlayer = null;
        // }
    }

    public void exitTrading() {
        for (var b : blockImages) {
            b.deselect();
            b.enable();
        }
        selectedImages.clear();
        selectedPlayer = null;
        tradeButton.setColor(1, 1, 1, 1);
        isTrading = false;
    }

    public boolean isTrading() {
        return isTrading;
    }

    public GameInstance getInstance() {
        return instance;
    }
}