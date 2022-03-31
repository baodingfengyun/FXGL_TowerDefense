package com.itcodebox.td.constant;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.itcodebox.td.data.TowerData;
import javafx.util.Duration;

/**
 * 可以静态导入此接口的全部内容,方便使用时直接使用
 *
 * @author LeeWyatt
 */
public interface Config {
    String VAR_TOWER_TYPE = "towerType";
    String VAR_IMAGE_NAME = "imgName";
    String VAR_WIDTH = "width";
    String VAR_HEIGHT = "height";

    String ET_LASER_TOWER = "laserTower";
    String ET_THUNDER_TOWER = "thunderTower";
    String ET_FLAME_TOWER = "flameTower";
    String ET_ARROW_TOWER = "arrowTower";
    String ET_POINT = "point";
    String ET_SPACE = "space";
    String ET_EMPTY = "empty";
    String ET_ENEMY = "enemy";
    String ET_LASER_TOWER_BULLET = "laserTowerBullet";
    String ET_FLAME_TOWER_BULLET = "flameTowerBullet";
    String ET_THUNDER_TOWER_BULLET = "thunderTowerBullet";
    String ET_ARROW_TOWER_BULLET = "arrowTowerBullet";
    String ET_BUILDING_INDICATOR = "buildIndicator";
    String ET_PLACED_BUTTON = "placedButton";
    String ET_PLACE_BOX = "placeBox";

    /**
     * 激光塔配置
     */
    TowerData LASER_TOWER_DATA = new TowerData(ET_LASER_TOWER, 30, 30, 15, 220,
            500, Duration.seconds(.2), FXGL.image("tower/laser/tower_icon.png", 30, 30));
    /**
     * 箭塔配置
     */
    TowerData ARROW_TOWER_DATA = new TowerData(ET_ARROW_TOWER, 43, 68, 1, 580,
            467, Duration.seconds(0.7), FXGL.image("tower/arrow/tower_icon.png")) {
        @Override
        public int getDamage() {
            return FXGLMath.random(10, 15);
        }
    };
    /**
     * 电塔配置
     */
    TowerData THUNDER_TOWER_DATA = new TowerData(ET_THUNDER_TOWER, 45, 72, 1, 260,
            600, Duration.seconds(.35), FXGL.image("tower/thunder/tower_icon.png")) {
        @Override
        public int getDamage() {
            return FXGLMath.random(20, 30);
        }
    };
    /**
     * 火塔配置
     */
    TowerData FLAME_TOWER_DATA = new TowerData(ET_FLAME_TOWER, 45, 89, 1, 350,
            350, Duration.seconds(.6), FXGL.image("tower/flame/tower_icon.png")) {
        @Override
        public int getDamage() {
            return FXGLMath.random(35, 50);
        }
    };
}
