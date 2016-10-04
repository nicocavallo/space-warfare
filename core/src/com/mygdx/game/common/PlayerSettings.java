package com.mygdx.game.common;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerSettings {

    private final Map<String,UpgradeObject> upgrades = new HashMap<String,UpgradeObject>();
    private BigInteger points = new BigInteger("0");

    public PlayerSettings()  {
        upgrades.put("health", new UpgradeObject(200,4){});
        upgrades.put("laserSpeed", new UpgradeObject(300,.5f,3,.9f){});
        upgrades.put("velocity", new UpgradeObject(200,2){});
    }

    public float getHealth() {
        return upgrades.get("health").getValue();
    }

    public float getVelocity() {
        return upgrades.get("velocity").getValue();
    }

    public float getLaserSpeed() {
        return upgrades.get("laserSpeed").getValue();
    }

    public List<UpgradeObject> getUpgrades() {
        List<UpgradeObject> res = new ArrayList<UpgradeObject>(upgrades.values());
        Collections.sort(res);
        return res;
    }

    public Integer incPoints() {
        return incPoints(1);
    }

    public Integer incPoints(Integer p) {
        this.points = points.add(new BigInteger(p.toString()));
        return points.intValue();
    }

    public Integer getPoints() {
        return points.intValue();
    }

    public void upgradeAll() {
        for (PlayerSettings.UpgradeObject uo: getUpgrades()) {
            points = new BigInteger(uo.upgrade(this.points.intValue()).toString());
        }
    }

    public static abstract class UpgradeObject implements Comparable<UpgradeObject>{

        private int price;
        private float value;
        private Integer level = 1;
        private float priceFactor = 2;
        private float valueFactor = 1.2f;

        public UpgradeObject(int initialPrice, float initialValue) {
            this(initialPrice,initialValue,2,1.2f);
        }

        public UpgradeObject(int initialPrice, float initialValue, float priceFactor, float valueFactor) {
            this.price = initialPrice;
            this.value = initialValue;
            this.priceFactor = priceFactor;
            this.valueFactor = valueFactor;
        }

        protected int incrementPrice(int price) {
            return price *= priceFactor;
        }

        protected float incrementValue(float value) {
            return value *= valueFactor;
        }

        public final Integer upgrade(int score) {
            if (score >= price) {
                int auxPoints = score - price;
                price = incrementPrice(price);
                value = incrementValue(value);
                level++;
                return auxPoints;
            }
            return score;
        }

        public int getPrice() {
            return price;
        }

        public float getValue() {
            return value;
        }

        public int getLevel() {
            return level;
        }

        @Override
        public int compareTo(UpgradeObject upgradeObject) {
            Integer result = this.level * this.price;
            return result.compareTo(upgradeObject.getLevel() * upgradeObject.getPrice());
        }
    }

}