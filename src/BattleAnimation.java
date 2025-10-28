import javax.swing.*;
import java.awt.*;

public class BattleAnimation {

    public static void shakeAnimation(JLabel label, Runnable onComplete) {
        Point originalLocation = label.getLocation();
        Timer shakeTimer = new Timer(30, null);
        final int[] shakeCount = { 0 };

        shakeTimer.addActionListener(e -> {
            if (shakeCount[0] < 12) {

                int offsetX = (shakeCount[0] % 2 == 0) ? 8 : -8;
                int offsetY = (shakeCount[0] % 4 < 2) ? 2 : -2;
                label.setLocation(originalLocation.x + offsetX, originalLocation.y + offsetY);
                shakeCount[0]++;
            } else {
                label.setLocation(originalLocation);
                ((Timer) e.getSource()).stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        shakeTimer.start();
    }

    public static void attackMoveAnimation(JLabel label, boolean moveRight, Runnable onComplete) {
        Point originalLocation = label.getLocation();
        Timer moveTimer = new Timer(20, null);
        final int[] step = { 0 };
        final int maxSteps = 10;
        final int distance = 30;

        moveTimer.addActionListener(e -> {
            if (step[0] < maxSteps) {
                // move
                int offset = (int) ((distance * step[0]) / maxSteps);
                if (!moveRight)
                    offset = -offset;
                label.setLocation(originalLocation.x + offset, originalLocation.y);
                step[0]++;
            } else if (step[0] < maxSteps * 2) {
                // go back
                int offset = (int) ((distance * (maxSteps * 2 - step[0])) / maxSteps);
                if (!moveRight)
                    offset = -offset;
                label.setLocation(originalLocation.x + offset, originalLocation.y);
                step[0]++;
            } else {
                label.setLocation(originalLocation);
                ((Timer) e.getSource()).stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        moveTimer.start();
    }

    // hit flash
    public static void hitFlashAnimation(JLabel label, Runnable onComplete) {
        final int[] flashCount = { 0 };
        Timer flashTimer = new Timer(100, null);
        Color originalBg = label.getBackground();
        boolean wasOpaque = label.isOpaque();

        flashTimer.addActionListener(e -> {
            if (flashCount[0] < 3) {
                if (flashCount[0] % 2 == 0) {
                    label.setOpaque(true);
                    label.setBackground(new Color(255, 0, 0, 100));
                } else {
                    label.setOpaque(wasOpaque);
                    label.setBackground(originalBg);
                }
                flashCount[0]++;
            } else {
                label.setOpaque(wasOpaque);
                label.setBackground(originalBg);
                ((Timer) e.getSource()).stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        flashTimer.start();
    }

    // HP BAR
    public static void animateHPBar(JProgressBar hpBar, int fromValue, int toValue, Runnable onComplete) {
        Timer hpTimer = new Timer(20, null);
        final int difference = fromValue - toValue;
        final int steps = 20;
        final int[] currentStep = { 0 };

        hpTimer.addActionListener(e -> {
            if (currentStep[0] < steps) {
                int currentValue = fromValue - (difference * currentStep[0] / steps);
                hpBar.setValue(currentValue);
                currentStep[0]++;
            } else {
                hpBar.setValue(toValue);
                ((Timer) e.getSource()).stop();
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
        hpTimer.start();
    }

    public static void playerAttackCombo(JLabel playerLabel, JLabel enemyLabel, int damage, Runnable onComplete) {
        // 1. player move
        attackMoveAnimation(playerLabel, true, () -> {
            // 2. enemy attacked
            hitFlashAnimation(enemyLabel, () -> {
                shakeAnimation(enemyLabel, () -> {
                    // 3. finish
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });
            });
        });
    }

    public static void enemyAttackCombo(JLabel enemyLabel, JLabel playerLabel, int damage, Runnable onComplete) {
        // 1. enemy move
        attackMoveAnimation(enemyLabel, false, () -> {
            // 2. player attacked
            hitFlashAnimation(playerLabel, () -> {
                shakeAnimation(playerLabel, () -> {
                    // finish
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });
            });
        });
    }
}