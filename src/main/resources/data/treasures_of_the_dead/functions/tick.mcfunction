#ROTATING SKULLS
execute as @e[tag=TOTD_Rotate] at @s run tp @s ~ ~ ~
execute as @e[tag=TOTD_Rotate] at @s run tag @s remove TOTD_Rotate

#CAPTAIN NAMES VISIBILITY
execute as @e[type=treasures_of_the_dead:captain_skeleton] run team join CaptainSkeletons @s
#execute as @e[team=CaptainSkeletons] at @s if entity @p[distance=..8] run data merge entity @s {CustomNameVisible:1}
#execute as @e[team=CaptainSkeletons] at @s if entity @p[distance=8..] run data merge entity @s {CustomNameVisible:0}

