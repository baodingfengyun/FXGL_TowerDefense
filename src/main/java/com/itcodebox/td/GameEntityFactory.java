package com.itcodebox.td;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.ProgressBar;
import com.itcodebox.td.component.*;
import com.itcodebox.td.constant.Config;
import com.itcodebox.td.constant.GameType;
import com.itcodebox.td.data.TowerData;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.itcodebox.td.constant.Config.*;

/**
 * 游戏内实体构建工厂(有意思的部分)
 *
 * @author LeeWyatt
 */
public class GameEntityFactory implements EntityFactory {

    /**
     * 激光炮塔
     */
    @Spawns(ET_LASER_TOWER)
    public Entity newTower(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.TOWER) //类型
                .bbox(BoundingShape.box(30, 30)) //碰撞检测矩形
                .with(new LaserTowerComponent()) //+激光塔组件
                .collidable() //+内置碰撞组件
                .build();
    }

    /**
     * 雷系炮塔
     */
    @Spawns(ET_THUNDER_TOWER)
    public Entity newThunderTower(SpawnData data) {
        return buildFiveElementsTower(data, THUNDER_TOWER_DATA);
    }

    /**
     * 火系炮塔
     */
    @Spawns(ET_FLAME_TOWER)
    public Entity newFlameTower(SpawnData data) {
        return buildFiveElementsTower(data, FLAME_TOWER_DATA);
    }

    private Entity buildFiveElementsTower(SpawnData data, TowerData towerData) {
        return FXGL.entityBuilder(data)
                .type(GameType.TOWER) //类型
                .bbox(BoundingShape.box(towerData.getWidth(), towerData.getHeight())) //碰撞检测矩形
                .with(new FiveElementsTowerComponent(towerData)) //+五元素塔组件
                .collidable() //+内置碰撞组件
                .build();
    }

    /**
     * 箭塔
     */
    @Spawns(ET_ARROW_TOWER)
    public Entity newArrowTower(SpawnData data) {
        return entityBuilder(data)
                .type(GameType.TOWER) //类型
                .viewWithBBox(FXGL.texture("tower/arrow/tower.png")) //渲染图及同等大小的碰撞检测矩形
                .with(new ArrowTowerComponent()) //+箭塔组件
                .build();
    }

    /**
     * 点
     */
    @Spawns(ET_POINT)
    public Entity newPoint(SpawnData data) {
        int index = data.get("index");
        String dir = data.get("dir");
        TowerDefenseApp app = (TowerDefenseApp) (FXGL.getApp());
        app.getPointInfos().put(index, new Pair<>(new Point2D(data.getX(), data.getY()), dir));
        return FXGL.entityBuilder(data)
                .type(GameType.POINT) //类型
                .build();
    }

    @Spawns(ET_SPACE)
    public Entity newSpace(SpawnData data) {
        TowerDefenseApp app = (TowerDefenseApp) (FXGL.getApp());
        app.getSpaceInfos().add(new Rectangle(data.getX(), data.getY(), data.<Integer>get("width"), data.<Integer>get("height")));
        return FXGL.entityBuilder(data)
                .type(GameType.SPACE)
                .build();
    }

    @Spawns(ET_EMPTY)
    public Entity newEmpty(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(GameType.EMPTY)
                .collidable()
                .build();
    }

    /**
     * 如何创建敌人实体
     *
     * @param data 参数
     * @return
     */
    @Spawns(ET_ENEMY)
    public Entity newEnemy(SpawnData data) {
        int maxHp = 500;
        HealthIntComponent hp = new HealthIntComponent(maxHp); //血值组件
        ProgressBar hpBar = new ProgressBar(false); //进度条(显示)
        hpBar.setFill(Color.LIGHTGREEN);
        hpBar.setWidth(48);
        hpBar.setHeight(7);
        hpBar.setTranslateY(-5);
        hpBar.setMaxValue(maxHp);
        hpBar.setCurrentValue(maxHp);
        hpBar.currentValueProperty().bind(hp.valueProperty());
        hp.valueProperty().addListener((ob, ov, nv) -> {
            int value = nv.intValue();
            if (value > maxHp * 0.65) {
                hpBar.setFill(Color.LIGHTGREEN);
            } else if (value > maxHp * 0.25) {
                hpBar.setFill(Color.GOLD);
            } else {
                hpBar.setFill(Color.RED);
            }

        });
        return FXGL.entityBuilder(data)
                .type(GameType.ENEMY) //类型
                .view(hpBar) // 外显
                .with(hp) //+血值组件
                .with(new EnemyComponent(hpBar)) //+敌人组件
                .with(new CollidableComponent(true)) //+内置碰撞组件
                .bbox(BoundingShape.box(48, 48)) //碰撞检测矩形
                .build();
    }

    @Spawns(ET_LASER_TOWER_BULLET)
    public Entity spawnLaserBullet(SpawnData data) {
        return createBullet(data, "tower/laser/bullet.png",30,10);
    }


    @Spawns(ET_FLAME_TOWER_BULLET)
    public Entity newFlameBullet(SpawnData data) {
        return createBullet(data, "tower/flame/bullet.png",30,10);
    }

    @Spawns(ET_THUNDER_TOWER_BULLET)
    public Entity newThunderBullet(SpawnData data) {
        return createBullet(data, "tower/thunder/bullet.png",30,10);
    }

    @Spawns(ET_ARROW_TOWER_BULLET)
    public Entity newArrowBullet(SpawnData data) {
        return createBullet(data, "tower/arrow/bullet.png", 50, 10);
    }

    /**
     * @param data 位置及自定义属性
     * @param s    图片
     * @param w    碰撞矩形宽
     * @param h    碰撞矩形高
     * @return 子弹实体
     */
    private Entity createBullet(SpawnData data, String s, int w, int h) {
        return entityBuilder(data)
                .type(GameType.BULLET) //类型
                .viewWithBBox(FXGL.texture(s, w, h)) //外显及碰撞矩形
                .with(new CollidableComponent(true)) //+内置碰撞组件
                .with(new OffscreenCleanComponent()) //+内置离开屏幕后清理实体组件
                .with(new BulletComponent(data.get("radius"), data.get("damage"))) //+子弹组件
                .build();
    }

    @Spawns(ET_BUILDING_INDICATOR)
    public Entity newBuildIndicator(SpawnData data) {
        return entityBuilder(data)
                .with(new BuildIndicatorComponent())
                .zIndex(100)
                .build();
    }

    @Spawns(ET_PLACED_BUTTON)
    public Entity newPlacedButton(SpawnData data) {
        Texture texture = FXGL.texture((String) data.get("imgName"), data.get("width"), data.get("height"));
        texture.setTranslateX((80 - texture.getWidth()) / 2.0);
        texture.setTranslateY((80 - texture.getHeight()) / 2.0);

        Texture bgTexture = FXGL.texture("btnBg.png", 105, 105);
        bgTexture.setTranslateX((80 - bgTexture.getWidth()) / 2);
        bgTexture.setTranslateY((80 - bgTexture.getHeight()) / 2);
        return entityBuilder(data)
                .view(new Rectangle(80, 80, Color.web("#D5D5D511")))
                .view(bgTexture)
                .view(texture)
                .with(new PlacedButtonComponent(data.get("towerType")))
                .build();
    }

    @Spawns(ET_PLACE_BOX)
    public Entity newPlaceBox(SpawnData data) {
        return entityBuilder(data)
                .at(1000, 0)
                .view("chooseBg.png")
                .build();
    }
}
