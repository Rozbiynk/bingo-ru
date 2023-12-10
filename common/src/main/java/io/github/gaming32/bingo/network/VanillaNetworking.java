package io.github.gaming32.bingo.network;

import com.google.common.collect.ImmutableMap;
import io.github.gaming32.bingo.Bingo;
import io.github.gaming32.bingo.game.ActiveGoal;
import io.github.gaming32.bingo.game.BingoBoard;
import net.minecraft.advancements.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;
import java.util.stream.IntStream;

public class VanillaNetworking {
    public static final AdvancementHolder ROOT_ADVANCEMENT = new AdvancementHolder(
        new ResourceLocation("bingo:generated/root"),
        new Advancement(
            Optional.empty(),
            Optional.of(new DisplayInfo(
                new ItemStack(Items.PLAYER_HEAD),
                Bingo.translatable("bingo.board.title"),
                CommonComponents.EMPTY,
                Optional.of(new ResourceLocation("minecraft:textures/gui/advancements/backgrounds/stone.png")),
                AdvancementType.TASK,
                false,
                false,
                true
            )),
            AdvancementRewards.EMPTY,
            ImmutableMap.of(),
            AdvancementRequirements.EMPTY,
            false
        )
    );

    public static final String CRITERION = "criterion";
//    public static final String[][] REQUIREMENTS = {{CRITERION}};
    public static final AdvancementRequirements REQUIREMENTS = AdvancementRequirements.allOf(List.of(CRITERION));

    public static List<AdvancementHolder> generateAdvancements(int size, ActiveGoal[] goals) {
        final List<AdvancementHolder> result = new ArrayList<>(1 + goals.length);
        result.add(ROOT_ADVANCEMENT);
        for (int i = 0; i < goals.length; i++) {
            result.add(generateAdvancement(i, goals[i], i % size, i / size));
        }
        return result;
    }

    public static AdvancementHolder generateAdvancement(int index, ActiveGoal goal, int x, int y) {
        final DisplayInfo displayInfo = new DisplayInfo(
            goal.icon().item(),
            goal.name(),
            goal.tooltip().orElse(CommonComponents.EMPTY),
            null,
            AdvancementType.TASK,
            false,
            false,
            false
        );
        displayInfo.setLocation(x + 0.5f, y);
        return new AdvancementHolder(
            BingoBoard.generateVanillaId(index),
            new Advancement(
                Optional.of(ROOT_ADVANCEMENT.id()),
                Optional.of(displayInfo),
                AdvancementRewards.EMPTY,
                ImmutableMap.of(),
                REQUIREMENTS,
                false
            )
        );
    }

    public static Map<ResourceLocation, AdvancementProgress> generateProgressMap(
        BingoBoard.Teams[] board, BingoBoard.Teams playerTeam
    ) {
        final Map<ResourceLocation, AdvancementProgress> result = new HashMap<>(board.length);
        for (int i = 0; i < board.length; i++) {
            result.put(BingoBoard.generateVanillaId(i), generateProgress(board[i].and(playerTeam)));
        }
        return result;
    }

    public static AdvancementProgress generateProgress(boolean complete) {
        final AdvancementProgress result = new AdvancementProgress();
        result.update(REQUIREMENTS);
        if (complete) {
            //noinspection DataFlowIssue
            result.getCriterion(CRITERION).grant();
        }
        return result;
    }

    public static Set<ResourceLocation> generateAdvancementIds(int count) {
        final Set<ResourceLocation> result = new HashSet<>(1 + count);
        result.add(ROOT_ADVANCEMENT.id());
        IntStream.range(0, count)
            .mapToObj(BingoBoard::generateVanillaId)
            .forEach(result::add);
        return result;
    }
}
