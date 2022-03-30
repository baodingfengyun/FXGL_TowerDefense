package com.itcodebox.td.component;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

/**
 * [组件] 子弹
 *
 * @author LeeWyatt
 */
public class BulletComponent extends Component {
    /**
     * 子弹的初始位置
     */
    private Point2D initPosition;
    /**
     * 半径
     */
    private final int radius;
    /**
     * 伤害值
     */
    private int damage;

    public BulletComponent(int radius, int damage) {
        this.radius = radius;
        this.damage = damage;
    }

    @Override
    public void onAdded() {
        initPosition = entity.getPosition();
    }

    @Override
    public void onUpdate(double tpf) {
        Point2D position = entity.getPosition();
        if (position.distance(initPosition) > radius) {
            if (entity.isActive()) {
                entity.removeFromWorld();
            }
        }
    }

    public int getDamage() {
        return damage;
    }

}
