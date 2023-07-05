package net.seconddanad.GoalPool;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;

import java.util.ArrayList;

public class GoalPool {
    public ArrayList<GoalPair> goals;

    public GoalPool(ArrayList<GoalPair> goals) {
        this.goals = goals;
    }
    public GoalPool() {
        this.goals = new ArrayList<>();
    }

    public<T extends Goal> void addGoal(int priority, T goal) {
        goals.add(new GoalPair<>(priority, goal));
    }
    public void activateGoals(GoalSelector selector) {
        selector.clear(x -> true);
        goals.forEach(val -> selector.add(val.priority(), val.goal()));
    }
}
