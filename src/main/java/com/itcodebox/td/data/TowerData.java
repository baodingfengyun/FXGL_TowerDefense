package com.itcodebox.td.data;

import javafx.scene.image.Image;
import javafx.util.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 塔建筑数据
 *
 * @author LeeWyatt
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TowerData {
    private String name;            // 名字
    private int width;              // 宽
    private int height;             // 高
    private int damage;             // 伤害
    private int attackRadius;       // 攻击半径
    private int bulletSpeed;        // 子弹速度
    private Duration attackDelay;   // 攻击间隔
    private Image towerIcon;        // 图片
}
