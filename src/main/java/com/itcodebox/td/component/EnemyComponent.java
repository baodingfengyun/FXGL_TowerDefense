package com.itcodebox.td.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.ui.ProgressBar;
import com.itcodebox.td.TowerDefenseApp;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.LinkedHashMap;

/**
 * [组件] 敌人
 *
 * @author LeeWyatt
 */
public class EnemyComponent extends Component {
    /**
     * 血条组件
     */
    private HealthIntComponent hp;
    /**
     * 点位表
     */
    private LinkedHashMap<Integer, Pair<Point2D, String>> pointInfos;
    /**
     * 下一个位置
     */
    private Point2D nextWaypoint;
    /**
     * 移动速度
     */
    private double speed;
    int index = 0;
    /**
     * 进度条
     */
    private ProgressBar hpBar;
    private AnimatedTexture texture;
    /**
     * 4 个方向和死亡动画
     */
    private AnimationChannel animWalkRight, animWalkLeft, animWalkUp, animWalkDown, animDie;
    /**
     * 死亡标记
     */
    private boolean dead;

    public EnemyComponent(ProgressBar hpBar) {
        this.hpBar = hpBar;
        animWalkRight = new AnimationChannel(FXGL.image("enemy/enemy_move_right.png", 5 * 48, 48 * 3),
                5, 48, 48, Duration.seconds(.5), 0, 14);
        animWalkLeft = new AnimationChannel(FXGL.image("enemy/enemy_move_left.png", 5 * 48, 48 * 3),
                5, 48, 48, Duration.seconds(.5), 0, 14);
        animWalkUp = new AnimationChannel(FXGL.image("enemy/enemy_move_up.png", 5 * 48, 48 * 3),
                5, 48, 48, Duration.seconds(.5), 0, 14);
        animWalkDown = new AnimationChannel(FXGL.image("enemy/enemy_move_down.png", 5 * 48, 48 * 3),
                5, 48, 48, Duration.seconds(.5), 0, 14);
        animDie = new AnimationChannel(FXGL.image("enemy/enemy_die.png", 5 * 48, 2 * 48),
                5, 48, 48, Duration.seconds(.25), 0, 8);

        texture = new AnimatedTexture(animWalkRight);

    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void attacked(int damage) {
        hp.damage(damage);
        if (hp.isZero()) {
            dead = true;
            entity.getViewComponent().removeChild(hpBar);
            texture.loopAnimationChannel(animDie);
            texture.setOnCycleFinished(() -> entity.removeFromWorld());
        }

    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
        TowerDefenseApp app = (TowerDefenseApp) (FXGL.getApp());
        pointInfos = app.getPointInfos();
        nextWaypoint = pointInfos.get(index).getKey();
        walkAnim();
    }

    private void walkAnim() {
        String dir = pointInfos.get(index).getValue();
        if ("left".equals(dir)) {
            texture.loopAnimationChannel(animWalkLeft);
        } else if ("right".equals(dir)) {
            texture.loopAnimationChannel(animWalkRight);
        } else if ("up".equals(dir)) {
            texture.loopAnimationChannel(animWalkUp);
        } else if ("down".equals(dir)) {
            texture.loopAnimationChannel(animWalkDown);
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (index >= pointInfos.size() || dead) {
            return;
        }
        speed = tpf * 30 * 2;

        Point2D velocity = nextWaypoint.subtract(entity.getPosition()).normalize().multiply(speed);

        entity.translate(velocity);

        if (nextWaypoint.distance(entity.getPosition()) < speed) {
            entity.setPosition(nextWaypoint);
            walkAnim();
            index++;
            if (index < pointInfos.size()) {
                nextWaypoint = pointInfos.get(index).getKey();
            }

            //else {
            //    FXGL.getEventBus().fireEvent(new EnemyReachedGoalEvent());
            //}
        }
    }
}
