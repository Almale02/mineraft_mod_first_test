package net.seconddanad.GoalPool;

import net.minecraft.entity.ai.goal.Goal;

public record GoalPair<T extends Goal>(int priority, T goal) {

}

