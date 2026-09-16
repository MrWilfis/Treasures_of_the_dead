package net.mrwilfis.treasures_of_the_dead.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;

import java.util.Arrays;
import java.util.List;

public class ReputationCommands {
    private static final List<String> COMPANIES = Arrays.asList(
            PlayerReputationData.ORDER_OF_SOULS,
            PlayerReputationData.GOLD_HOARDERS,
            PlayerReputationData.MERCHANT_ALLIANCE,
            PlayerReputationData.REAPERS_BONES
    );

    private static final SuggestionProvider<CommandSourceStack> COMPANY_SUGGESTIONS =
            (context, builder) -> SharedSuggestionProvider.suggest(COMPANIES, builder);

    /**
     * Получить локализованное название компании из файла перевода
     */
    private static Component getCompanyName(String company) {
        return Component.translatable("trading_company." + Treasures_of_the_dead.MOD_ID + "." + company);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reputation")
                .requires(source -> source.hasPermission(2))

                // ============ SET ============
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("company", StringArgumentType.word())
                                        .suggests(COMPANY_SUGGESTIONS)
                                        .then(Commands.literal("level")
                                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                            String company = StringArgumentType.getString(context, "company");
                                                            int value = IntegerArgumentType.getInteger(context, "value");

                                                            if (!COMPANIES.contains(company)) {
                                                                context.getSource().sendFailure(
                                                                        Component.translatable(
                                                                                "commands." + Treasures_of_the_dead.MOD_ID + ".unknown_company",
                                                                                String.join(", ", COMPANIES)
                                                                        )
                                                                );
                                                                return 0;
                                                            }

                                                            PlayerReputationData.setLevel(player, company, value);
                                                            PlayerReputationData.setExperience(player, company, 0);

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable(
                                                                            "commands." + Treasures_of_the_dead.MOD_ID + ".set.level.success",
                                                                            value,
                                                                            getCompanyName(company),
                                                                            player.getName().getString()
                                                                    ),
                                                                    true
                                                            );
                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("exp")
                                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                            String company = StringArgumentType.getString(context, "company");
                                                            int value = IntegerArgumentType.getInteger(context, "value");

                                                            if (!COMPANIES.contains(company)) {
                                                                context.getSource().sendFailure(
                                                                        Component.translatable(
                                                                                "commands." + Treasures_of_the_dead.MOD_ID + ".unknown_company",
                                                                                String.join(", ", COMPANIES)
                                                                        )
                                                                );
                                                                return 0;
                                                            }

                                                            PlayerReputationData.setExperience(player, company, value);

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable(
                                                                            "commands." + Treasures_of_the_dead.MOD_ID + ".set.exp.success",
                                                                            value,
                                                                            getCompanyName(company),
                                                                            player.getName().getString()
                                                                    ),
                                                                    true
                                                            );
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )

                // ============ ADD ============
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("company", StringArgumentType.word())
                                        .suggests(COMPANY_SUGGESTIONS)
                                        .then(Commands.literal("exp")
                                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                            String company = StringArgumentType.getString(context, "company");
                                                            int value = IntegerArgumentType.getInteger(context, "value");

                                                            if (!COMPANIES.contains(company)) {
                                                                context.getSource().sendFailure(
                                                                        Component.translatable(
                                                                                "commands." + Treasures_of_the_dead.MOD_ID + ".unknown_company",
                                                                                String.join(", ", COMPANIES)
                                                                        )
                                                                );
                                                                return 0;
                                                            }

                                                            PlayerReputationData.addExperience(player, company, value);

                                                            int newLevel = PlayerReputationData.getLevel(player, company);
                                                            int newExp = PlayerReputationData.getExperience(player, company);
                                                            int requiredExp = PlayerReputationData.getRequiredExperienceForLevel(newLevel);
                                                            int progress = requiredExp > 0 ? (newExp * 100) / requiredExp : 0;

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable(
                                                                            "commands." + Treasures_of_the_dead.MOD_ID + ".add.exp.success",
                                                                            value,
                                                                            getCompanyName(company),
                                                                            player.getName().getString(),
                                                                            newLevel,
                                                                            newExp,
                                                                            requiredExp,
                                                                            progress
                                                                    ),
                                                                    true
                                                            );
                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("level")
                                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                            String company = StringArgumentType.getString(context, "company");
                                                            int value = IntegerArgumentType.getInteger(context, "value");

                                                            if (!COMPANIES.contains(company)) {
                                                                context.getSource().sendFailure(
                                                                        Component.translatable(
                                                                                "commands." + Treasures_of_the_dead.MOD_ID + ".unknown_company",
                                                                                String.join(", ", COMPANIES)
                                                                        )
                                                                );
                                                                return 0;
                                                            }


                                                            for (int i = 0; i < value; i++) {
                                                                int requiredExp = PlayerReputationData.getRequiredExperienceForLevel(
                                                                        PlayerReputationData.getLevel(player, company)
                                                                );
                                                                PlayerReputationData.addExperience(player, company, requiredExp);
                                                            }

                                                            int newLevel = PlayerReputationData.getLevel(player, company);
                                                            int newExp = PlayerReputationData.getExperience(player, company);
                                                            int requiredExp = PlayerReputationData.getRequiredExperienceForLevel(newLevel);
                                                            int progress = requiredExp > 0 ? (newExp * 100) / requiredExp : 0;

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable(
                                                                            "commands." + Treasures_of_the_dead.MOD_ID + ".add.level.success",
                                                                            value,
                                                                            getCompanyName(company),
                                                                            player.getName().getString(),
                                                                            newLevel,
                                                                            newExp,
                                                                            requiredExp,
                                                                            progress
                                                                    ),
                                                                    true
                                                            );
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )

                // ============ REMOVE ============
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("company", StringArgumentType.word())
                                        .suggests(COMPANY_SUGGESTIONS)
                                        .then(Commands.literal("exp")
                                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                            String company = StringArgumentType.getString(context, "company");
                                                            int value = IntegerArgumentType.getInteger(context, "value");

                                                            if (!COMPANIES.contains(company)) {
                                                                context.getSource().sendFailure(
                                                                        Component.translatable(
                                                                                "commands." + Treasures_of_the_dead.MOD_ID + ".unknown_company",
                                                                                String.join(", ", COMPANIES)
                                                                        )
                                                                );
                                                                return 0;
                                                            }

                                                            int currentExp = PlayerReputationData.getExperience(player, company);
                                                            int currentLevel = PlayerReputationData.getLevel(player, company);
                                                            int newExp = Math.max(0, currentExp - value);

                                                            PlayerReputationData.setExperience(player, company, newExp);

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable(
                                                                            "commands." + Treasures_of_the_dead.MOD_ID + ".remove.exp.success",
                                                                            value,
                                                                            getCompanyName(company),
                                                                            player.getName().getString(),
                                                                            currentLevel,
                                                                            newExp,
                                                                            currentExp
                                                                    ),
                                                                    true
                                                            );
                                                            return 1;
                                                        })
                                                )
                                        )
                                        .then(Commands.literal("level")
                                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                            String company = StringArgumentType.getString(context, "company");
                                                            int value = IntegerArgumentType.getInteger(context, "value");

                                                            if (!COMPANIES.contains(company)) {
                                                                context.getSource().sendFailure(
                                                                        Component.translatable(
                                                                                "commands." + Treasures_of_the_dead.MOD_ID + ".unknown_company",
                                                                                String.join(", ", COMPANIES)
                                                                        )
                                                                );
                                                                return 0;
                                                            }

                                                            int currentLevel = PlayerReputationData.getLevel(player, company);
                                                            int newLevel = Math.max(0, currentLevel - value);
                                                            int currentExp = PlayerReputationData.getExperience(player, company);

                                                            PlayerReputationData.setLevel(player, company, newLevel);

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.translatable(
                                                                            "commands." + Treasures_of_the_dead.MOD_ID + ".remove.level.success",
                                                                            value,
                                                                            getCompanyName(company),
                                                                            player.getName().getString(),
                                                                            newLevel,
                                                                            currentExp
                                                                    ),
                                                                    true
                                                            );
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )

                // ============ GET ============
                .then(Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                    context.getSource().sendSuccess(
                                            () -> Component.translatable(
                                                    "commands." + Treasures_of_the_dead.MOD_ID + ".get.header",
                                                    player.getName().getString()
                                            ),
                                            true
                                    );

                                    for (String company : COMPANIES) {
                                        int level = PlayerReputationData.getLevel(player, company);
                                        int exp = PlayerReputationData.getExperience(player, company);
                                        int requiredExp = PlayerReputationData.getRequiredExperienceForLevel(level);
                                        int progress = requiredExp > 0 ? (exp * 100) / requiredExp : 0;

                                        context.getSource().sendSuccess(
                                                () -> Component.translatable(
                                                        "commands." + Treasures_of_the_dead.MOD_ID + ".get.entry",
                                                        getCompanyName(company),
                                                        level,
                                                        exp,
                                                        requiredExp,
                                                        progress
                                                ),
                                                true
                                        );
                                    }

                                    return 1;
                                })
                        )
                )

                // ============ RESET ============
                .then(Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("company", StringArgumentType.word())
                                        .suggests(COMPANY_SUGGESTIONS)
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            String company = StringArgumentType.getString(context, "company");

                                            if (!COMPANIES.contains(company)) {
                                                context.getSource().sendFailure(
                                                        Component.translatable(
                                                                "commands." + Treasures_of_the_dead.MOD_ID + ".unknown_company",
                                                                String.join(", ", COMPANIES)
                                                        )
                                                );
                                                return 0;
                                            }

                                            PlayerReputationData.setLevel(player, company, 0);
                                            PlayerReputationData.setExperience(player, company, 0);

                                            context.getSource().sendSuccess(
                                                    () -> Component.translatable(
                                                            "commands." + Treasures_of_the_dead.MOD_ID + ".reset.success",
                                                            getCompanyName(company),
                                                            player.getName().getString()
                                                    ),
                                                    true
                                            );
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }
}
