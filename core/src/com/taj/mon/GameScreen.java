package com.taj.mon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.taj.mon.BlockImage.State;
import com.taj.mon.block.Bank;
import com.taj.mon.block.Block;
import com.taj.mon.block.Metro;
import com.taj.mon.block.Property;
import com.taj.mon.dialog.AlertActionDialog;
import com.taj.mon.dialog.AlertActionDialog.AlertAction;
import com.taj.mon.dialog.AlertDialog;
import com.taj.mon.dialog.BankDialog;
import com.taj.mon.dialog.BankImage;
import com.taj.mon.dialog.BlindAuctionDialog;
import com.taj.mon.dialog.HospitalDialog;
import com.taj.mon.dialog.JailDialog;
import com.taj.mon.dialog.MetroDialog;
import com.taj.mon.dialog.PropertyPurchaseDialog;
import com.taj.mon.dialog.PropertyViewDialog;
import com.taj.mon.dialog.TradeDialog;

/**
 * The GUI representation of a game.
 */
public class GameScreen extends ScreenAdapter {

    private static class InfoLabel extends Label {

        private static Player curPlayer;

        private Player p;

        public InfoLabel(Skin skin, Player player) {
            super(player.name + ": " + player.cashAmt, skin);
            this.p = player;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (p.isBankrupt) {
                this.setText(p.name + ": Bankrupt");
            } else
                this.setText(p.name + ": " + p.cashAmt);

            if (this.p == curPlayer) {
                this.setColor(0, 1, 0, 1);
            } else {
                this.setColor(1, 1, 1, 1);
            }
        }

        private static void setCurPlayer(Player player) {
            curPlayer = player;
        }
    }

    // these values preserve the aspect ratio of the map
    private static final float WORLD_WIDTH = 270;
    private static final float WORLD_HEIGHT = 390;

    private Skin skin;
    private GameApp game;
    private GameInstance instance;
    private Stage mainStage;
    private ArrayList<BlockImage> blockImages;
    /**
     * A convenience variable holding (some duplicate) block images to match the map
     * route.
     */
    private ArrayList<BlockImage> virtualBlockImages;
    private ArrayList<PlayerImage> playerImages;

    private Stage uiStage;
    private Queue<Dialog> dialogs = new LinkedList<Dialog>();
    private InfoLabel[] infoLabels;
    /**
     * Fixed images that appear beside the labels.
     */
    private PlayerImage[] infoImages;
    private TextButton nextButton;
    private TextButton tradeButton;
    private TextButton sellButton;
    private DiceVisual[] dice;
    private ArrayList<Integer> diceRolls;
    private Consumer<List<Integer>> diceRollListener;
    private Table table;
    private OrthographicCamera camera;
    private float deltaZoom;
    private float deltaX, deltaY;

    private Player currentPlayer;

    // trading
    private boolean isTrading;
    private ArrayList<BlockImage> selectedImages;
    private Player selectedPlayer;

    private boolean isSelling;

    public GameScreen(GameApp game, String[] arr) {
        this.game = game;
        this.instance = new GameInstance(this, arr);
        blockImages = new ArrayList<>();
        virtualBlockImages = new ArrayList<>();
        playerImages = new ArrayList<>();
        selectedImages = new ArrayList<>();
        dice = new DiceVisual[2];
        diceRolls = new ArrayList<>();

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
        mainStage = new Stage(new FillViewport(WORLD_WIDTH, WORLD_HEIGHT, camera), game.batch);
        camera.zoom = WORLD_HEIGHT / (WORLD_WIDTH * GameApp.ASPECT_RATIO);
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

        uiStage = new Stage(new FitViewport(GameApp.WIN_WIDTH_HIGH, GameApp.WIN_HEIGHT_HIGH), game.batch);
        infoLabels = new InfoLabel[instance.players.size()];
        infoImages = new PlayerImage[instance.players.size()];
        table = new Table();
        for (Player p : instance.players) {
            infoLabels[p.number] = new InfoLabel(skin, p);
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
                if (isSelling || dice[0].isInAction())
                    return;
                if (isTrading) {
                    if (selectedImages.isEmpty()) {
                        exitTrading();
                    } else {
                        if (selectedPlayer == null) {
                            return;
                        }
                        ArrayList<Property> pro1 = new ArrayList<>();
                        ArrayList<Property> pro2 = new ArrayList<>();

                        for (var im : selectedImages) {
                            var p = (Property) im.getBlock();
                            if (p.owner == currentPlayer)
                                pro1.add(p);
                            else
                                pro2.add(p);
                        }
                        createDialog("Trade", currentPlayer, selectedPlayer, pro1, pro2);
                    }
                } else {
                    isTrading = true;
                    for (var im : blockImages) {
                        im.stateChanged(State.TRADE);
                    }
                    tradeButton.setColor(1, 0, 0, 1);
                }
            }
        });

        sellButton = new TextButton("Sell\nProperty", skin);
        sellButton.setBounds(1500, 700, 200, 150);
        sellButton.setVisible(false);
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isTrading)
                    return;
                isSelling = !isSelling;
                if (isSelling) {
                    sellButton.setColor(1, 0, 0, 1);
                    for (var im : blockImages) {
                        im.stateChanged(State.SELL);
                        im.targetPlayerChanged(currentPlayer);
                    }
                } else {
                    sellButton.setColor(1, 1, 1, 1);
                    for (var im : blockImages) {
                        im.stateChanged(State.NORMAL);
                    }
                }
            }
        });

        for (int i = 0; i < dice.length; i++) {
            dice[i] = new DiceVisual(this);
            dice[i].setPosition(50f + i * 80, 700f);
            uiStage.addActor(dice[i]);
        }

        uiStage.addActor(nextButton);
        uiStage.addActor(tradeButton);
        uiStage.addActor(sellButton);
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
        
        instance.onShow();
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

        for (var iter = playerImages.iterator(); iter.hasNext();) {
            var p = iter.next();
            if (p.player.isBankrupt) {
                p.remove();
                iter.remove();
            } else if (p.emptyRoute() && p.getParentBlock() != virtualBlockImages.get(p.player.getPosition()))
                p.addRoute(createRoute(p.player.getLastPosition(), p.player.getPosition(), p.player.isBackwards()));
        }

        mainStage.getViewport().apply();
        mainStage.act(delta);
        mainStage.draw();

        InfoLabel.setCurPlayer(currentPlayer);

        // near bankrupt, allow selling
        if (currentPlayer == null) {
            // I'm lazy
        }
        else if (currentPlayer.cashAmt < 0) {
            sellButton.setVisible(true);
        } else if (!isSelling) {
            sellButton.setVisible(false);
        }

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

    private List<BlockImage> createRoute(int ori, int dst, boolean backwards) {
        // ori should NOT equal dst
        ArrayList<BlockImage> temp = new ArrayList<>();
        if (backwards) {
            int t = ori;
            ori = dst;
            dst = t;
        }
        if (ori < dst) {
            temp.addAll(virtualBlockImages.subList(ori, dst + 1));
        }
        else {
            temp.addAll(virtualBlockImages.subList(ori, virtualBlockImages.size()));
            temp.addAll(virtualBlockImages.subList(0, dst + 1));
        }
        if (backwards) Collections.reverse(temp);
        return temp;
    }

    public void nextMove() {
        // disable the button and shortcut when trading or selling
        // also disable it when the dice animation is running
        if (isTrading || isSelling || dice[0].isVisible())
            return;
        if (currentPlayer != null && currentPlayer.cashAmt < 0) {
            return;
        }
        nextButton.setText("Next Player\n(Space)");
        for (var d : dice) {
            d.show();
        }
        instance.nextPlayer();
        currentPlayer = instance.getCurrentPlayer();
    }

    public void receiveDiceRoll(int roll) {
        diceRolls.add(roll);
        if (diceRolls.size() == dice.length) {
            diceRollListener.accept(diceRolls);
            diceRolls.clear();
        }
    }

    public void requestDiceRoll(Consumer<List<Integer>> diceRollListener, boolean waitForClick) {
        this.diceRollListener = diceRollListener;
        for (var d : dice) {
            d.show();
            if (!waitForClick)
                d.getDiceRoll();
        }
    }

    @SuppressWarnings("unchecked")
    public void createDialog(String type, Object... args) {
        Dialog d;
        switch (type) {
            case "PurchaseProperty":
                d = new PropertyPurchaseDialog("Purchase Property", skin, this, (Property) args[0], (Player) args[1]);
                break;
            case "BlindAuction":
                d = new BlindAuctionDialog("Blind Auction", skin, this, (Property) args[0]);
                break;
            case "ViewProperty":
                d = new PropertyViewDialog("View Property", skin, this, (Property) args[0], currentPlayer);
                break;
            case "ShowAlert":
                d = new AlertDialog("Alert", skin, (String) args[0]);
                break;
            case "AlertAction":
                d = new AlertActionDialog("Alert", skin, this, (String) args[0], (AlertAction) args[1]);
                break;
            case "Metro":
                d = new MetroDialog("Metro", skin, this, (Metro) args[0], (Player) args[1]);
                break;
            case "Jail":
                d = new JailDialog("Jail", skin, this, (Player) args[0]);
                break;
            case "Hospital":
                d = new HospitalDialog("Hospital", skin, this, (Player) args[0]);
                break;
            case "Bank":
                d = new BankDialog("Bank", skin, currentPlayer);
                break;
            case "Trade":
                d = new TradeDialog("Trade", skin, this, (Player) args[0], (Player) args[1],
                        (ArrayList<Property>) args[2], (ArrayList<Property>) args[3]);
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
        BufferedReader csvReader = null;
        csvReader = new BufferedReader(new InputStreamReader(FileHandle.class.getResourceAsStream("/blockPos.txt")));

        String row;
        int count = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] tokens = row.split(",");
            int x = Integer.parseInt(tokens[0]), y = Integer.parseInt(tokens[1]);
            int rotate = Integer.parseInt(tokens[2]);

            Block b = instance.blocks.get(count);
            if (b instanceof Property) {
                blockImages.add(new PropertyImage(b, this, x, y, rotate));
            }
            else if (b instanceof Bank) {
                blockImages.add(new BankImage(b, this, x, y, rotate));
            }
            else
                blockImages.add(new BlockImage(instance.blocks.get(count), this, x, y, rotate));
            count++;
        }
        for (int i = 0; i < GameInstance.MAP_SIZE; i++) {
            virtualBlockImages.add(blockImages.get(instance.convertPos(i)));
        }

    }

    public void selectPlayer(Player p) {
        if (p == currentPlayer || p == selectedPlayer) {
            return;
        }
        for (var im : blockImages) {
            im.targetPlayerChanged(p);
        }
        selectedImages.removeIf(i -> (((Property) i.getBlock()).owner != currentPlayer));
        selectedPlayer = p;
    }

    public void selectImage(BlockImage image) {
        Property p = (Property) image.getBlock();
        selectPlayer(p.owner);
        selectedImages.add(image);
    }

    public void deselectImage(BlockImage image) {
        selectedImages.remove(image);
        if (selectedImages.stream().allMatch(i -> ((Property) i.block).owner == currentPlayer)) {
            blockImages.forEach(im -> im.targetPlayerChanged(null));
            selectedPlayer = null;
        }
    }

    public void addActorToStage(Actor actor) {
        mainStage.addActor(actor);
    }

    public void exitTrading() {
        for (var b : blockImages) {
            b.stateChanged(State.NORMAL);
        }
        selectedImages.clear();
        selectedPlayer = null;
        tradeButton.setColor(1, 1, 1, 1);
        isTrading = false;
    }

    public void gameOver(Player winner) {
        createDialog(
            "AlertAction",
            "Game is over! Congratulations to " + winner.name + "!",
            (AlertAction) () -> game.setScreen(new TitleScreen(game))
        ); 
    }

    public boolean isTrading() {
        return isTrading;
    }

    public boolean isSelling() {
        return isSelling;
    }

    public GameInstance getInstance() {
        return instance;
    }

    /**
     * Suspends the current player and let the other player take action.
     * 
     * @param player the other player
     */
	public void waitForPlayer(Player player) {
        currentPlayer = player;
        nextButton.setText("Done");
    }

    /**
     * Returns the player that is currently interacting with the UI.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public DiceVisual[] getDice() {
        return dice;
    }
    
    @Override
    public void dispose() {
        skin.dispose();
        uiStage.dispose();
        mainStage.dispose();
    }
    
    @Override
    public void hide() {
        this.dispose();
        Gdx.input.setInputProcessor(null);
    }
}