import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BattleGame extends JFrame {
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private int currentStage;
    private int enemyIndex;

    // UI Components
    private JTextArea battleLog;
    private JProgressBar playerHpBar, enemyHpBar;
    private JLabel playerInfoLabel, enemyInfoLabel, skillInfolabel;
    private JLabel playerImageLabel, enemyImageLabel;
    private JButton attackBtn, potionBtn, runBtn, homeBtn, skillBtn;
    private JLabel potionCountLabel;
    private ImagePanel battlePanel;

    public BattleGame() {
        setTitle("Three Knight Rebirth");
        setSize(1080, 765);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showCharacterSelection();
    }

    private void showCharacterSelection() {
        try {
            Image backgruadmain = ImageIO.read(getClass().getResourceAsStream("/resources/A_bgmain.jpg"));
            ImagePanel selectionPanel = new ImagePanel(backgruadmain);
            selectionPanel.setLayout(new BorderLayout());
            selectionPanel.setBorder(BorderFactory.createEmptyBorder(110, 110, 110, 110));

            JPanel options = new JPanel(new GridLayout(1, 3, 20, 20));
            options.setBackground(Color.darkGray);

            options.add(characterCard("SWORDMAN", 1000, 200, 20, "BladeBreaker (Attack:300% + EnemyDef-20%] ) ",
                    Color.black));
            options.add(characterCard("MAGE", 600, 370, 12, "Inferno (Attack:500% + playerHp:-10%)", Color.black));
            options.add(characterCard("ARROWMAN", 800, 299, 15, "ArrowRain (Attack:350% + heal:10% of playHP)",
                    Color.black));
            selectionPanel.add(options, BorderLayout.CENTER);

            setContentPane(selectionPanel);
            revalidate();
            repaint();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load main background image:\n" + e.getMessage(),
                    "Image Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private JPanel characterCard(String name, int hp, int atk, int def, String detailSkill, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));

        JLabel lbl = new JLabel(
                "<html><center><b>" + name + "</b><br/>HP: " + hp + " ATK: " + atk + " DEF: " + def + "<br/>Skill : "
                        + detailSkill + "</center></html>",
                SwingConstants.CENTER);
        lbl.setBorder(BorderFactory.createEmptyBorder(50, 40, 60, 40));
        p.add(lbl, BorderLayout.SOUTH);

        JPanel preview = new JPanel() {
            private Image cardImage;
            {
                try {
                    cardImage = ImageIO
                            .read(getClass().getResourceAsStream("/resources/A_" + name.toLowerCase() + ".png"));
                } catch (IOException e) {
                    System.err.println("Cannot load image for: " + name);
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (cardImage != null) {
                    g.drawImage(cardImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        preview.setPreferredSize(new Dimension(140, 140));
        p.add(preview, BorderLayout.CENTER);

        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Image battlePlayerIMG = ImageIO
                            .read(getClass().getResource("/resources/A_" + name.toLowerCase() + "_battle.png"));
                    player = new Player(name, hp, hp, atk, def, color, battlePlayerIMG);
                    startGame();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error loading image file for " + name, "Image Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return p;
    }

    private void startGame() {
        currentStage = 1;
        initBattleUI();
        setupStage();
    }

    private void setupStage() {
        enemies.clear();
        enemyIndex = 0;
        battleLog.setText("Stage " + currentStage + "\n");
        attackBtn.setVisible(true);
        potionBtn.setVisible(true);
        runBtn.setVisible(true);
        potionCountLabel.setVisible(true);
        homeBtn.setVisible(false);

        try {
            Image backguardStage = null;
            switch (currentStage) {
                case 1:
                    battleLog.append("A wild Goblin appears!\n");
                    Image goblinImage = ImageIO.read(getClass().getResource("/resources/A_goblin.png"));
                    backguardStage = ImageIO.read(getClass().getResource("/resources/A_bgflorest.jpg"));

                    enemies.add(new Goblin(10, goblinImage));
                    enemies.add(new Goblin(15, goblinImage));
                    enemies.add(new Goblin(20, goblinImage));
                    break;
                case 2:
                    battleLog.append("A pair of Orks block your path!\n");
                    Image orkImage = ImageIO.read(getClass().getResource("/resources/A_ork.png"));
                    backguardStage = ImageIO.read(getClass().getResource("/resources/A_bgork.jpg"));

                    enemies.add(new Ork(30, orkImage));
                    enemies.add(new Ork(40, orkImage));
                    break;
                case 3:
                    battleLog.append("A fearsome Dragon descends from the sky!\n");
                    Image dragonImage = ImageIO.read(getClass().getResource("/resources/A_dragon.png"));
                    backguardStage = ImageIO.read(getClass().getResource("/resources/A_bgdragon.jpg"));

                    enemies.add(new Dragon(50, dragonImage));
                    break;

                default:
                    battleLog.append("You have completed all stages! Victory!\n");
                    battleLog.append("New stage is comingSoon!\n");
                    battleLog.append("Thank You!\n");
                    backguardStage = ImageIO.read(getClass().getResource("/resources/A_bgend.jpg"));
                    disableButtons();
                    playerHpBar.setVisible(false);
                    enemyHpBar.setVisible(false);
                    playerInfoLabel.setVisible(false);
                    enemyInfoLabel.setVisible(false);
                    playerImageLabel.setVisible(false);
                    skillInfolabel.setVisible(false);
                    break;
            }
            if (backguardStage != null && battlePanel != null) {
                battlePanel.setBackguardImage(backguardStage);
                battlePanel.repaint();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load assets for Stage " + currentStage + ":\n" + e.getMessage(),
                    "Asset Error", JOptionPane.ERROR_MESSAGE);
            enemies.clear();

        }
        updateBattleUI();
    }

    private void initBattleUI() {
        battlePanel = new ImagePanel(null);
        battlePanel.setLayout(null);
        battlePanel.setBackground(new Color(30, 30, 40));
        battlePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Player Image
        playerImageLabel = new JLabel();
        playerImageLabel.setBounds(120, 280, 220, 220);
        battlePanel.add(playerImageLabel);

        // Enemy Image
        enemyImageLabel = new JLabel();
        enemyImageLabel.setBounds(650, 180, 280, 280);
        battlePanel.add(enemyImageLabel);

        // Player HP bar
        playerHpBar = new JProgressBar();
        playerHpBar.setStringPainted(true);
        playerHpBar.setForeground(new Color(0, 220, 0));
        playerHpBar.setBounds(80, 520, 300, 25);
        battlePanel.add(playerHpBar);

        // Enemy HP bar
        enemyHpBar = new JProgressBar();
        enemyHpBar.setStringPainted(true);
        enemyHpBar.setForeground(new Color(220, 0, 0));
        enemyHpBar.setBounds(600, 140, 320, 25);
        battlePanel.add(enemyHpBar);

        // Info label
        playerInfoLabel = new JLabel("Player Info");
        playerInfoLabel.setForeground(Color.WHITE);
        playerInfoLabel.setBounds(80, 490, 300, 40);
        battlePanel.add(playerInfoLabel);

        enemyInfoLabel = new JLabel("Enemy Info", SwingConstants.RIGHT);
        enemyInfoLabel.setForeground(Color.WHITE);
        enemyInfoLabel.setBounds(600, 110, 300, 40);
        battlePanel.add(enemyInfoLabel);

        skillInfolabel = new JLabel("Skill Info", SwingConstants.CENTER);
        skillInfolabel.setForeground(Color.WHITE);
        skillInfolabel.setBounds(400, 490, 300, 40);
        battlePanel.add(skillInfolabel);

        // Battle Log
        battleLog = new JTextArea();
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        battleLog.setBackground(new Color(45, 45, 60));
        battleLog.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(battleLog);
        scrollPane.setBounds(100, 560, 800, 100);
        battlePanel.add(scrollPane);

        // Buttons
        attackBtn = new JButton("ATTACK");
        potionBtn = new JButton("POTION");
        runBtn = new JButton("RUN");
        homeBtn = new JButton("HOME");
        skillBtn = new JButton("SKILL");

        attackBtn.setBounds(300, 670, 120, 40);
        potionBtn.setBounds(450, 670, 120, 40);
        runBtn.setBounds(600, 670, 120, 40);
        homeBtn.setBounds(450, 670, 120, 40);
        skillBtn.setBounds(150, 670, 120, 40);

        homeBtn.setVisible(false);

        for (JButton btn : new JButton[] { attackBtn, potionBtn, runBtn, homeBtn, skillBtn }) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setBackground(new Color(80, 80, 120));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            battlePanel.add(btn);
        }

        potionCountLabel = new JLabel("Potions: 3");
        potionCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        potionCountLabel.setForeground(new Color(0, 200, 0));
        potionCountLabel.setBounds(780, 670, 200, 40);
        battlePanel.add(potionCountLabel);

        // Event listeners
        attackBtn.addActionListener(e -> playerAttack());
        potionBtn.addActionListener(e -> {
            if (player.usePotions()) {
                battleLog.append("You used a potion and healed for 150 HP.\n");
                updateBattleUI();
                Timer timer = new Timer(1000, evt -> enemyTurn());
                timer.setRepeats(false);
                timer.start();
            } else {
                battleLog.append("You cannot use a potion! (No potions left or HP is full)\n");
            }
        });
        runBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to surrender and return to the character selection?",
                    "Surrender",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dispose(); // Close
                SwingUtilities.invokeLater(() -> {
                    BattleGame newGame = new BattleGame();
                    newGame.setVisible(true); // Start
                });
            }
        });

        skillBtn.addActionListener(e -> playerUseskill());

        setContentPane(battlePanel);
        revalidate();
        repaint();
    }

    private void updateBattleUI() {
        if (player == null)
            return;

        // --- Update Player Info ---
        playerInfoLabel.setText(String.format("<html><b>%s</b><br>HP: %d / %d<br>ATK: %d DEF: %d</html>",
                player.getName(), player.getCurrentHp(), player.getMaxHp(),
                player.getAtkPower(), player.getDef()));
        playerHpBar.setMaximum(player.getMaxHp());
        playerHpBar.setValue(player.getCurrentHp());
        if (player.getImage() != null) {
            playerImageLabel.setIcon(new ImageIcon(player.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
        }

        // --- Update Potion Count ---
        potionCountLabel.setText(" Potions: " + player.getPotions());

        // --- Update Enemy Info ---
        if (enemies.isEmpty() || enemyIndex >= enemies.size()) {
            enemyInfoLabel.setText("---");
            enemyHpBar.setValue(0);
            enemyImageLabel.setIcon(null);
        } else {
            Enemy currentEnemy = enemies.get(enemyIndex);
            enemyInfoLabel.setText(String.format("<html><b>%s (Lv. %d)</b><br>HP: %d / %d<br>ATK: %d DEF: %d</html>",
                    currentEnemy.getName(), currentEnemy.getLevel(),
                    currentEnemy.getCurrentHp(), currentEnemy.getMaxHp(),
                    currentEnemy.getAtkPower(), currentEnemy.getDef()));
            enemyHpBar.setMaximum(currentEnemy.getMaxHp());
            enemyHpBar.setValue(currentEnemy.getCurrentHp());
            if (currentEnemy.getImage() != null) {
                enemyImageLabel.setIcon(
                        new ImageIcon(currentEnemy.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
            } else {
                enemyImageLabel.setIcon(null);
            }
        }
        // --- Update Skill Info ---
        // check skill cooldown
        if (player.getSkill() != null) {
            Skill skill = player.getSkill();
            if (skill.isAvailable()) {
                skillInfolabel.setText(skill.name + " : Ready");
                skillInfolabel.setLayout(new BorderLayout());
                skillInfolabel.setBounds(725, 572, 200, 200);

                skillBtn.setEnabled(true);
            } else {
                skillInfolabel.setText(skill.name + " : Cooldown " + skill.getCurrenCooldown() + " turns");
                skillBtn.setEnabled(false);
            }
        } else {
            skillInfolabel.setText("No Skill");
            skillBtn.setEnabled(false);
        }
    }

    private void playerUseskill() {
        if (enemies.isEmpty() || enemyIndex >= enemies.size())
            return;
        Enemy currentEnemy = enemies.get(enemyIndex);
        // Disable buttons during animation
        attackBtn.setEnabled(false);
        potionBtn.setEnabled(false);
        runBtn.setEnabled(false);
        // Calculate damage
        int result = player.useSkill(currentEnemy);
        if (result == 0) {
            battleLog.append("No skill assigned!\n");
            attackBtn.setEnabled(true);
            potionBtn.setEnabled(true);
            runBtn.setEnabled(true);
            skillBtn.setEnabled(false);
            return;
        }
        if (result == -1) {
            battleLog.append("Skill is not ready yet!\n");
            attackBtn.setEnabled(true);
            potionBtn.setEnabled(true);
            runBtn.setEnabled(true);
            skillBtn.setEnabled(false);
            return;
        }
        int damage = result;
        // use animation combo + battle log + apply damage + animate hp bar
        BattleAnimation.playerAttackCombo(enemyInfoLabel, enemyImageLabel, damage, () -> {
            String skillName = player.getSkill().getName();
            battleLog.append("You used " + skillName + " and Attack " + damage + " damage to the "
                    + currentEnemy.getName() + "!\n");
            // Apply damage to enemy
            int oldHp = currentEnemy.getCurrentHp();
            currentEnemy.takedamage(damage);
            // Animate HP bar
            BattleAnimation.animateHPBar(enemyHpBar, oldHp, currentEnemy.currentHp, () -> {
                updateBattleUI();
                if (!currentEnemy.isAlive()) {
                    battleLog.append("The " + currentEnemy.getName() + " has been defeated!\n");
                    enemyIndex++;
                    if (enemyIndex >= enemies.size()) {
                        battleLog.append("Stage " + currentStage + " cleared!\n");
                        currentStage++;
                        Timer timer = new Timer(1500, e -> {
                            setupStage();
                            attackBtn.setEnabled(true);
                            potionBtn.setEnabled(true);
                            runBtn.setEnabled(true);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        battleLog.append("Next enemy appears!\n");
                        updateBattleUI();
                        Timer timer = new Timer(500, e -> enemyTurn());
                        timer.setRepeats(false);
                        timer.start();
                    }
                } else {
                    Timer timer = new Timer(1000, e -> enemyTurn());
                    timer.setRepeats(false);
                    timer.start();
                }
            });
        });
    }

    private void playerAttack() {
        if (enemies.isEmpty() || enemyIndex >= enemies.size())
            return;

        Enemy currentEnemy = enemies.get(enemyIndex);

        // Disable buttons during animation
        attackBtn.setEnabled(false);
        potionBtn.setEnabled(false);
        runBtn.setEnabled(false);
        skillBtn.setEnabled(false);

        // Calculate damage
        int damage = player.atkPower();

        // ใช้ animation combo
        BattleAnimation.playerAttackCombo(playerImageLabel, enemyImageLabel, damage, () -> {
            battleLog.append("You attack the " + currentEnemy.getName() + " for " + damage + " damage!\n");

            // Apply damage to enemy
            int oldHp = currentEnemy.getCurrentHp();
            currentEnemy.takedamage(damage);

            // Animate HP bar
            BattleAnimation.animateHPBar(enemyHpBar, oldHp, currentEnemy.getCurrentHp(), () -> {
                updateBattleUI();

                // Check if enemy is defeated
                if (!currentEnemy.isAlive()) {
                    battleLog.append("The " + currentEnemy.getName() + " has been defeated!\n");
                    enemyIndex++;

                    if (enemyIndex >= enemies.size()) {
                        battleLog.append("Stage " + currentStage + " cleared!\n");
                        currentStage++;

                        Timer timer = new Timer(1500, e -> {
                            setupStage();
                            attackBtn.setEnabled(true);
                            potionBtn.setEnabled(true);
                            runBtn.setEnabled(true);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        battleLog.append("Next enemy appears!\n");
                        updateBattleUI();
                        Timer timer = new Timer(500, e -> enemyTurn());
                        timer.setRepeats(false);
                        timer.start();
                    }
                } else {
                    Timer timer = new Timer(1000, e -> enemyTurn());
                    timer.setRepeats(false);
                    timer.start();
                }
            });
        });
    }

    private void enemyTurn() {
        if (enemies.isEmpty() || enemyIndex >= enemies.size()) {
            attackBtn.setEnabled(true);
            potionBtn.setEnabled(true);
            runBtn.setEnabled(true);
            skillBtn.setEnabled(false);
            return;
        }
        player.reduceSkillCooldown();

        Enemy currentEnemy = enemies.get(enemyIndex);
        // Calculate damage
        int damage = currentEnemy.atkPower();

        if (!currentEnemy.isAlive()) {
            attackBtn.setEnabled(true);
            potionBtn.setEnabled(true);
            runBtn.setEnabled(true);
            skillBtn.setEnabled(false);
            return;
        }

        // ใช้ animation combo
        BattleAnimation.enemyAttackCombo(enemyImageLabel, playerImageLabel, damage, () -> {
            battleLog.append("The " + currentEnemy.getName() + " attacks you for " + damage + " damage!\n");

            // Apply damage to player
            int oldHp = player.getCurrentHp();
            player.takedamage(damage);

            // Animate HP bar
            BattleAnimation.animateHPBar(playerHpBar, oldHp, player.getCurrentHp(), () -> {
                updateBattleUI();

                if (!player.isAlive()) {
                    battleLog.append("You have been defeated...\n");
                    battleLog.append("GAME OVER\n");

                    Timer timer = new Timer(1000, e -> {
                        int choice = JOptionPane.showConfirmDialog(this,
                                "You were defeated! Try again?",
                                "Game Over",
                                JOptionPane.YES_NO_OPTION);

                        if (choice == JOptionPane.YES_OPTION) {
                            dispose();
                            SwingUtilities.invokeLater(() -> {
                                BattleGame newGame = new BattleGame();
                                newGame.setVisible(true);
                            });
                        } else {
                            System.exit(0);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    attackBtn.setEnabled(true);
                    potionBtn.setEnabled(true);
                    runBtn.setEnabled(true);
                }
            });
        });
    }

    private void disableButtons() {
        attackBtn.setVisible(false);
        potionBtn.setVisible(false);
        runBtn.setVisible(false);
        potionCountLabel.setVisible(false);
        homeBtn.setVisible(true);
        skillBtn.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BattleGame game = new BattleGame();
            game.setVisible(true);
        });
    }
}